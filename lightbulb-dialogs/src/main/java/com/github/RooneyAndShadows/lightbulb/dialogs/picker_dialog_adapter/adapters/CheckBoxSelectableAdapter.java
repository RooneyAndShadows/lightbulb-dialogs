package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.rooneyandshadows.lightbulb.recycleradapters.EasyAdapterConfiguration;
import com.github.rooneyandshadows.lightbulb.recycleradapters.EasyAdapterDataModel;
import com.github.rooneyandshadows.lightbulb.recycleradapters.EasyAdapterSelectableModes;
import com.github.rooneyandshadows.lightbulb.recycleradapters.EasyRecyclerAdapter;
import com.github.rooneyandshadows.lightbulb.selectableview.CheckBoxView;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("unchecked")
public class CheckBoxSelectableAdapter<ItemType extends EasyAdapterDataModel> extends EasyRecyclerAdapter<ItemType> {

    public CheckBoxSelectableAdapter() {
        super(new EasyAdapterConfiguration<ItemType>().withSelectMode(EasyAdapterSelectableModes.SELECT_MULTIPLE));
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

        CheckBoxViewHolder(CheckBoxView categoryItemBinding, EasyRecyclerAdapter<ItemType> adapter) {
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