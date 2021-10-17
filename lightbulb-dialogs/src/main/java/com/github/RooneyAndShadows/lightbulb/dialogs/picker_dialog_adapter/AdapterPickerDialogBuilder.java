package com.github.RooneyAndShadows.lightbulb.dialogs.picker_dialog_adapter;


import com.github.RooneyAndShadows.lightbulb.dialogs.base.BaseDialogBuilder;
import com.github.RooneyAndShadows.lightbulb.dialogs.base.BaseDialogFragment;
import com.github.RooneyAndShadows.lightbulb.recycleradapters.EasyAdapterDataModel;
import com.github.RooneyAndShadows.lightbulb.recycleradapters.EasyRecyclerAdapter;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPickerDialogBuilder<ModelType extends EasyAdapterDataModel> extends BaseDialogBuilder<AdapterPickerDialog<ModelType>> {
    private final EasyRecyclerAdapter<ModelType> adapter;
    private AdapterPickerDialog.SelectionChangedListener<int[]> changedCallback;
    private RecyclerView.ItemDecoration itemDecoration;
    private int[] selection;

    public AdapterPickerDialogBuilder(FragmentManager manager, String dialogTag, EasyRecyclerAdapter<ModelType> adapter) {
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
    public AdapterPickerDialogBuilder<ModelType> withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withOnCancelListener(listener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withOnShowListener(listener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withOnHideListener(listener);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (AdapterPickerDialogBuilder<ModelType>) super.withDialogType(dialogType);
    }

    @Override
    public AdapterPickerDialogBuilder<ModelType> withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
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
