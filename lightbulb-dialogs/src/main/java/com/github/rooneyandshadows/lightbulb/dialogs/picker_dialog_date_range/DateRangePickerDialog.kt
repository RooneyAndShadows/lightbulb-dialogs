package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.java.commons.string.StringUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog.DateRange
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER", "unused")
class DateRangePickerDialog : BasePickerDialogFragment<DateRange>(DateRangeSelection(null, null)) {
    private var textViewFrom: TextView? = null
    private var textViewTo: TextView? = null
    private var textViewFromValue: TextView? = null
    private var textViewToValue: TextView? = null
    private var calendar: MaterialCalendarView? = null
    private var offsetFrom = DEFAULT_OFFSET
    private var offsetTo = DEFAULT_OFFSET
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}
    var dialogDateFormat: String = DEFAULT_DATE_FORMAT
        private set
    var dialogTextFrom: String? = null
        private set
    var dialogTextTo: String? = null
        private set

    companion object {
        private val DEFAULT_OFFSET = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())
        private const val DEFAULT_DATE_FORMAT = "MMM dd, yyyy"
        private const val DATE_RANGE_FROM_TEXT_TAG = "DATE_RANGE_FROM_TEXT_TAG"
        private const val DATE_RANGE_TO_TEXT_TAG = "DATE_RANGE_TO_TEXT_TAG"
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TAG"
        private const val DATE_FROM_ZONE_TAG = "DATE_FROM_ZONE_TAG"
        private const val DATE_TO_ZONE_TAG = "DATE_TO_ZONE_TAG"

        fun newInstance(): DateRangePickerDialog {
            return DateRangePickerDialog()
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
        outState.putString(DATE_FROM_ZONE_TAG, offsetFrom.toString())
        outState.putString(DATE_TO_ZONE_TAG, offsetTo.toString())
        outState.putString(DATE_RANGE_FROM_TEXT_TAG, dialogTextFrom)
        outState.putString(DATE_RANGE_TO_TEXT_TAG, dialogTextTo)
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        offsetFrom = ZoneOffset.of(savedState.getString(DATE_FROM_ZONE_TAG))
        offsetTo = ZoneOffset.of(savedState.getString(DATE_TO_ZONE_TAG))
        dialogTextFrom = savedState.getString(DATE_RANGE_FROM_TEXT_TAG)
        dialogTextTo = savedState.getString(DATE_RANGE_TO_TEXT_TAG)
        dialogDateFormat = StringUtils.getOrDefault(savedState.getString(DATE_FORMAT_TAG), dialogDateFormat)
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState, dialogArguments)
        if (savedInstanceState != null) return
        val defaultFrom = ResourceUtils.getPhrase(requireContext(), R.string.dialog_range_picker_from_text)
        val defaultTo = ResourceUtils.getPhrase(requireContext(), R.string.dialog_range_picker_to_text)
        dialogTextFrom = dialogTextFrom ?: defaultFrom
        dialogTextTo = dialogTextTo ?: defaultTo
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutInflater.inflate(R.layout.dialog_picker_daterange_vertical, null)
        else layoutInflater.inflate(R.layout.dialog_picker_daterange_horizontal, null)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        val activeSelection = selection.getActiveSelection()
        selectViews(view)
        setupHeader(activeSelection)
        calendar?.apply {
            leftArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
            rightArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
            post {
                if (activeSelection == null) {
                    clearSelection()
                    return@post
                }
                val start = dateToCalendarDay(activeSelection.from)
                val end = dateToCalendarDay(activeSelection.to)
                selectRange(start, end)
                calendar?.setCurrentDate(end, false)

            }
        }
    }

    @Override
    override fun onSelectionChange(newSelection: DateRange?) {
        setupHeader(newSelection)
        val needSync = checkIfCalendarNeedsSync(newSelection)
        if (!needSync) return
        calendar?.post {
            val newFrom = dateToCalendarDay(newSelection?.from)
            val newTo = dateToCalendarDay(newSelection?.to)
            calendar?.selectRange(newFrom, newTo)
            calendar?.setCurrentDate(newTo, false)
        }
    }

    @Override
    override fun doOnViewStateRestored(savedInstanceState: Bundle?) {
        super.doOnViewStateRestored(savedInstanceState)
        calendar?.apply {
            setOnRangeSelectedListener { _: MaterialCalendarView?, dates: List<CalendarDay> ->
                if (dates.size < 2) return@setOnRangeSelectedListener
                val first = dates[0]
                val last = dates[dates.size - 1]
                val newFrom = DateUtilsOffsetDate.date(first.year, first.month, first.day, 0, 0, 0, offsetFrom)
                val newTo = DateUtilsOffsetDate.date(last.year, last.month, last.day, 23, 59, 59, offsetTo)
                val range = DateRange(newFrom, newTo)
                if (isDialogShown) selection.setDraftSelection(range)
                else selection.setCurrentSelection(range)
            }
        }
    }

    private fun setupHeader(selection: DateRange?) {
        textViewFrom?.apply {
            text = dialogTextFrom
        }
        textViewTo?.apply {
            text = dialogTextTo
        }
        textViewFromValue?.apply {
            val default = ResourceUtils.getPhrase(context, R.string.dialog_date_picker_empty_text)
            text = DateUtilsOffsetDate.getDateString(dialogDateFormat, selection?.from) ?: default
        }
        textViewToValue?.apply {
            val default = ResourceUtils.getPhrase(context, R.string.dialog_date_picker_empty_text)
            text = DateUtilsOffsetDate.getDateString(dialogDateFormat, selection?.to) ?: default
        }
    }

    private fun checkIfCalendarNeedsSync(currentValue: DateRange?): Boolean {
        val selectedDates = calendar!!.selectedDates
        selectedDates.apply {
            if (size < 2) return currentValue != null
            val calendarFrom = dateFromCalendarDay(first(), 0, 0, 0)
            val calendarTo = dateFromCalendarDay(last(), 23, 59, 59)
            return !(compareDates(currentValue?.from, calendarFrom) && compareDates(currentValue?.to, calendarTo))
        }
    }

    @Override
    override fun setSelection(newSelection: DateRange?) {
        val validatedRange = prepareRangeForSet(newSelection)
        selection.setCurrentSelection(validatedRange)
        offsetFrom = validatedRange?.from?.offset ?: DEFAULT_OFFSET
        offsetTo = validatedRange?.from?.offset ?: DEFAULT_OFFSET
    }

    fun setSelection(from: OffsetDateTime, to: OffsetDateTime) {
        val rangeToSet = DateRange(from, to)
        setSelection(rangeToSet)
    }

    fun setDialogDateFormat(format: String?) {
        val activeSelection = selection.getActiveSelection()
        dialogDateFormat = format ?: DEFAULT_DATE_FORMAT
        setupHeader(activeSelection)
    }

    fun setDialogTextFrom(textFrom: String?) {
        dialogTextFrom = textFrom
        textViewFrom?.apply {
            text = dialogTextFrom
        }
    }

    fun setDialogTextTo(textFrom: String?) {
        dialogTextTo = textFrom
        textViewTo?.apply {
            text = dialogTextTo
        }
    }

    private fun selectViews(view: View) {
        textViewFrom = view.findViewById(R.id.rangePickerFromText)
        textViewTo = view.findViewById(R.id.rangePickerToText)
        textViewFromValue = view.findViewById(R.id.rangePickerFromValue)
        textViewToValue = view.findViewById(R.id.rangePickerToValue)
        calendar = view.findViewById(R.id.rangeCalendarView)
    }

    private fun prepareRangeForSet(range: DateRange?): DateRange? {
        range ?: return null
        var from = range.from
        var to = range.to
        if (DateUtilsOffsetDate.isDateAfter(from, to)) {
            val tmp: OffsetDateTime = from
            from = to
            to = tmp
        } else if (DateUtilsOffsetDate.isDateBefore(to, from)) {
            val tmp: OffsetDateTime = from
            from = to
            to = tmp
        }
        from = DateUtilsOffsetDate.setTimeToDate(from, 0, 0, 0)
        to = DateUtilsOffsetDate.setTimeToDate(to, 23, 59, 59)
        return DateRange(from, to)
    }

    private fun dateToCalendarDay(date: OffsetDateTime?): CalendarDay? {
        if (date == null) return null
        val year = DateUtilsOffsetDate.extractYearFromDate(date)
        val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
        val day = DateUtilsOffsetDate.extractDayOfMonthFromDate(date)
        return CalendarDay.from(year, month, day)
    }

    private fun dateFromCalendarDay(calendarDay: CalendarDay, hour: Int, minutes: Int, seconds: Int): OffsetDateTime {
        return DateUtilsOffsetDate.date(
            calendarDay.year,
            calendarDay.month,
            calendarDay.day,
            hour,
            minutes,
            seconds,
            offsetFrom
        )
    }

    private fun compareDates(testDate: OffsetDateTime?, target: OffsetDateTime?): Boolean {
        return DateUtilsOffsetDate.isDateEqual(testDate, target, false)
    }

    class DateRange(val from: OffsetDateTime, val to: OffsetDateTime) : Parcelable {
        constructor(parcel: Parcel) : this(
            ParcelUtils.readOffsetDateTime(parcel)!!,
            ParcelUtils.readOffsetDateTime(parcel)!!
        )

        fun compare(from: OffsetDateTime, to: OffsetDateTime): Boolean {
            val fromEquals = DateUtilsOffsetDate.isDateEqual(this.from, from, false)
            val toEquals = DateUtilsOffsetDate.isDateEqual(this.to, to, false)
            return fromEquals && toEquals
        }

        fun compare(target: DateRange?): Boolean {
            if (target == null) return false
            return compare(target.from, target.to)
        }

        fun getFromString(format: String): String {
            return DateUtilsOffsetDate.getDateString(format, from)
        }

        fun getToString(format: String): String {
            return DateUtilsOffsetDate.getDateString(format, to)
        }

        @Override
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            ParcelUtils.writeOffsetDateTime(parcel, from)
            ParcelUtils.writeOffsetDateTime(parcel, to)
        }

        @Override
        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<DateRange> {
            override fun createFromParcel(parcel: Parcel): DateRange {
                return DateRange(parcel)
            }

            override fun newArray(size: Int): Array<DateRange?> {
                return arrayOfNulls(size)
            }
        }
    }
}