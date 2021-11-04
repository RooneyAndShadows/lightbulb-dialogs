package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;

import java.util.Date;

import androidx.fragment.app.FragmentManager;

public class DateRangePickerDialogBuilder extends LightBulbDialogBuilder<DateRangePickerDialog> {

    private LightBulbPickerDialogFragment.SelectionChangedListener<Date[]> dateSetListener;
    private String textFrom;
    private String textTo;
    private String dateFormat;
    private Date[] initialRange;

    public DateRangePickerDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    @Override
    public DateRangePickerDialogBuilder withTitle(String title) {
        return (DateRangePickerDialogBuilder) super.withTitle(title);
    }

    @Override
    public DateRangePickerDialogBuilder withMessage(String message) {
        return (DateRangePickerDialogBuilder) super.withMessage(message);
    }

    @Override
    public DateRangePickerDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (DateRangePickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public DateRangePickerDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (DateRangePickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public DateRangePickerDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (DateRangePickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public DateRangePickerDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (DateRangePickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public DateRangePickerDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (DateRangePickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public DateRangePickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (DateRangePickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public DateRangePickerDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (DateRangePickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public DateRangePickerDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (DateRangePickerDialogBuilder) super.withAnimations(animation);
    }

    public DateRangePickerDialogBuilder withTextFrom(String textFrom) {
        this.textFrom = textFrom;
        return this;
    }

    public DateRangePickerDialogBuilder withTextTo(String textTo) {
        this.textTo = textTo;
        return this;
    }

    public DateRangePickerDialogBuilder withOnDateSelectedEvent(LightBulbPickerDialogFragment.SelectionChangedListener<Date[]> listener) {
        this.dateSetListener = listener;
        return this;
    }

    public DateRangePickerDialogBuilder withSelection(Date[] range) {
        this.initialRange = range;
        return this;
    }

    public DateRangePickerDialogBuilder withDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    @Override
    public DateRangePickerDialog buildDialog() {
        DateRangePickerDialog dialogFragment = (DateRangePickerDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = DateRangePickerDialog.newInstance(positiveButtonConfiguration, negativeButtonConfiguration, dateFormat, textFrom, textTo, cancelableOnClickOutside, animation);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener);
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener);
        dialogFragment.setOnSelectionChangedListener(dateSetListener);
        dialogFragment.setSelection(initialRange);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        return dialogFragment;
    }
}
