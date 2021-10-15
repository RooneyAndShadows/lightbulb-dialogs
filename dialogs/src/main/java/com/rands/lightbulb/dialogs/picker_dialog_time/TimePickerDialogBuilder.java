package com.rands.lightbulb.dialogs.picker_dialog_time;

import com.rands.lightbulb.dialogs.base.BaseDialogBuilder;
import com.rands.lightbulb.dialogs.base.BaseDialogFragment;
import com.rands.lightbulb.dialogs.base.BasePickerDialogFragment;

import androidx.fragment.app.FragmentManager;

import static com.rands.lightbulb.dialogs.base.BaseDialogFragment.*;

public class TimePickerDialogBuilder extends BaseDialogBuilder<TimePickerDialog> {

    private BasePickerDialogFragment.SelectionChangedListener<int[]> timeSetListener;
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
    public TimePickerDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (TimePickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public TimePickerDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (TimePickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public TimePickerDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (TimePickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public TimePickerDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (TimePickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public TimePickerDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (TimePickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public TimePickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (TimePickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public TimePickerDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (TimePickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public TimePickerDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (TimePickerDialogBuilder) super.withAnimations(animation);
    }

    public TimePickerDialogBuilder withOnDateSelectedEvent(BasePickerDialogFragment.SelectionChangedListener<int[]> listener) {
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
