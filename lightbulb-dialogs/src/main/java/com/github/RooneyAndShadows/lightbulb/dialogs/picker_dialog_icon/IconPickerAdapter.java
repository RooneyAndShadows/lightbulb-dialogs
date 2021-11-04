package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.rooneyandshadows.lightbulb.commons.utils.IconUtils;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapter;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterConfiguration;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterDataModel;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterSelectableModes;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import static com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter.IconModel;

public class IconPickerAdapter extends LightBulbAdapter<IconModel> {
    private final Context context;

    public IconPickerAdapter(Context context, LightBulbAdapterSelectableModes selectableMode) {
        super(new LightBulbAdapterConfiguration<IconModel>().withSelectMode(selectableMode));
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppCompatImageButton v = (AppCompatImageButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_icon_item, parent, false);
        return new IconVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        IconModel item = items.get(position);
        IconVH vh = (IconVH) holder;
        vh.setItem(item, position, this);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        IconVH vh = (IconVH) holder;
        vh.release();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public String getItemName(IconModel item) {
        return item.getItemName();
    }

    @Override
    public void setCollection(List<IconModel> collection) {
        super.setCollection(validatePendingCollection(collection));
    }

    @Override
    public void appendCollection(List<IconModel> collection) {
        super.appendCollection(validatePendingCollection(collection));
    }

    public Drawable getDrawable(IconModel iconModel, int size) {
        if (size < 0)
            size = 0;
        IIcon icon = getIconValue(iconModel);
        if (icon == null)
            return null;
        switch (iconModel.getIconSet()) {
            case FONTAWESOME:
                return IconUtils.getIconWithAttributeColor(context, icon, R.attr.colorOnSurface, size);
            default:
                return null;
        }
    }

    private IIcon getIconValue(IconModel iconModel) {
        switch (iconModel.getIconSet()) {
            case FONTAWESOME:
                String iconInputName = iconModel.getIconName()
                        .replace("-", "_")
                        .replace("fa_", "faw_");//To match enum name
                try {
                    return FontAwesome.Icon.valueOf(iconInputName);
                } catch (Exception e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private List<IconModel> validatePendingCollection(List<IconModel> inputCollection) {
        List<IconModel> outputCollection = new ArrayList<>();
        for (IconModel inputIcon : inputCollection) {
            IIcon icon = getIconValue(inputIcon);
            if (icon != null)
                outputCollection.add(inputIcon);
        }
        return outputCollection;
    }

    public class IconVH extends RecyclerView.ViewHolder {
        protected AppCompatImageButton iconView;
        protected IconModel item;

        IconVH(AppCompatImageButton view) {
            super(view);
            this.iconView = (AppCompatImageButton) itemView;
        }

        public void setItem(IconModel item, int position, IconPickerAdapter adapter) {
            this.item = item;
            boolean isItemSelected = adapter.isItemSelected(position);
            int colorAttribute = isItemSelected ? R.attr.colorAccent : R.attr.colorOnSurface;
            int alpha = isItemSelected ? 255 : (int) (255 * 0.3);
            int iconSize = ResourceUtils.getDimenPxById(context, R.dimen.dialog_icon_picker_icon_size);
            Drawable iconDrawable = IconUtils.getIconWithAttributeColor(iconView.getContext(), getIconValue(item), colorAttribute, iconSize);
            iconDrawable.setAlpha(alpha);
            iconView.setImageDrawable(iconDrawable);
            iconView.setOnClickListener(v -> {
                boolean prevState = adapter.isItemSelected(position);
                adapter.selectItemAt(position, !prevState);
            });
        }

        public void release() {
            iconView.setImageDrawable(null);
        }
    }

    public static class IconModel extends LightBulbAdapterDataModel {
        private final String iconName;
        private final IconSet iconSet;
        private final String iconExternalName;

        public IconModel(String iconName, String externalName, IconSet iconSet) {
            super(false);
            this.iconExternalName = externalName;
            this.iconName = iconName;
            this.iconSet = iconSet;
        }

        // Parcelling part
        public IconModel(Parcel in) {
            super(in);
            this.iconName = in.readString();
            this.iconExternalName = in.readString();
            this.iconSet = IconSet.valueOf(in.readInt());
        }

        public static final Parcelable.Creator<IconModel> CREATOR = new Parcelable.Creator<IconModel>() {
            public IconModel createFromParcel(Parcel in) {
                return new IconModel(in);
            }

            public IconModel[] newArray(int size) {
                return new IconModel[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int i) {
            dest.writeString(iconName);
            dest.writeString(iconExternalName);
            dest.writeInt(iconSet.getValue());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String getItemName() {
            return iconExternalName;
        }

        public String getIconName() {
            return iconName;
        }

        public IconSet getIconSet() {
            return iconSet;
        }

        public String getIconExternalName() {
            return iconExternalName;
        }

        public static List<IconModel> getAllForSet(IconSet set) {
            switch (set) {
                case FONTAWESOME:
                    List<IconModel> icons = new ArrayList<>();
                    for (FontAwesome.Icon icon : FontAwesome.Icon.values())
                        icons.add(new IconModel(icon.getName(), icon.getName(), IconSet.FONTAWESOME));
                    return icons;
                default:
                    return null;
            }
        }
    }

    public enum IconSet {
        FONTAWESOME(1, "FontAwesome");
        private final Integer value;
        private final String name;
        private static final Map<Integer, IconSet> mapValues = new HashMap<>();
        private static final Map<String, IconSet> mapNames = new HashMap<>();

        IconSet(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (IconSet type : IconSet.values()) {
                mapValues.put(type.value, type);
                mapNames.put(type.name, type);
            }
        }

        public static IconSet valueOf(Integer type) {
            return mapValues.get(type);
        }

        public static IconSet byName(String type) {
            return mapNames.get(type);
        }

        public static Boolean has(String type) {
            return mapNames.containsKey(type);
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}