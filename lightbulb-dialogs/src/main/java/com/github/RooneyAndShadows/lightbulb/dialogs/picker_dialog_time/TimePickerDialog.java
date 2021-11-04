package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.github.rooneyandshadows.java.commons.date.DateUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbPickerDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import java.util.Date;

@SuppressWarnings({"unused", "UnusedAssignment"})
public class TimePickerDialog extends LightBulbPickerDialogFragment<int[]> {
    private static final String TIME_SELECTION_TAG = "TIME_SELECTION_TAG";
    private static final String TIME_SELECTION_DRAFT_TAG = "TIME_SELECTION_DRAFT_TAG";
    private TimePicker picker;
    private boolean ignorePickerEvent;

    public static TimePickerDialog newInstance(
            DialogButtonConfiguration positive, DialogButtonConfiguration negative, boolean cancelable, DialogAnimationTypes animationType) {
        TimePickerDialog f = new TimePickerDialog();
        DialogBundleHelper bundleHelper = new DialogBundleHelper()
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType);
        f.setArguments(bundleHelper.getBundle());
        return f;
    }

    public TimePickerDialog() {
        super(new TimeSelection(new int[]{DateUtils.getHourOfDay(DateUtils.now()), DateUtils.getMinuteOfHour(DateUtils.now())}, null), false);
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (dialogArguments == null) {
                throw new IllegalArgumentException("Bundle args required");
            }
            if (hasSelection())
                selection.setCurrentSelection(selection.getCurrentSelection());
            else
                selection.setCurrentSelection(dialogArguments.getIntArray(TIME_SELECTION_TAG));
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(TIME_SELECTION_TAG), false);
            selection.setDraftSelection(savedInstanceState.getIntArray(TIME_SELECTION_DRAFT_TAG), false);
        }
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        if (selection.getCurrentSelection() != null)
            outState.putIntArray(TIME_SELECTION_TAG, selection.getCurrentSelection());
        if (selection.getDraftSelection() != null)
            outState.putIntArray(TIME_SELECTION_DRAFT_TAG, selection.getDraftSelection());
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        return View.inflate(getContext(), R.layout.dialog_picker_time_picker, null);
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
        picker = view.findViewById(R.id.dialogTimePicker);
        picker.setIs24HourView(true);
        picker.setSaveEnabled(false);
        synchronizeSelectUi();
        picker.setOnTimeChangedListener((timePicker, hourOfDay, minutesOfHour) -> {
            int[] newSelection = new int[]{hourOfDay, minutesOfHour};
            if (isDialogShown())
                selection.setDraftSelection(newSelection);
            else
                selection.setCurrentSelection(newSelection);
        });
    }

    public void setSelection(int hour, int minutes) {
        int newHour = validateHour(hour);
        int newMinute = validateMinute(minutes);
        selection.setCurrentSelection(new int[]{newHour, newMinute});
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void synchronizeSelectUi() {
        int[] newDate = selection.hasDraftSelection() ? selection.getDraftSelection() : selection.getCurrentSelection();
        if (newDate != null) {
            if (picker == null)
                return;
            int hour = newDate[0];
            int minutes = newDate[1];
            int apiLevel = Build.VERSION.SDK_INT;
            if (apiLevel < 23) {
                picker.setCurrentHour(hour);
                picker.setCurrentMinute(minutes);
            } else {
                picker.setHour(hour);
                picker.setMinute(minutes);
            }
        }
    }

    public void setSelection(int[] newSelection) {
        if (newSelection == null)
            selection.setCurrentSelection(null);
        else
            setSelection(newSelection[0], newSelection[1]);
    }

    public void setSelectionFromDate(Date newSelection) {
        if (newSelection == null) setSelection(null);
        else
            setSelection(DateUtils.extractYearFromDate(newSelection), DateUtils.extractMonthOfYearFromDate(newSelection));
    }

    private int validateHour(int hour) {
        int minutes = selection.getCurrentSelection()[1];
        if (hour >= 24) {
            hour = 23;
            minutes = 59;
        }
        if (hour < 0) {
            hour = 0;
        }
        return hour;
    }

    private int validateMinute(int minutes) {
        int hour = selection.getCurrentSelection()[0];
        if (minutes == 60) {
            validateHour(hour + 1);
            minutes = 0;
        }
        if (minutes < 0)
            minutes = 0;
        return minutes;
    }


    private int[] getTimeFromDate(Date date) {
        return date == null ? null : new int[]{DateUtils.getHourOfDay(date), DateUtils.getMinuteOfHour(date)};
    }
}