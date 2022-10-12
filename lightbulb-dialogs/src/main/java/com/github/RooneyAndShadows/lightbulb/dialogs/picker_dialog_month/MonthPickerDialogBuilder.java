package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogCallbacks;
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

public class MonthPickerDialogBuilder extends BaseDialogBuilder<MonthPickerDialog> {

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

    public MonthPickerDialogBuilder(LifecycleOwner lifecycleOwner, FragmentManager manager, String dialogTag) {
        super(lifecycleOwner, manager, dialogTag);
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
    public MonthPickerDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (MonthPickerDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public MonthPickerDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (MonthPickerDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public MonthPickerDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (MonthPickerDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public MonthPickerDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (MonthPickerDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public MonthPickerDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (MonthPickerDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public MonthPickerDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (MonthPickerDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public MonthPickerDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (MonthPickerDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public MonthPickerDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (MonthPickerDialogBuilder) super.withAnimations(animation);
    }

    @Override
    public MonthPickerDialogBuilder withDialogCallbacks(DialogCallbacks callbacks) {
        return (MonthPickerDialogBuilder) super.withDialogCallbacks(callbacks);
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
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner);
        dialogFragment.setDialogCallbacks(dialogCallbacks);
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
