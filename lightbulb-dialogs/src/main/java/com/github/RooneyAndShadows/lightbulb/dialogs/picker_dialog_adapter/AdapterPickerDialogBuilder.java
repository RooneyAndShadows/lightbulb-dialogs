package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter;


import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapter;
import com.github.rooneyandshadows.lightbulb.recycleradapters.LightBulbAdapterDataModel;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPickerDialogBuilder<ModelType extends LightBulbAdapterDataModel> extends LightBulbDialogBuilder<AdapterPickerDialog<ModelType>> {
    private final LightBulbAdapter<ModelType> adapter;
    private AdapterPickerDialog.SelectionChangedListener<int[]> changedCallback;
    private RecyclerView.ItemDecoration itemDecoration;
    private int[] selection;

    public AdapterPickerDialogBuilder(FragmentManager manager, String dialogTag, LightBulbAdapter<ModelType> adapter) {
        super(manager, dialogTag);
        this.adapter = adapter;
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withTitle(String title) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withTitle(title);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withMessage(String message) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withMessage(message);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withOnCancelListener(listener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withOnShowListener(listener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withOnHideListener(listener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withDialogType(dialogType);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withAnimations(animation);
    }

    public AdapterPickerDialogBuilder<ModelType> withSelectionCallback(AdapterPickerDialog.SelectionChangedListener<int[]> listener) {
        changedCallback = listener;
        return this;
    }

    public AdapterPickerDialogBuilder<ModelType> withSelection(int[] selection) {
        this.selection = selection;
        return this;
    }

    public AdapterPickerDialogBuilder<ModelType> withItemDecoration(RecyclerView.ItemDecoration decoration) {
        this.itemDecoration = decoration;
        return this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public AdapterPickerDialog<ModelType> buildDialog() {
        AdapterPickerDialog<ModelType> dialogFragment = (AdapterPickerDialog<ModelType>) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = AdapterPickerDialog.newInstance(title, message, positiveButtonConfiguration, negativeButtonConfiguration, cancelableOnClickOutside, dialogType, animation);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener);
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        dialogFragment.setAdapter(adapter);
        dialogFragment.setOnSelectionChangedListener(changedCallback);
        dialogFragment.setSelection(selection);
        dialogFragment.setItemDecoration(itemDecoration);
        return dialogFragment;
    }
}
