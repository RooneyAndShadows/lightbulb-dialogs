package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogCallbacks;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder;

import java.time.OffsetDateTime;
import java.util.Date;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

public class DateRangePickerDialogBuilder extends BaseDialogBuilder<DateRangePickerDialog> {

    private BasePickerDialogFragment.SelectionChangedListener<OffsetDateTime[]> dateSetListener;
    private String textFrom;
    private String textTo;
    private String dateFormat;
    private OffsetDateTime[] initialRange;

    public DateRangePickerDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    public DateRangePickerDialogBuilder(LifecycleOwner lifecycleOwner, FragmentManager manager, String dialogTag) {
        super(lifecycleOwner, manager, dialogTag);
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
    public DateRangePickerDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (DateRangePickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public DateRangePickerDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (DateRangePickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public DateRangePickerDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (DateRangePickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public DateRangePickerDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (DateRangePickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public DateRangePickerDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (DateRangePickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public DateRangePickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (DateRangePickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public DateRangePickerDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (DateRangePickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public DateRangePickerDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (DateRangePickerDialogBuilder) super.withAnimations(animation);
    }

    @Override
    public DateRangePickerDialogBuilder withDialogCallbacks(DialogCallbacks callbacks) {
        return (DateRangePickerDialogBuilder) super.withDialogCallbacks(callbacks);
    }

    public DateRangePickerDialogBuilder withTextFrom(String textFrom) {
        this.textFrom = textFrom;
        return this;
    }

    public DateRangePickerDialogBuilder withTextTo(String textTo) {
        this.textTo = textTo;
        return this;
    }

    public DateRangePickerDialogBuilder withOnDateSelectedEvent(BasePickerDialogFragment.SelectionChangedListener<OffsetDateTime[]> listener) {
        this.dateSetListener = listener;
        return this;
    }

    public DateRangePickerDialogBuilder withSelection(OffsetDateTime[] range) {
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
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner);
        dialogFragment.setDialogCallbacks(dialogCallbacks);
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
