package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Suppress("UNUSED_PARAMETER")
class DateTimePickerDialog : BasePickerDialogFragment<OffsetDateTime>(DateTimeSelection(null, null)) {
    private var showingTimePicker = false
    private var calendarView: MaterialCalendarView? = null
    private var timePickerView: TimePicker? = null
    private var selectionTextValue: AppCompatTextView? = null
    private var modeChangeButton: AppCompatImageButton? = null
    private var zoneOffset = DEFAULT_OFFSET
    var dialogDateFormat = DEFAULT_DATE_FORMAT
        private set
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}

    companion object {
        private val DEFAULT_OFFSET = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())
        private const val DEFAULT_DATE_FORMAT = "dd MMM HH:mm, yyyy"
        private const val SHOWING_TIME_PICKER_TAG = "SHOWING_TIME_PICKER_TAG"
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TEXT"
        private const val DATE_OFFSET_TAG = "DATE_OFFSET_TAG"
        fun newInstance(): DateTimePickerDialog {
            return DateTimePickerDialog()
        }
    }

    @Override
    override fun getRegularConstraints(): RegularDialogConstraints {
        return RegularDialogConstraintsBuilder(this)
            .default()
            .withMaxHeightInPercents(90)
            .build()
    }

    @Override
    override fun doOnSaveDialogProperties(outState: Bundle) {
        super.doOnSaveDialogProperties(outState)
        outState.apply {
            putString(DATE_OFFSET_TAG, zoneOffset.toString())
            putBoolean(SHOWING_TIME_PICKER_TAG, showingTimePicker)
            putString(DATE_FORMAT_TAG, dialogDateFormat)
        }
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        savedState.apply {
            showingTimePicker = getBoolean(SHOWING_TIME_PICKER_TAG)
            dialogDateFormat = getString(DATE_FORMAT_TAG, dialogDateFormat)
            zoneOffset = ZoneOffset.of(getString(DATE_OFFSET_TAG))
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutInflater.inflate(R.layout.dialog_picker_datetime_vertical, null)
        else layoutInflater.inflate(R.layout.dialog_picker_datetime_horizontal, null)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        val activeSelection = dialogSelection.getActiveSelection()
        selectViews(view)
        setupHeader(activeSelection)
        val context = requireContext()
        modeChangeButton?.apply {
            background = ResourceUtils.getDrawable(context, R.drawable.dialogs_bg_round_corners_transparent_ripple)
            setOnClickListener {
                showingTimePicker = !showingTimePicker
                syncPickerMode()
            }
        }
        calendarView?.apply {
            leftArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
            rightArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
            if (activeSelection == null) clearSelection()
            else {
                val selectedDate = dateToCalendarDay(activeSelection)
                setDateSelected(selectedDate, true)
                setCurrentDate(selectedDate, false)
            }
        }
        activeSelection?.apply selectedDate@{
            timePickerView?.apply {
                hour = DateUtilsOffsetDate.getHourOfDay(this@selectedDate)
                minute = DateUtilsOffsetDate.getMinuteOfHour(this@selectedDate)
            }
        }
        syncPickerMode()
    }

    private fun checkIfCalendarNeedsSync(currentValue: OffsetDateTime?): Boolean {
        val calendarValue = calendarView!!.selectedDate?.let {
            calendarDayToDate(it, currentValue ?: DateUtilsOffsetDate.nowLocal())
        }
        if (currentValue == null && calendarValue == null) return false
        if (currentValue == null || calendarValue == null) return true
        return !DateUtilsOffsetDate.isDateEqual(currentValue, calendarValue)
    }

    @Override
    override fun onSelectionChange(newSelection: OffsetDateTime?) {
        setupHeader(newSelection)
        if (!checkIfCalendarNeedsSync(newSelection)) return
        calendarView?.apply {
            if (newSelection == null) clearSelection()
            else {
                val selectedDate = dateToCalendarDay(newSelection)
                setDateSelected(selectedDate, true)
                setCurrentDate(selectedDate, false)
            }
        }
        timePickerView?.apply {
            if (newSelection == null) return@apply
            hour = DateUtilsOffsetDate.getHourOfDay(newSelection)
            minute = DateUtilsOffsetDate.getMinuteOfHour(newSelection)
        }
    }

    @Override
    override fun doOnViewStateRestored(savedInstanceState: Bundle?) {
        super.doOnViewStateRestored(savedInstanceState)
        calendarView!!.setOnDateChangedListener { _: MaterialCalendarView?, date: CalendarDay, _: Boolean ->
            if (isDialogShown) {
                val draftDate = dialogSelection.getDraftSelection()
                val hour: Int
                val minute: Int
                val second = 0
                if (draftDate != null) {
                    hour = DateUtilsOffsetDate.getHourOfDay(draftDate)
                    minute = DateUtilsOffsetDate.getMinuteOfHour(draftDate)
                } else {
                    val now = DateUtilsOffsetDate.nowLocal()
                    hour = DateUtilsOffsetDate.getHourOfDay(now)
                    minute = DateUtilsOffsetDate.getMinuteOfHour(now)
                }
                dialogSelection.setDraftSelection(
                    DateUtilsOffsetDate.date(date.year, date.month, date.day, hour, minute, second, zoneOffset)
                )
            } else {
                val currentDate = dialogSelection.getCurrentSelection()
                val hour: Int
                val minute: Int
                val second = 0
                if (currentDate != null) {
                    hour = DateUtilsOffsetDate.getHourOfDay(currentDate)
                    minute = DateUtilsOffsetDate.getMinuteOfHour(currentDate)
                } else {
                    val now = DateUtilsOffsetDate.nowLocal()
                    hour = DateUtilsOffsetDate.getHourOfDay(now)
                    minute = DateUtilsOffsetDate.getMinuteOfHour(now)
                }
                dialogSelection.setCurrentSelection(
                    DateUtilsOffsetDate.date(date.year, date.month, date.day, hour, minute, second, zoneOffset)
                )
            }
        }
        timePickerView!!.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val date = DateUtilsOffsetDate.setTimeToDate(dialogSelection.getDraftSelection(), hourOfDay, minute, 0)
            if (isDialogShown) dialogSelection.setDraftSelection(date) else dialogSelection.setCurrentSelection(date)
        }
    }

    @Override
    override fun setSelection(newSelection: OffsetDateTime?) {
        super.setSelection(newSelection)
        zoneOffset = newSelection?.offset ?: DEFAULT_OFFSET
    }

    fun setDialogDateFormat(dateFormat: String?) {
        val activeSelection = dialogSelection.getActiveSelection()
        dialogDateFormat = dateFormat ?: DEFAULT_DATE_FORMAT
        setupHeader(activeSelection)
    }

    private fun selectViews(view: View) {
        selectionTextValue = view.findViewById(R.id.dateSelectionValue)
        timePickerView = view.findViewById(R.id.timePickerView)
        modeChangeButton = view.findViewById(R.id.datePickerModeChangeButton)
        calendarView = view.findViewById(R.id.dateCalendarView)
    }

    private fun setupHeader(selection: OffsetDateTime?) {
        modeChangeButton?.apply {
            visibility = if (selection == null) View.GONE else View.VISIBLE
        }
        selectionTextValue?.apply {
            val dateString = DateUtilsOffsetDate.getDateString(dialogDateFormat, selection, Locale.getDefault())
            val default = ResourceUtils.getPhrase(requireContext(), R.string.dialog_month_picker_empty_text)
            text = if (dateString.isNullOrBlank()) default else dateString
        }
    }

    private fun dateToCalendarDay(date: OffsetDateTime?): CalendarDay? {
        if (date == null) return null
        val year = DateUtilsOffsetDate.extractYearFromDate(date)
        val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
        val day = DateUtilsOffsetDate.extractDayOfMonthFromDate(date)
        return CalendarDay.from(year, month, day)
    }

    private fun calendarDayToDate(
        calendarDay: CalendarDay?,
        currentSelection: OffsetDateTime,
    ): OffsetDateTime? {
        if (calendarDay == null) return null
        val hour = currentSelection.hour
        val minute = currentSelection.minute
        return DateUtilsOffsetDate.date(calendarDay.year, calendarDay.month, calendarDay.day, hour, minute, 0)
    }

    private fun syncPickerMode() {
        val activeSelection = dialogSelection.getActiveSelection()
        if (showingTimePicker && activeSelection == null) showingTimePicker = false
        val modeIconRes = if (showingTimePicker) R.drawable.dialogs_ic_calendar else R.drawable.dialogs_ic_time
        val modeIcon = ResourceUtils.getDrawable(modeChangeButton!!.context, modeIconRes).apply {
            this!!.setTint(ResourceUtils.getColorByAttribute(modeChangeButton!!.context, R.attr.colorOnPrimary))
        }
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
}