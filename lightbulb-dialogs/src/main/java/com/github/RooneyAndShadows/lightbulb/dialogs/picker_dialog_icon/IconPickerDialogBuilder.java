package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog;

import androidx.fragment.app.FragmentManager;

public class IconPickerDialogBuilder extends LightBulbDialogBuilder<IconPickerDialog> {
    private LightBulbPickerDialogFragment.SelectionChangedListener<int[]> changedCallback;
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
    public IconPickerDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (IconPickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public IconPickerDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (IconPickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public IconPickerDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (IconPickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public IconPickerDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (IconPickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public IconPickerDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (IconPickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public IconPickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (IconPickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public IconPickerDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (IconPickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public IconPickerDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
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
