package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;

import androidx.fragment.app.FragmentManager;

public class ColorPickerDialogBuilder extends LightBulbDialogBuilder<ColorPickerDialog> {
    private LightBulbPickerDialogFragment.SelectionChangedListener<int[]> changedCallback;
    private int[] selection;
    private final ColorPickerAdapter adapter;


    public ColorPickerDialogBuilder(FragmentManager manager, String dialogTag, ColorPickerAdapter adapter) {
        super(manager, dialogTag);
        this.adapter = adapter;
    }

    @Override
    public ColorPickerDialogBuilder withTitle(String title) {
        return (ColorPickerDialogBuilder) super.withTitle(title);
    }

    @Override
    public ColorPickerDialogBuilder withMessage(String message) {
        return (ColorPickerDialogBuilder) super.withMessage(message);
    }

    @Override
    public ColorPickerDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (ColorPickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public ColorPickerDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (ColorPickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public ColorPickerDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (ColorPickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public ColorPickerDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (ColorPickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public ColorPickerDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (ColorPickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public ColorPickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (ColorPickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public ColorPickerDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (ColorPickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public ColorPickerDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (ColorPickerDialogBuilder) super.withAnimations(animation);
    }

    public ColorPickerDialogBuilder withSelectionCallback(LightBulbPickerDialogFragment.SelectionChangedListener<int[]> listener) {
        changedCallback = listener;
        return this;
    }

    public ColorPickerDialogBuilder withSelection(int[] selection) {
        this.selection = selection;
        return this;
    }

    @Override
    public ColorPickerDialog buildDialog() {
        ColorPickerDialog colorPickerDialog = (ColorPickerDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (colorPickerDialog == null)
            colorPickerDialog = ColorPickerDialog.newInstance(title, message, positiveButtonConfiguration, negativeButtonConfiguration, cancelableOnClickOutside, animation);
        colorPickerDialog.setFragmentManager(fragmentManager);
        colorPickerDialog.setDialogTag(dialogTag);
        colorPickerDialog.addOnShowListener(onShowListener);
        colorPickerDialog.addOnHideListener(onHideListener);
        colorPickerDialog.addOnCancelListener(onCancelListener);
        colorPickerDialog.addOnNegativeClickListeners(onNegativeClickListener);
        colorPickerDialog.addOnPositiveClickListener(onPositiveClickListener);
        colorPickerDialog.setAdapter(adapter);
        colorPickerDialog.setOnSelectionChangedListener(changedCallback);
        colorPickerDialog.setSelection(selection);
        return colorPickerDialog;
    }
}
