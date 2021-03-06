package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.github.rooneyandshadows.java.commons.date.DateUtils;
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate;
import com.github.rooneyandshadows.java.commons.string.StringUtils;
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.R;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;


public class DateTimePickerDialog extends BasePickerDialogFragment<OffsetDateTime> {
    private static final String SHOWING_TIME_PICKER_TAG = "SHOWING_TIME_PICKER_TAG";
    private static final String DATE_FORMAT_TAG = "DATE_FORMAT_TEXT";
    private static final String DATE_SELECTION_TAG = "DATE_PICKER_SELECTION_TAG";
    private static final String DATE_SELECTION_DRAFT_TAG = "DATE_PICKER_SELECTION_DRAFT_TAG";
    private static final String DATE_OFFSET_TAG = "DATE_OFFSET_TAG";
    private String dateFormat = "dd MMM HH:mm, yyyy";
    private boolean showingTimePicker;
    private MaterialCalendarView calendarView;
    private TimePicker timePickerView;
    private AppCompatTextView selectionTextValue;
    private AppCompatImageButton modeChangeButton;
    private ZoneOffset zoneOffset = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone());

    public static DateTimePickerDialog newInstance(
            DialogButtonConfiguration positive, DialogButtonConfiguration negative,
            String dateFormat, boolean cancelable, DialogAnimationTypes animationType) {
        DateTimePickerDialog dialogFragment = new DateTimePickerDialog();
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

    public DateTimePickerDialog() {
        super(new DateTimeSelection(null, null));
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (dialogArguments == null) {
                throw new IllegalArgumentException("Bundle args required");
            }
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat);
            showingTimePicker = false;
            if (hasSelection()) {
                selection.setCurrentSelection(selection.getCurrentSelection());
            } else {
                OffsetDateTime selectedDateFromArguments = DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dialogArguments.getString(DATE_SELECTION_TAG));
                selection.setCurrentSelection(selectedDateFromArguments);
            }
        } else {
            showingTimePicker = savedInstanceState.getBoolean(SHOWING_TIME_PICKER_TAG);
            selection.setCurrentSelection(DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(DATE_SELECTION_TAG)), false);
            selection.setDraftSelection(DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(DATE_SELECTION_DRAFT_TAG)), false);
            zoneOffset = ZoneOffset.of(savedInstanceState.getString(DATE_OFFSET_TAG));
        }
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        if (selection.getCurrentSelection() != null)
            outState.putString(DATE_SELECTION_TAG, DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, selection.getCurrentSelection()));
        if (selection.getDraftSelection() != null)
            outState.putString(DATE_SELECTION_DRAFT_TAG, DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, selection.getDraftSelection()));
        outState.putString(DATE_OFFSET_TAG, zoneOffset.toString());
        outState.putBoolean(SHOWING_TIME_PICKER_TAG, showingTimePicker);
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            return View.inflate(getContext(), R.layout.dialog_picker_datetime_vertical, null);
        else
            return View.inflate(getContext(), R.layout.dialog_picker_datetime_horizontal, null);
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
        selectionTextValue = view.findViewById(R.id.dateSelectionValue);
        timePickerView = view.findViewById(R.id.timePickerView);
        modeChangeButton = view.findViewById(R.id.datePickerModeChangeButton);
        calendarView = view.findViewById(R.id.dateCalendarView);
        Context ctx = modeChangeButton.getContext();
        modeChangeButton.setBackgroundDrawable(ResourceUtils.getDrawable(ctx, R.drawable.background_round_corners_transparent));
        modeChangeButton.setOnClickListener(v -> {
            showingTimePicker = !showingTimePicker;
            syncPickerMode();
        });
        modeChangeButton.setVisibility((selection.getDraftSelection() != null) ? View.VISIBLE : View.GONE);
        calendarView.getLeftArrow().setTint(ResourceUtils.getColorByAttribute(ctx, R.attr.colorAccent));
        calendarView.getRightArrow().setTint(ResourceUtils.getColorByAttribute(ctx, R.attr.colorAccent));
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (isDialogShown()) {
                OffsetDateTime draftDate = selection.getDraftSelection();
                int hour = 0;
                int minute = 0;
                int second = 0;
                if (draftDate != null) {
                    hour = DateUtilsOffsetDate.getHourOfDay(draftDate);
                    minute = DateUtilsOffsetDate.getMinuteOfHour(draftDate);
                }
                selection.setDraftSelection(DateUtilsOffsetDate.date(date.getYear(), date.getMonth(), date.getDay(), hour, minute, second, zoneOffset));
            } else {
                OffsetDateTime currentDate = selection.getCurrentSelection();
                int hour = 0;
                int minute = 0;
                int second = 0;
                if (currentDate != null) {
                    hour = DateUtilsOffsetDate.getHourOfDay(currentDate);
                    minute = DateUtilsOffsetDate.getMinuteOfHour(currentDate);
                }
                selection.setCurrentSelection(DateUtilsOffsetDate.date(date.getYear(), date.getMonth(), date.getDay(), hour, minute, second, zoneOffset));
            }
        });
        timePickerView.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            OffsetDateTime date = DateUtilsOffsetDate.setTimeToDate(selection.getDraftSelection(), hourOfDay, minute, 0);
            if (isDialogShown()) selection.setDraftSelection(date);
            else selection.setCurrentSelection(date);
        });
        syncPickerMode();
        synchronizeSelectUi();
    }

    @Override
    protected void synchronizeSelectUi() {
        OffsetDateTime newDate = selection.hasDraftSelection() ? selection.getDraftSelection() : selection.getCurrentSelection();
        if (newDate != null) {
            if (calendarView != null) {
                modeChangeButton.setVisibility(View.VISIBLE);
                CalendarDay selectedDate = dateToCalendarDay(newDate);
                calendarView.clearSelection();
                calendarView.setDateSelected(selectedDate, true);
                calendarView.setCurrentDate(selectedDate, false);
                timePickerView.setHour(DateUtilsOffsetDate.getHourOfDay(newDate));
                timePickerView.setMinute(DateUtilsOffsetDate.getMinuteOfHour(newDate));
            }
        } else {
            if (calendarView != null) {
                modeChangeButton.setVisibility(View.GONE);
                calendarView.clearSelection();
            }
        }
        updateHeader(newDate);
    }

    @Override
    public void setSelection(OffsetDateTime newSelection) {
        super.setSelection(newSelection);
        if (newSelection != null)
            zoneOffset = newSelection.getOffset();
    }

    private void updateHeader(OffsetDateTime newDate) {
        if (selectionTextValue != null) {
            Context ctx = selectionTextValue.getContext();
            String dateString = DateUtilsOffsetDate.getDateString(dateFormat, newDate, Locale.getDefault());
            if (dateString == null || dateString.equals(""))
                dateString = ResourceUtils.getPhrase(ctx, R.string.dialog_month_picker_empty_text);
            selectionTextValue.setText(dateString);
        }
    }

    private CalendarDay dateToCalendarDay(OffsetDateTime date) {
        if (date == null)
            return null;
        int year = DateUtilsOffsetDate.extractYearFromDate(date);
        int month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date);
        int day = DateUtilsOffsetDate.extractDayOfMonthFromDate(date);
        return CalendarDay.from(year, month, day);
    }

    private void syncPickerMode() {
        Drawable modeIcon = ResourceUtils.getDrawable(modeChangeButton.getContext(), showingTimePicker ? R.drawable.calendar_icon : R.drawable.time_icon);
        modeIcon.setTint(ResourceUtils.getColorByAttribute(modeChangeButton.getContext(), R.attr.colorOnPrimary));
        modeChangeButton.setImageDrawable(modeIcon);
        if (showingTimePicker) {
            timePickerView.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.GONE);
        } else {
            timePickerView.setVisibility(View.GONE);
            calendarView.setVisibility(View.VISIBLE);
        }
        measureDialogLayout();
    }
}