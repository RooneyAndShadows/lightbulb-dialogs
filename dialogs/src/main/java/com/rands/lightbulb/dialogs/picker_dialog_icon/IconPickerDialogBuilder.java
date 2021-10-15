package com.rands.lightbulb.dialogs.picker_dialog_icon;

import com.rands.lightbulb.dialogs.base.BaseDialogBuilder;
import com.rands.lightbulb.dialogs.base.BaseDialogFragment;
import com.rands.lightbulb.dialogs.base.BasePickerDialogFragment;
import com.rands.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog;

import androidx.fragment.app.FragmentManager;

import static com.rands.lightbulb.dialogs.base.BaseDialogFragment.*;

public class IconPickerDialogBuilder extends BaseDialogBuilder<IconPickerDialog> {
    private BasePickerDialogFragment.SelectionChangedListener<int[]> changedCallback;
    private int[] selection;
    private final IconPickerAdapter adapter;


    public IconPickerDialogBuilder(FragmentManager manager, String dialogTag, IconPickerAdapter adapter) {
        super(manager, dialogTag);
        this.adapter = adapter;
    }

    @Override
    public IconPickerDialogBuilder withTitle(String title) {
        return (IconPickerDialogBuilder) super.withTitle(title);
    }

    @Override
    public IconPickerDialogBuilder withMessage(String message) {
        return (IconPickerDialogBuilder) super.withMessage(message);
    }

    @Override
    public IconPickerDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (IconPickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public IconPickerDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (IconPickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public IconPickerDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (IconPickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public IconPickerDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (IconPickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public IconPickerDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (IconPickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public IconPickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (IconPickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public IconPickerDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (IconPickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public IconPickerDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (IconPickerDialogBuilder) super.withAnimations(animation);
    }

    public IconPickerDialogBuilder withSelectionCallback(AdapterPickerDialog.SelectionChangedListener<int[]> listener) {
        changedCallback = listener;
        return this;
    }

    public IconPickerDialogBuilder withSelection(int[] selection) {
        this.selection = selection;
        return this;
    }

    @Override
    public IconPickerDialog buildDialog() {
        IconPickerDialog iconPickerDialog = (IconPickerDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (iconPickerDialog == null)
            iconPickerDialog = IconPickerDialog.newInstance(title, message, positiveButtonConfiguration, negativeButtonConfiguration, cancelableOnClickOutside, animation);
        iconPickerDialog.setFragmentManager(fragmentManager);
        iconPickerDialog.setDialogTag(dialogTag);
        iconPickerDialog.addOnShowListener(onShowListener);
        iconPickerDialog.addOnHideListener(onHideListener);
        iconPickerDialog.addOnCancelListener(onCancelListener);
        iconPickerDialog.addOnNegativeClickListeners(onNegativeClickListener);
        iconPickerDialog.addOnPositiveClickListener(onPositiveClickListener);
        iconPickerDialog.setAdapter(adapter);
        iconPickerDialog.setOnSelectionChangedListener(changedCallback);
        iconPickerDialog.setSelection(selection);
        return iconPickerDialog;
    }
}
