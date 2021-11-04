package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;

public class MonthPickerDialogBuilder extends LightBulbDialogBuilder<MonthPickerDialog> {

    private MonthPickerDialog.SelectionChangedListener<int[]> monthSetListener;
    private ArrayList<int[]> disabledMonths;
    private ArrayList<int[]> enabledMonths;
    private String dateFormat;
    private int[] initialSelection;
    private int minYear = 1970;
    private int maxYear = 2100;

    public MonthPickerDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    @Override
    public MonthPickerDialogBuilder withTitle(String title) {
        return (MonthPickerDialogBuilder) super.withTitle(title);
    }

    @Override
    public MonthPickerDialogBuilder withMessage(String message) {
        return (MonthPickerDialogBuilder) super.withMessage(message);
    }

    @Override
    public MonthPickerDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (MonthPickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public MonthPickerDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (MonthPickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public MonthPickerDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (MonthPickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public MonthPickerDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (MonthPickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public MonthPickerDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (MonthPickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public MonthPickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (MonthPickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public MonthPickerDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (MonthPickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public MonthPickerDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (MonthPickerDialogBuilder) super.withAnimations(animation);
    }

    public MonthPickerDialogBuilder withDisabledMonths(ArrayList<int[]> disabledMonths) {
        this.disabledMonths = disabledMonths;
        return this;
    }

    public MonthPickerDialogBuilder withEnabledMonths(ArrayList<int[]> enabledMonths) {
        this.enabledMonths = enabledMonths;
        return this;
    }

    public MonthPickerDialogBuilder withOnDateSelectedEvent(MonthPickerDialog.SelectionChangedListener<int[]> listener) {
        this.monthSetListener = listener;
        return this;
    }

    public MonthPickerDialogBuilder withSelection(int year, int month) {
        this.initialSelection = new int[]{year, month};
        return this;
    }

    public MonthPickerDialogBuilder withMinYear(int year) {
        this.minYear = year;
        return this;
    }

    public MonthPickerDialogBuilder withMaxYear(int year) {
        this.maxYear = year;
        return this;
    }

    public MonthPickerDialogBuilder withDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    @Override
    public MonthPickerDialog buildDialog() {
        MonthPickerDialog dialogFragment = (MonthPickerDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = MonthPickerDialog.newInstance(positiveButtonConfiguration, negativeButtonConfiguration, dateFormat, cancelableOnClickOutside, animation);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener);
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        dialogFragment.setOnSelectionChangedListener(monthSetListener);
        dialogFragment.setDisabledMonths(disabledMonths);
        dialogFragment.setEnabledMonths(enabledMonths);
        dialogFragment.setCalendarBounds(minYear, maxYear);
        if (initialSelection != null)
            dialogFragment.setSelection(initialSelection[0], initialSelection[1]);
        return dialogFragment;
    }
}
