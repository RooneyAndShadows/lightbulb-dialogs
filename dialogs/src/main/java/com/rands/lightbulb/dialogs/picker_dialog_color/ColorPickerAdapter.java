package com.rands.lightbulb.dialogs.picker_dialog_color;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rands.lightbulb.commons.utils.ResourceUtils;
import com.rands.lightbulb.dialogs.R;
import com.rands.lightbulb.recycleradapters.EasyAdapterConfiguration;
import com.rands.lightbulb.recycleradapters.EasyAdapterDataModel;
import com.rands.lightbulb.recycleradapters.EasyAdapterSelectableModes;
import com.rands.lightbulb.recycleradapters.EasyRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import static com.rands.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter.*;

public class ColorPickerAdapter extends EasyRecyclerAdapter<ColorModel> {
    private final Context context;

    public ColorPickerAdapter(Context context, EasyAdapterSelectableModes selectableMode) {
        super(new EasyAdapterConfiguration<ColorModel>().withSelectMode(selectableMode));
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppCompatImageButton v = (AppCompatImageButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_color_item, parent, false);
        return new ColorVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ColorVH vh = (ColorVH) holder;
        ColorModel item = items.get(position);
        vh.setItem(item, position, this);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ColorVH vh = (ColorVH) holder;
        vh.release();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public String getItemName(ColorModel item) {
        return item.getItemName();
    }

    @Override
    public void setCollection(List<ColorModel> collection) {
        super.setCollection(validatePendingCollection(collection));
    }

    @Override
    public void appendCollection(List<ColorModel> collection) {
        super.appendCollection(validatePendingCollection(collection));
    }

    public Drawable getColorDrawable(ColorModel colorModel) {
        if (colorModel == null)
            return null;
        Drawable colorDrawable = ResourceUtils.getDrawable(context, R.drawable.color_icon).mutate();
        colorDrawable.setTint(Color.parseColor(colorModel.colorHex));
        return colorDrawable;
    }

    private List<ColorModel> validatePendingCollection(List<ColorModel> inputCollection) {
        List<ColorModel> outputCollection = new ArrayList<>();
        for (ColorModel colorInput : inputCollection) {
            try {
                Color.parseColor(colorInput.colorHex);
                outputCollection.add(colorInput);
            } catch (Exception e) {
                //ignore
            }
        }
        return outputCollection;
    }

    public static class ColorVH extends RecyclerView.ViewHolder {
        protected AppCompatImageButton colorView;
        protected ColorModel item;

        ColorVH(AppCompatImageButton view) {
            super(view);
            this.colorView = (AppCompatImageButton) itemView;
        }

        public void setItem(ColorModel item, int position, ColorPickerAdapter adapter) {
            this.item = item;
            Context ctx = colorView.getContext();
            boolean isItemSelected = adapter.isItemSelected(position);
            int drawableId = isItemSelected ? R.drawable.color_selected_icon : R.drawable.color_icon;
            Drawable iconDrawable = ResourceUtils.getDrawable(ctx, drawableId).mutate();
            iconDrawable.setTint(Color.parseColor(item.colorHex));
            //colorView.setColorFilter(Color.parseColor(item.colorHex));
            colorView.setImageDrawable(iconDrawable);
            colorView.setOnClickListener(v -> {
                boolean prevState = adapter.isItemSelected(position);
                adapter.selectItemAt(position, !prevState);
            });
        }

        public void release() {
            colorView.setImageDrawable(null);
        }
    }

    public static class ColorModel extends EasyAdapterDataModel {
        private final String colorHex;
        private final String colorExternalName;

        public ColorModel(String iconName, String externalName) {
            super(false);
            this.colorExternalName = externalName;
            this.colorHex = iconName;
        }

        // Parcelling part
        public ColorModel(Parcel in) {
            super(in);
            this.colorHex = in.readString();
            this.colorExternalName = in.readString();
        }

        public static final Parcelable.Creator<ColorModel> CREATOR = new Parcelable.Creator<ColorModel>() {
            public ColorModel createFromParcel(Parcel in) {
                return new ColorModel(in);
            }

            public ColorModel[] newArray(int size) {
                return new ColorModel[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int i) {
            dest.writeString(colorHex);
            dest.writeString(colorExternalName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String getItemName() {
            return colorExternalName;
        }

        public String getColorHex() {
            return colorHex;
        }

        public String getColorExternalName() {
            return colorExternalName;
        }
    }
}