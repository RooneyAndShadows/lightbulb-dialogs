package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.rooneyandshadows.java.commons.date.DateUtils;
import com.github.rooneyandshadows.java.commons.string.StringUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused")
public class DateRangePickerDialog extends LightBulbPickerDialogFragment<Date[]> {
    private static final String SELECTION_FROM_TAG = "DATE_RANGE_SELECTION_FROM_TAG";
    private static final String SELECTION_TO_TAG = "DATE_RANGE_SELECTION_TO_TAG";
    private static final String DRAFT_FROM_TAG = "DATE_RANGE_DRAFT_FROM_TAG";
    private static final String DRAFT_TO_TAG = "DATE_RANGE_DRAFT_TO_TAG";
    private static final String DATE_RANGE_FROM_TEXT_TAG = "DATE_RANGE_FROM_TEXT_TAG";
    private static final String DATE_RANGE_TO_TEXT_TAG = "DATE_RANGE_TO_TEXT_TAG";
    private static final String DATE_FORMAT_TAG = "DATE_FORMAT_TAG";
    private String textFrom;
    private String textTo;
    private String dateFormat = "MMM dd, yyyy";
    private TextView textViewFromValue;
    private TextView textViewToValue;
    private MaterialCalendarView calendar;

    public DateRangePickerDialog() {
        super(new DateRangeSelection(null, null));
    }

    public static DateRangePickerDialog newInstance(
            DialogButtonConfiguration positive, DialogButtonConfiguration negative, String dateFormat,
            String textFrom, String textTo, boolean cancelable, DialogAnimationTypes animationType) {
        DateRangePickerDialog dialogFragment = new DateRangePickerDialog();
        DialogBundleHelper bundleHelper = new DialogBundleHelper()
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType);
        bundleHelper.getBundle().putString(DATE_RANGE_FROM_TEXT_TAG, textFrom);
        bundleHelper.getBundle().putString(DATE_RANGE_TO_TEXT_TAG, textTo);
        bundleHelper.getBundle().putString(DATE_FORMAT_TAG, dateFormat);
        dialogFragment.setArguments(bundleHelper.getBundle());
        return dialogFragment;
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        super.create(savedInstanceState, dialogArguments);
        if (savedInstanceState == null) {
            if (dialogArguments == null) {
                throw new IllegalArgumentException("Bundle args required");
            }
            textFrom = dialogArguments.getString(DATE_RANGE_FROM_TEXT_TAG);
            textTo = dialogArguments.getString(DATE_RANGE_TO_TEXT_TAG);
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat);
            if (hasSelection()) {
                selection.setCurrentSelection(new Date[]{selection.getCurrentSelection()[0], selection.getCurrentSelection()[1]});
            } else {
                Date from = DateUtils.getDateFromStringInDefaultFormat(dialogArguments.getString(SELECTION_FROM_TAG));
                Date to = DateUtils.getDateFromStringInDefaultFormat(dialogArguments.getString(SELECTION_TO_TAG));
                selection.setCurrentSelection(new Date[]{from, to});
            }
        } else {
            textFrom = savedInstanceState.getString(DATE_RANGE_FROM_TEXT_TAG);
            textTo = savedInstanceState.getString(DATE_RANGE_TO_TEXT_TAG);
            dateFormat = StringUtils.getOrDefault(savedInstanceState.getString(DATE_FORMAT_TAG), dateFormat);
            Date selectionFrom = DateUtils.getDateFromStringInDefaultFormat(savedInstanceState.getString(SELECTION_FROM_TAG));
            Date selectionTo = DateUtils.getDateFromStringInDefaultFormat(savedInstanceState.getString(SELECTION_TO_TAG));
            Date draftFrom = DateUtils.getDateFromStringInDefaultFormat(savedInstanceState.getString(DRAFT_FROM_TAG));
            Date draftTo = DateUtils.getDateFromStringInDefaultFormat(savedInstanceState.getString(DRAFT_TO_TAG));
            selection.setCurrentSelection(new Date[]{selectionFrom, selectionTo}, false);
            selection.setDraftSelection(new Date[]{draftFrom, draftTo}, false);
        }
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        Date selectionFrom = selection.getCurrentSelection()[0];
        Date selectionTo = selection.getCurrentSelection()[1];
        Date draftFrom = selection.getDraftSelection()[0];
        Date draftTo = selection.getDraftSelection()[1];
        if (selectionFrom != null)
            outState.putString(SELECTION_FROM_TAG, DateUtils.getDateStringInDefaultFormat(selectionFrom));
        if (selectionTo != null)
            outState.putString(SELECTION_TO_TAG, DateUtils.getDateStringInDefaultFormat(selectionTo));
        if (draftFrom != null)
            outState.putString(DRAFT_FROM_TAG, DateUtils.getDateStringInDefaultFormat(draftFrom));
        if (draftTo != null)
            outState.putString(DRAFT_TO_TAG, DateUtils.getDateStringInDefaultFormat(draftTo));
        outState.putString(DATE_RANGE_FROM_TEXT_TAG, textFrom);
        outState.putString(DATE_RANGE_TO_TEXT_TAG, textTo);
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return View.inflate(getContext(), R.layout.dialog_picker_daterange_vertical, null);
        } else {
            return View.inflate(getContext(), R.layout.dialog_picker_daterange_horizontal, null);
        }
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
        TextView textViewFrom = view.findViewById(R.id.rangePickerFromText);
        TextView textViewTo = view.findViewById(R.id.rangePickerToText);
        textViewFromValue = view.findViewById(R.id.rangePickerFromValue);
        textViewToValue = view.findViewById(R.id.rangePickerToValue);
        textViewFrom.setText(textFrom);
        textViewTo.setText(textTo);
        calendar = view.findViewById(R.id.rangeCalendarView);
        calendar.getLeftArrow().setTint(ResourceUtils.getColorByAttribute(calendar.getContext(), R.attr.colorAccent));
        calendar.getRightArrow().setTint(ResourceUtils.getColorByAttribute(calendar.getContext(), R.attr.colorAccent));
        calendar.setOnRangeSelectedListener((widget, dates) -> {
            if (dates.size() < 2)
                return;
            CalendarDay first = dates.get(0);
            CalendarDay last = dates.get(dates.size() - 1);
            Date newFrom = DateUtils.date(first.getYear(), first.getMonth(), first.getDay(), 0, 0, 0);
            Date newTo = DateUtils.date(last.getYear(), last.getMonth(), last.getDay(), 23, 59, 59);
            if (isDialogShown())
                selection.setDraftSelection(new Date[]{newFrom, newTo});
            else
                selection.setCurrentSelection(new Date[]{newFrom, newTo});
        });
        synchronizeSelectUi();
    }

    @Override
    protected void synchronizeSelectUi() {
        Date newFrom = selection.hasDraftSelection() ? selection.getDraftSelection()[0] : selection.getCurrentSelection()[0];
        Date newTo = selection.hasDraftSelection() ? selection.getDraftSelection()[1] : selection.getCurrentSelection()[1];
        if (calendar != null) {
            if (newFrom == null && newTo == null)
                calendar.post(() -> calendar.clearSelection());
            else if (newFrom != null && newTo != null) {
                calendar.post(() -> {
                    calendar.selectRange(dateToCalendarDay(newFrom), dateToCalendarDay(newTo));
                    calendar.setCurrentDate(dateToCalendarDay(newTo), false);
                });
            }
        }
        if (textViewFromValue != null && textViewToValue != null) {
            Context ctx = textViewFromValue.getContext();
            String dateStringFrom = DateUtils.getDateString(dateFormat, newFrom, Locale.getDefault());
            String dateStringTo = DateUtils.getDateString(dateFormat, newTo, Locale.getDefault());
            if (dateStringFrom == null || dateStringFrom.equals(""))
                dateStringFrom = ResourceUtils.getPhrase(ctx, R.string.dialog_date_picker_empty_text);
            if (dateStringTo == null || dateStringTo.equals(""))
                dateStringTo = ResourceUtils.getPhrase(ctx, R.string.dialog_date_picker_empty_text);
            textViewFromValue.setText(dateStringFrom);
            textViewToValue.setText(dateStringTo);
        }
    }

    public final void setSelection(Date newFrom, Date newTo) {
        Date[] preparedDates = prepareRangeForSet(newFrom, newTo);
        newFrom = preparedDates[0];
        newTo = preparedDates[1];
        boolean isValid = ((newFrom != null && newTo != null) || (newFrom == null && newTo == null));
        if (!isValid)
            return;
        selection.setCurrentSelection(new Date[]{newFrom, newTo});
    }

    public final void setSelection(Date[] range) {
        if (range == null)
            range = new Date[]{null, null};
        setSelection(range[0], range[1]);
    }

    private Date[] prepareRangeForSet(Date newFrom, Date newTo) {
        if (newFrom != null && newTo != null) {
            if (DateUtils.isDateAfter(newFrom, newTo)) {
                Date tmp = newFrom;
                newFrom = newTo;
                newTo = tmp;
            } else if (DateUtils.isDateBefore(newTo, newFrom)) {
                Date tmp = newFrom;
                newFrom = newTo;
                newTo = tmp;
            }
        }
        newFrom = DateUtils.setTimeToDate(newFrom, 0, 0, 0);
        newTo = DateUtils.setTimeToDate(newTo, 23, 59, 59);
        return new Date[]{newFrom, newTo};
    }

    private CalendarDay dateToCalendarDay(Date date) {
        if (date == null)
            return null;
        int year = DateUtils.extractYearFromDate(date);
        int month = DateUtils.extractMonthOfYearFromDate(date);
        int day = DateUtils.extractDayOfMonthFromDate(date);
        return CalendarDay.from(year, month, day);
    }
}