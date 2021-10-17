package com.github.RooneyAndShadows.lightbulb.dialogs.picker_dialog_color;

import com.github.RooneyAndShadows.lightbulb.dialogs.base.BaseDialogBuilder;
import com.github.RooneyAndShadows.lightbulb.dialogs.base.BaseDialogFragment;
import com.github.RooneyAndShadows.lightbulb.dialogs.base.BasePickerDialogFragment;

import androidx.fragment.app.FragmentManager;

public class ColorPickerDialogBuilder extends BaseDialogBuilder<ColorPickerDialog> {
    private BasePickerDialogFragment.SelectionChangedListener<int[]> changedCallback;
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
    public ColorPickerDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (ColorPickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public ColorPickerDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (ColorPickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public ColorPickerDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (ColorPickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public ColorPickerDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (ColorPickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public ColorPickerDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (ColorPickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public ColorPickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (ColorPickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public ColorPickerDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (ColorPickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public ColorPickerDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (ColorPickerDialogBuilder) super.withAnimations(animation);
    }

    public ColorPickerDialogBuilder withSelectionCallback(BasePickerDialogFragment.SelectionChangedListener<int[]> listener) {
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
