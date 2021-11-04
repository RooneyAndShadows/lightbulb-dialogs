package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;

import androidx.fragment.app.FragmentManager;

public class TimePickerDialogBuilder extends LightBulbDialogBuilder<TimePickerDialog> {

    private LightBulbPickerDialogFragment.SelectionChangedListener<int[]> timeSetListener;
    private int[] initialTime;

    public TimePickerDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    @Override
    public TimePickerDialogBuilder withTitle(String title) {
        return (TimePickerDialogBuilder) super.withTitle(title);
    }

    @Override
    public TimePickerDialogBuilder withMessage(String message) {
        return (TimePickerDialogBuilder) super.withMessage(message);
    }

    @Override
    public TimePickerDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (TimePickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public TimePickerDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (TimePickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public TimePickerDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (TimePickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public TimePickerDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (TimePickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public TimePickerDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (TimePickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public TimePickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (TimePickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public TimePickerDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (TimePickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public TimePickerDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (TimePickerDialogBuilder) super.withAnimations(animation);
    }

    public TimePickerDialogBuilder withOnDateSelectedEvent(LightBulbPickerDialogFragment.SelectionChangedListener<int[]> listener) {
        this.timeSetListener = listener;
        return this;
    }

    public TimePickerDialogBuilder withInitialTime(int hour, int minute) {
        this.initialTime = new int[]{hour, minute};
        return this;
    }

    public TimePickerDialogBuilder withInitialTime(int[] time) {
        this.initialTime = time;
        return this;
    }

    @Override
    public TimePickerDialog buildDialog() {
        TimePickerDialog dialogFragment = (TimePickerDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = TimePickerDialog.newInstance(positiveButtonConfiguration, negativeButtonConfiguration, cancelableOnClickOutside, animation);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener);
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener);
        dialogFragment.setSelection(initialTime);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        dialogFragment.setOnSelectionChangedListener(timeSetListener);
        return dialogFragment;
    }
}
