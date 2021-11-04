package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;

import java.util.Date;

import androidx.fragment.app.FragmentManager;

public class DateTimePickerDialogBuilder extends LightBulbDialogBuilder<DateTimePickerDialog> {

    private LightBulbPickerDialogFragment.SelectionChangedListener<Date> dateSetListener;
    private Date initialDate;
    private String dateFormat;

    public DateTimePickerDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    @Override
    public DateTimePickerDialogBuilder withTitle(String title) {
        return (DateTimePickerDialogBuilder) super.withTitle(title);
    }

    @Override
    public DateTimePickerDialogBuilder withMessage(String message) {
        return (DateTimePickerDialogBuilder) super.withMessage(message);
    }

    @Override
    public DateTimePickerDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (DateTimePickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public DateTimePickerDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (DateTimePickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public DateTimePickerDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (DateTimePickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public DateTimePickerDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (DateTimePickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public DateTimePickerDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (DateTimePickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public DateTimePickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (DateTimePickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public DateTimePickerDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (DateTimePickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public DateTimePickerDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (DateTimePickerDialogBuilder) super.withAnimations(animation);
    }

    public DateTimePickerDialogBuilder withOnDateSelectedEvent(LightBulbPickerDialogFragment.SelectionChangedListener<Date> listener) {
        this.dateSetListener = listener;
        return this;
    }

    public DateTimePickerDialogBuilder withSelection(Date date) {
        this.initialDate = date;
        return this;
    }

    public DateTimePickerDialogBuilder withDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    @Override
    public DateTimePickerDialog buildDialog() {
        DateTimePickerDialog dialogFragment = (DateTimePickerDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = DateTimePickerDialog.newInstance(positiveButtonConfiguration, negativeButtonConfiguration, dateFormat, cancelableOnClickOutside, animation);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener);
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener);
        dialogFragment.setOnSelectionChangedListener(dateSetListener);
        dialogFragment.setSelection(initialDate);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        return dialogFragment;
    }
}
