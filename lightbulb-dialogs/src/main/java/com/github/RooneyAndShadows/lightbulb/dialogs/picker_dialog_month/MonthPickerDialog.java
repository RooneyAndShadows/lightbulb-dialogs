package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate;
import com.github.rooneyandshadows.java.commons.string.StringUtils;
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment;
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MonthPickerDialog extends BasePickerDialogFragment<int[]> {
    private static final String DATE_FORMAT_TAG = "DATE_FORMAT_TEXT";
    private static final String MONTH_SELECTION_TAG = "MONTH_PICKER_SELECTION_TAG";
    private static final String MONTH_SELECTION_DRAFT_TAG = "DATE_PICKER_SELECTION_DRAFT_TAG";
    private static final String PICKER_MIN_YEAR = "MONTH_PICKER_MIN_YEAR";
    private static final String PICKER_MAX_YEAR = "MONTH_PICKER_MAX_YEAR";
    private static final String PICKER_DISABLED_MONTHS = "PICKER_DISABLED_MONTHS";
    private static final String PICKER_ENABLED_MONTHS = "PICKER_ENABLED_MONTHS";
    private static final String MONTH_CALENDAR_SHOWN_YEAR = "MONTH_PICKER_SHOWN_YEAR";
    private MonthCalendarView monthCalendar;
    private String dateFormat = "MMMM YYYY";
    private int minYear = 1970;
    private int maxYear = 2100;
    private ArrayList<int[]> disabledMonths;
    private ArrayList<int[]> enabledMonths;
    private TextView pickerHeadingSelectionTextView;

    public static MonthPickerDialog newInstance(
            DialogButtonConfiguration positive, DialogButtonConfiguration negative, String dateFormat,
            boolean cancelable, DialogAnimationTypes animationType) {
        MonthPickerDialog dialogFragment = new MonthPickerDialog();
        DialogBundleHelper bundleHelper = new DialogBundleHelper()
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType);
        bundleHelper.getBundle().putString(DATE_FORMAT_TAG, dateFormat);
        dialogFragment.setArguments(bundleHelper.getBundle());
        return dialogFragment;
    }

    public MonthPickerDialog() {
        super(new MonthSelection(null, null));
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (dialogArguments == null) {
                throw new IllegalArgumentException("Bundle args required");
            }
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat);
            if (hasSelection())
                selection.setCurrentSelection(selection.getCurrentSelection());
            else
                selection.setCurrentSelection(dialogArguments.getIntArray(MONTH_SELECTION_TAG));
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(MONTH_SELECTION_TAG), false);
            selection.setDraftSelection(savedInstanceState.getIntArray(MONTH_SELECTION_DRAFT_TAG), false);
            minYear = savedInstanceState.getInt(PICKER_MIN_YEAR);
            maxYear = savedInstanceState.getInt(PICKER_MAX_YEAR);
            ArrayList<String> previouslyEnabled = savedInstanceState.getStringArrayList(PICKER_ENABLED_MONTHS);
            ArrayList<String> previouslyDisabled = savedInstanceState.getStringArrayList(PICKER_DISABLED_MONTHS);
            if (previouslyEnabled == null && previouslyDisabled == null)
                setCalendarBounds(minYear, minYear);
            if (previouslyDisabled != null) {
                ArrayList<int[]> previouslyDisabledMonths = new ArrayList<>();
                for (String disabledMonth : previouslyDisabled) {
                    OffsetDateTime monthAsDate = DateUtilsOffsetDate.getDateFromStringInDefaultFormat(disabledMonth);
                    int year = DateUtilsOffsetDate.extractYearFromDate(monthAsDate);
                    int month = DateUtilsOffsetDate.extractMonthOfYearFromDate(monthAsDate);
                    previouslyDisabledMonths.add(new int[]{year, month});
                }
                setDisabledMonths(previouslyDisabledMonths);
            }
            if (previouslyEnabled != null) {
                ArrayList<int[]> previouslyEnabledMonths = new ArrayList<>();
                for (String enabledMonth : previouslyEnabled) {
                    OffsetDateTime monthAsDate = DateUtilsOffsetDate.getDateFromStringInDefaultFormat(enabledMonth);
                    int year = DateUtilsOffsetDate.extractYearFromDate(monthAsDate);
                    int month = DateUtilsOffsetDate.extractMonthOfYearFromDate(monthAsDate);
                    previouslyEnabledMonths.add(new int[]{year, month});
                }
                setEnabledMonths(previouslyEnabledMonths);
            }
        }
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putInt(PICKER_MIN_YEAR, minYear);
        outState.putInt(PICKER_MAX_YEAR, maxYear);
        if (selection.getCurrentSelection() != null)
            outState.putIntArray(MONTH_SELECTION_TAG, selection.getCurrentSelection());
        if (selection.getDraftSelection() != null)
            outState.putIntArray(MONTH_SELECTION_DRAFT_TAG, selection.getDraftSelection());
        if (this.enabledMonths != null) {
            ArrayList<String> enabledMonths = new ArrayList<>();
            for (int[] enabledMonth : this.enabledMonths)
                enabledMonths.add(DateUtilsOffsetDate.getDateStringInDefaultFormat(DateUtilsOffsetDate.date(enabledMonth[0], enabledMonth[1])));
            outState.putStringArrayList(PICKER_ENABLED_MONTHS, enabledMonths);
        }
        if (this.disabledMonths != null) {
            ArrayList<String> disabledMonths = new ArrayList<>();
            for (int[] disabledMonth : this.disabledMonths)
                disabledMonths.add(DateUtilsOffsetDate.getDateStringInDefaultFormat(DateUtilsOffsetDate.date(disabledMonth[0], disabledMonth[1])));
            outState.putStringArrayList(PICKER_DISABLED_MONTHS, disabledMonths);
        }
        if (monthCalendar != null)
            outState.putInt(MONTH_CALENDAR_SHOWN_YEAR, monthCalendar.getCurrentShownYear());
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return View.inflate(getContext(), R.layout.dialog_picker_month_vertical, null);
        } else {
            return View.inflate(getContext(), R.layout.dialog_picker_month_horizontal, null);
        }
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
        pickerHeadingSelectionTextView = view.findViewById(R.id.dateSelectionValue);
        monthCalendar = view.findViewById(R.id.dialogMonthPicker);
        monthCalendar.setCalendarBounds(minYear, maxYear);
        monthCalendar.setDisabledMonths(disabledMonths);
        monthCalendar.setEnabledMonths(enabledMonths);
        monthCalendar.addSelectioChangeListener(newSelection -> {
            if (isDialogShown()) selection.setDraftSelection(newSelection);
            else selection.setCurrentSelection(newSelection);
        });
        synchronizeSelectUi();
    }

    @Override
    protected void synchronizeSelectUi() {
        int[] newValue = selection.hasDraftSelection() ? selection.getDraftSelection() : selection.getCurrentSelection();
        if (monthCalendar != null) {
            if (newValue == null)
                monthCalendar.clearSelection();
            else {
                monthCalendar.setSelectedMonthAndScrollToYear(newValue[0], newValue[1]);
            }
        }
        if (pickerHeadingSelectionTextView != null) {
            OffsetDateTime date = newValue == null ? null : DateUtilsOffsetDate.date(newValue[0], newValue[1]);
            Context ctx = pickerHeadingSelectionTextView.getContext();
            String dateString = DateUtilsOffsetDate.getDateString(dateFormat, date, Locale.getDefault());
            if (dateString == null || dateString.equals(""))
                dateString = ResourceUtils.getPhrase(ctx, R.string.dialog_month_picker_empty_text);
            pickerHeadingSelectionTextView.setText(dateString);
        }
    }

    public void clearSelection() {
        selection.setCurrentSelection(null);
    }

    public void setSelection(int[] newSelection) {
        int[] processedInput = validateSelectionInput(newSelection);
        selection.setCurrentSelection(processedInput);
    }

    public void setSelection(int year, int month) {
        int[] processedInput = validateSelectionInput(year, month);
        selection.setCurrentSelection(processedInput);
    }

    public void setCalendarBounds(int min, int max) {
        if (min > max) {
            maxYear = min;
            minYear = max;
        } else {
            minYear = min;
            maxYear = max;
        }
        OffsetDateTime targetDate = isDialogShown() ? getMonthAsDate(selection.getDraftSelection()) : getMonthAsDate(selection.getCurrentSelection());
        if (targetDate != null && !DateUtilsOffsetDate.isDateInRange(targetDate, DateUtilsOffsetDate.date(minYear, 1), DateUtilsOffsetDate.date(maxYear, 12)))
            setSelection(null);
        if (monthCalendar != null)
            monthCalendar.setCalendarBounds(minYear, maxYear);
    }

    public void setDisabledMonths(ArrayList<int[]> disabled) {
        disabledMonths = disabled;
        if (disabledMonths != null)
            for (int[] disabledMonth : disabled)
                if (Arrays.equals(disabledMonth, getSelectionAsArray()))
                    setSelection(null);
        if (monthCalendar != null)
            monthCalendar.setDisabledMonths(disabled);
    }

    public void setEnabledMonths(ArrayList<int[]> enabled) {
        enabledMonths = enabled;
        if (enabledMonths != null) {
            minYear = DateUtilsOffsetDate.extractYearFromDate(DateUtilsOffsetDate.nowLocal());
            maxYear = minYear;
            if (enabledMonths.size() > 0) {
                minYear = enabled.get(0)[0];
                maxYear = enabled.get(0)[0];
            }
            boolean clearCurrentSelection = true;
            for (int[] month : enabledMonths) {
                int currentYear = month[0];
                if (Arrays.equals(month, getSelectionAsArray()))
                    clearCurrentSelection = false;
                if (currentYear < minYear)
                    minYear = currentYear;
                if (currentYear > maxYear)
                    maxYear = currentYear;
            }
            if (clearCurrentSelection)
                setSelection(null);
        }
        if (monthCalendar != null)
            monthCalendar.setEnabledMonths(enabledMonths);
    }

    public int[] getSelectionAsArray() {
        return selection.getCurrentSelection();
    }

    private int[] validateSelectionInput(int[] selection) {
        if (selection == null)
            return null;
        return validateSelectionInput(selection[0], selection[1]);
    }

    private int[] validateSelectionInput(int year, int month) {
        if (year < minYear)
            year = minYear;
        if (year > maxYear)
            year = maxYear;
        if (month < 1)
            month = 1;
        if (month > 12)
            month = 12;
        return new int[]{year, month};
    }

    public OffsetDateTime getMonthAsDate(int[] month) {
        return month == null ? null : DateUtilsOffsetDate.date(month[0], month[1]);
    }
}