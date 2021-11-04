package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapter;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterConfiguration;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterDataModel;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterSelectableModes;
import com.github.rooneyandshadows.lightbulb.selectableview.RadioButtonView;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings({"unchecked"})
public class RadioButtonSelectableAdapter<ItemType extends LightBulbAdapterDataModel> extends LightBulbAdapter<ItemType> {

    public RadioButtonSelectableAdapter() {
        super(new LightBulbAdapterConfiguration<ItemType>().withSelectMode(LightBulbAdapterSelectableModes.SELECT_SINGLE));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RadioButtonView v = (RadioButtonView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_radio_button, parent, false);
        return new RadioButtonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RadioButtonViewHolder vHolder = (RadioButtonViewHolder) holder;
        vHolder.bindItem();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        RadioButtonViewHolder vh = (RadioButtonViewHolder) holder;
        vh.recycle();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public String getItemName(ItemType item) {
        return item.getItemName();
    }

    protected Drawable getItemIcon(ItemType item) {
        return null;
    }

    protected Drawable getItemIconBackground(ItemType item) {
        return null;
    }

    public class RadioButtonViewHolder extends RecyclerView.ViewHolder {
        protected RadioButtonView selectableView;
        protected ItemType item;

        public RadioButtonViewHolder(RadioButtonView categoryItemBinding) {
            super(categoryItemBinding);
            this.selectableView = (RadioButtonView) itemView;
            selectableView.setOnCheckedListener((view, isChecked) -> selectableView.post(() -> selectItem(item, isChecked)));
        }

        public void bindItem() {
            this.item = getItem(getBindingAdapterPosition());
            boolean isSelectedInAdapter = isItemSelected(item);
            if (selectableView.isChecked() != isSelectedInAdapter)
                selectableView.setChecked(isSelectedInAdapter);
            selectableView.setText(getItemName(item));
            selectableView.setIcon(getItemIcon(item), getItemIconBackground(item));
        }

        private void recycle() {
            selectableView.setIcon(null, null);
        }
    }
}