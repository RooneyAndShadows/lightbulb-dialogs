package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapter;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterConfiguration;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterDataModel;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterSelectableModes;
import com.github.rooneyandshadows.lightbulb.selectableview.CheckBoxView;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("unchecked")
public class CheckBoxSelectableAdapter<ItemType extends LightBulbAdapterDataModel> extends LightBulbAdapter<ItemType> {

    public CheckBoxSelectableAdapter() {
        super(new LightBulbAdapterConfiguration<ItemType>().withSelectMode(LightBulbAdapterSelectableModes.SELECT_MULTIPLE));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CheckBoxView v = (CheckBoxView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_checkbox_button, parent, false);
        return new CheckBoxViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CheckBoxViewHolder vHolder = (CheckBoxViewHolder) holder;
        vHolder.bindItem();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        CheckBoxViewHolder vh = (CheckBoxViewHolder) holder;
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

    public class CheckBoxViewHolder extends RecyclerView.ViewHolder {
        protected CheckBoxView selectableView;
        protected ItemType item;

        CheckBoxViewHolder(CheckBoxView categoryItemBinding, LightBulbAdapter<ItemType> adapter) {
            super(categoryItemBinding);
            this.selectableView = (CheckBoxView) itemView;
            selectableView.setOnCheckedListener((view, isChecked) -> selectableView.post(() -> adapter.selectItemAt(getBindingAdapterPosition(), isChecked)));
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