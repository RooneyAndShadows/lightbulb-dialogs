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
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class DateTimePickerDialog : BasePickerDialogFragment<OffsetDateTime?>(DateTimeSelection(null, null)) {
    private var dateFormat = "dd MMM HH:mm, yyyy"
    private var showingTimePicker = false
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var timePickerView: TimePicker
    private lateinit var selectionTextValue: AppCompatTextView
    private lateinit var modeChangeButton: AppCompatImageButton
    private var zoneOffset = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())

    companion object {
        private const val SHOWING_TIME_PICKER_TAG = "SHOWING_TIME_PICKER_TAG"
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TEXT"
        private const val DATE_SELECTION_TAG = "DATE_PICKER_SELECTION_TAG"
        private const val DATE_SELECTION_DRAFT_TAG = "DATE_PICKER_SELECTION_DRAFT_TAG"
        private const val DATE_OFFSET_TAG = "DATE_OFFSET_TAG"
        fun newInstance(
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            dateFormat: String?,
            cancelable: Boolean,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION,
        ): DateTimePickerDialog {
            return DateTimePickerDialog().apply {
                this.arguments = DialogBundleHelper().apply {
                    withPositiveButtonConfig(positive)
                    withNegativeButtonConfig(negative)
                    withCancelable(cancelable)
                    withShowing(false)
                    withDialogType(DialogTypes.NORMAL)
                    withAnimation(animationType)
                    bundle.putString(DATE_FORMAT_TAG, dateFormat)
                }.bundle
            }
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat)
            showingTimePicker = false
            if (hasSelection()) selection.setCurrentSelection(selection.getCurrentSelection())
            else {
                val dateFromArguments = getDateFromString(dialogArguments.getString(DATE_SELECTION_TAG))
                selection.setCurrentSelection(dateFromArguments)
            }
        } else {
            showingTimePicker = savedInstanceState.getBoolean(SHOWING_TIME_PICKER_TAG)
            val selectionFromState = getDateFromString(savedInstanceState.getString(DATE_SELECTION_TAG))
            val selectionDraftFromState = getDateFromString(savedInstanceState.getString(DATE_SELECTION_DRAFT_TAG))
            selection.setCurrentSelection(selectionFromState, false)
            selection.setDraftSelection(selectionDraftFromState, false)
            zoneOffset = ZoneOffset.of(savedInstanceState.getString(DATE_OFFSET_TAG))
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        selection.getCurrentSelection().apply {
            outState!!.putString(DATE_SELECTION_TAG, getDateString(this))
        }
        selection.getDraftSelection().apply {
            outState!!.putString(DATE_SELECTION_DRAFT_TAG, getDateString(this))
        }
        outState!!.putString(DATE_OFFSET_TAG, zoneOffset.toString())
        outState.putBoolean(SHOWING_TIME_PICKER_TAG, showingTimePicker)
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutInflater.inflate(R.layout.dialog_picker_datetime_vertical, null)
        else layoutInflater.inflate(R.layout.dialog_picker_datetime_horizontal, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
        selectViews(view)
        val context = requireContext()
        modeChangeButton.background = ResourceUtils.getDrawable(context, R.drawable.background_round_corners_transparent)
        modeChangeButton.setOnClickListener {
            showingTimePicker = !showingTimePicker
            syncPickerMode()
        }
        modeChangeButton.visibility = if (selection.getDraftSelection() != null) View.VISIBLE else View.GONE
        calendarView.leftArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
        calendarView.rightArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
        calendarView.setOnDateChangedListener { _: MaterialCalendarView?, date: CalendarDay, _: Boolean ->
            if (isDialogShown) {
                val draftDate = selection.getDraftSelection()
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
                val currentDate = selection.getCurrentSelection()
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
        }
        timePickerView.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val date = DateUtilsOffsetDate.setTimeToDate(selection.getDraftSelection(), hourOfDay, minute, 0)
            if (isDialogShown) selection.setDraftSelection(date) else selection.setCurrentSelection(date)
        }
        syncPickerMode()
        synchronizeSelectUi()
    }

    @Override
    override fun synchronizeSelectUi() {
        val newDate = if (selection.hasDraftSelection()) selection.getDraftSelection() else selection.getCurrentSelection()
        if (newDate != null) {
            modeChangeButton.visibility = View.VISIBLE
            val selectedDate = dateToCalendarDay(newDate)
            calendarView.clearSelection()
            calendarView.setDateSelected(selectedDate, true)
            calendarView.setCurrentDate(selectedDate, false)
            timePickerView.hour = DateUtilsOffsetDate.getHourOfDay(newDate)
            timePickerView.minute = DateUtilsOffsetDate.getMinuteOfHour(newDate)
        } else {
            modeChangeButton.visibility = View.GONE
            calendarView.clearSelection()
        }
        updateHeader(newDate)
    }

    override fun setSelection(newSelection: OffsetDateTime?) {
        super.setSelection(newSelection)
        if (newSelection != null) zoneOffset = newSelection.offset
    }

    private fun selectViews(view: View) {
        selectionTextValue = view.findViewById(R.id.dateSelectionValue)
        timePickerView = view.findViewById(R.id.timePickerView)
        modeChangeButton = view.findViewById(R.id.datePickerModeChangeButton)
        calendarView = view.findViewById(R.id.dateCalendarView)
    }

    private fun getDateFromString(dateString: String?): OffsetDateTime? {
        return DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dateString)
    }

    private fun getDateString(date: OffsetDateTime?): String? {
        return DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, date)
    }

    private fun updateHeader(newDate: OffsetDateTime?) {
        val ctx = selectionTextValue.context
        var dateString = DateUtilsOffsetDate.getDateString(dateFormat, newDate, Locale.getDefault())
        if (dateString == null || dateString == "") dateString =
            ResourceUtils.getPhrase(ctx, R.string.dialog_month_picker_empty_text)
        selectionTextValue.text = dateString
    }

    private fun dateToCalendarDay(date: OffsetDateTime?): CalendarDay? {
        if (date == null) return null
        val year = DateUtilsOffsetDate.extractYearFromDate(date)
        val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
        val day = DateUtilsOffsetDate.extractDayOfMonthFromDate(date)
        return CalendarDay.from(year, month, day)
    }

    private fun syncPickerMode() {
        val modeIconRes = if (showingTimePicker) R.drawable.calendar_icon else R.drawable.time_icon
        val modeIcon = ResourceUtils.getDrawable(modeChangeButton.context, modeIconRes).apply {
            this!!.setTint(ResourceUtils.getColorByAttribute(modeChangeButton.context, R.attr.colorOnPrimary))
        }
        modeChangeButton.setImageDrawable(modeIcon)
        if (showingTimePicker) {
            timePickerView.visibility = View.VISIBLE
            calendarView.visibility = View.GONE
        } else {
            timePickerView.visibility = View.GONE
            calendarView.visibility = View.VISIBLE
        }
        measureDialogLayout()
    }
}