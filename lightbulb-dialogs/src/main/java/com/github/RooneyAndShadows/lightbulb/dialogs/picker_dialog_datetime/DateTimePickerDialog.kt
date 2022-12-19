package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.widget.AppCompatTextView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import androidx.appcompat.widget.AppCompatImageButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.java.commons.string.StringUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class DateTimePickerDialog : BasePickerDialogFragment<OffsetDateTime?>(DateTimeSelection(null, null)) {
    private var dateFormat = "dd MMM HH:mm, yyyy"
    private var showingTimePicker = false
    private var calendarView: MaterialCalendarView? = null
    private var timePickerView: TimePicker? = null
    private var selectionTextValue: AppCompatTextView? = null
    private var modeChangeButton: AppCompatImageButton? = null
    private var zoneOffset = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat)
            showingTimePicker = false
            if (hasSelection()) {
                selection.setCurrentSelection(selection.currentSelection)
            } else {
                val selectedDateFromArguments = DateUtilsOffsetDate.getDateFromString(
                    DateUtilsOffsetDate.defaultFormatWithTimeZone, dialogArguments.getString(
                        DATE_SELECTION_TAG
                    )
                )
                selection.setCurrentSelection(selectedDateFromArguments)
            }
        } else {
            showingTimePicker = savedInstanceState.getBoolean(SHOWING_TIME_PICKER_TAG)
            selection.setCurrentSelection(
                DateUtilsOffsetDate.getDateFromString(
                    DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(
                        DATE_SELECTION_TAG
                    )
                ), false
            )
            selection.setDraftSelection(
                DateUtilsOffsetDate.getDateFromString(
                    DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(
                        DATE_SELECTION_DRAFT_TAG
                    )
                ), false
            )
            zoneOffset = ZoneOffset.of(savedInstanceState.getString(DATE_OFFSET_TAG))
        }
    }

    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        if (selection.currentSelection != null) outState!!.putString(
            DATE_SELECTION_TAG,
            DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, selection.currentSelection)
        )
        if (selection.draftSelection != null) outState!!.putString(
            DATE_SELECTION_DRAFT_TAG,
            DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, selection.draftSelection)
        )
        outState!!.putString(DATE_OFFSET_TAG, zoneOffset.toString())
        outState.putBoolean(SHOWING_TIME_PICKER_TAG, showingTimePicker)
    }

    override fun setDialogLayout(inflater: LayoutInflater?): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) View.inflate(
            context,
            R.layout.dialog_picker_datetime_vertical,
            null
        ) else View.inflate(
            context,
            R.layout.dialog_picker_datetime_horizontal,
            null
        )
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {
        selectionTextValue = view!!.findViewById(R.id.dateSelectionValue)
        timePickerView = view.findViewById(R.id.timePickerView)
        modeChangeButton = view.findViewById(R.id.datePickerModeChangeButton)
        calendarView = view.findViewById(R.id.dateCalendarView)
        val ctx = modeChangeButton.getContext()
        modeChangeButton.setBackgroundDrawable(
            ResourceUtils.getDrawable(
                ctx,
                R.drawable.background_round_corners_transparent
            )
        )
        modeChangeButton.setOnClickListener(View.OnClickListener { v: View? ->
            showingTimePicker = !showingTimePicker
            syncPickerMode()
        })
        modeChangeButton.setVisibility(if (selection.draftSelection != null) View.VISIBLE else View.GONE)
        calendarView.getLeftArrow().setTint(ResourceUtils.getColorByAttribute(ctx, R.attr.colorAccent))
        calendarView.getRightArrow().setTint(ResourceUtils.getColorByAttribute(ctx, R.attr.colorAccent))
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget: MaterialCalendarView?, date: CalendarDay, selected: Boolean ->
            if (isDialogShown) {
                val draftDate = selection.draftSelection
                var hour = 0
                var minute = 0
                val second = 0
                if (draftDate != null) {
                    hour = DateUtilsOffsetDate.getHourOfDay(draftDate)
                    minute = DateUtilsOffsetDate.getMinuteOfHour(draftDate)
                }
                selection.setDraftSelection(
                    DateUtilsOffsetDate.date(
                        date.year,
                        date.month,
                        date.day,
                        hour,
                        minute,
                        second,
                        zoneOffset
                    )
                )
            } else {
                val currentDate = selection.currentSelection
                var hour = 0
                var minute = 0
                val second = 0
                if (currentDate != null) {
                    hour = DateUtilsOffsetDate.getHourOfDay(currentDate)
                    minute = DateUtilsOffsetDate.getMinuteOfHour(currentDate)
                }
                selection.setCurrentSelection(
                    DateUtilsOffsetDate.date(
                        date.year,
                        date.month,
                        date.day,
                        hour,
                        minute,
                        second,
                        zoneOffset
                    )
                )
            }
        })
        timePickerView.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view1: TimePicker?, hourOfDay: Int, minute: Int ->
            val date = DateUtilsOffsetDate.setTimeToDate(selection.draftSelection, hourOfDay, minute, 0)
            if (isDialogShown) selection.setDraftSelection(date) else selection.setCurrentSelection(date)
        })
        syncPickerMode()
        synchronizeSelectUi()
    }

    override fun synchronizeSelectUi() {
        val newDate = if (selection.hasDraftSelection()) selection.draftSelection else selection.currentSelection
        if (newDate != null) {
            if (calendarView != null) {
                modeChangeButton!!.visibility = View.VISIBLE
                val selectedDate = dateToCalendarDay(newDate)
                calendarView!!.clearSelection()
                calendarView!!.setDateSelected(selectedDate, true)
                calendarView!!.setCurrentDate(selectedDate, false)
                timePickerView!!.hour = DateUtilsOffsetDate.getHourOfDay(newDate)
                timePickerView!!.minute = DateUtilsOffsetDate.getMinuteOfHour(newDate)
            }
        } else {
            if (calendarView != null) {
                modeChangeButton!!.visibility = View.GONE
                calendarView!!.clearSelection()
            }
        }
        updateHeader(newDate)
    }

    override fun setSelection(newSelection: OffsetDateTime?) {
        super.setSelection(newSelection)
        if (newSelection != null) zoneOffset = newSelection.offset
    }

    private fun updateHeader(newDate: OffsetDateTime?) {
        if (selectionTextValue != null) {
            val ctx = selectionTextValue!!.context
            var dateString = DateUtilsOffsetDate.getDateString(dateFormat, newDate, Locale.getDefault())
            if (dateString == null || dateString == "") dateString =
                ResourceUtils.getPhrase(ctx, R.string.dialog_month_picker_empty_text)
            selectionTextValue!!.text = dateString
        }
    }

    private fun dateToCalendarDay(date: OffsetDateTime?): CalendarDay? {
        if (date == null) return null
        val year = DateUtilsOffsetDate.extractYearFromDate(date)
        val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
        val day = DateUtilsOffsetDate.extractDayOfMonthFromDate(date)
        return CalendarDay.from(year, month, day)
    }

    private fun syncPickerMode() {
        val modeIcon = ResourceUtils.getDrawable(
            modeChangeButton!!.context,
            if (showingTimePicker) R.drawable.calendar_icon else R.drawable.time_icon
        )
        modeIcon!!.setTint(ResourceUtils.getColorByAttribute(modeChangeButton!!.context, R.attr.colorOnPrimary))
        modeChangeButton!!.setImageDrawable(modeIcon)
        if (showingTimePicker) {
            timePickerView!!.visibility = View.VISIBLE
            calendarView!!.visibility = View.GONE
        } else {
            timePickerView!!.visibility = View.GONE
            calendarView!!.visibility = View.VISIBLE
        }
        measureDialogLayout()
    }

    companion object {
        private const val SHOWING_TIME_PICKER_TAG = "SHOWING_TIME_PICKER_TAG"
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TEXT"
        private const val DATE_SELECTION_TAG = "DATE_PICKER_SELECTION_TAG"
        private const val DATE_SELECTION_DRAFT_TAG = "DATE_PICKER_SELECTION_DRAFT_TAG"
        private const val DATE_OFFSET_TAG = "DATE_OFFSET_TAG"
        fun newInstance(
            positive: DialogButtonConfiguration?, negative: DialogButtonConfiguration?,
            dateFormat: String?, cancelable: Boolean, animationType: DialogAnimationTypes?
        ): DateTimePickerDialog {
            val dialogFragment = DateTimePickerDialog()
            val bundleHelper = DialogBundleHelper()
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
            bundleHelper.bundle.putString(DATE_FORMAT_TAG, dateFormat)
            dialogFragment.arguments = bundleHelper.bundle
            return dialogFragment
        }
    }
}