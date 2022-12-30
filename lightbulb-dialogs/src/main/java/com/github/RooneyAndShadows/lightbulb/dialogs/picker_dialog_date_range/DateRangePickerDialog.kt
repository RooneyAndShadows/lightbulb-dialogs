package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.java.commons.string.StringUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
class DateRangePickerDialog : BasePickerDialogFragment<Array<OffsetDateTime?>?>(DateRangeSelection(null, null)) {
    private var textFrom: String? = null
    private var textTo: String? = null
    private var dateFormat = "MMM dd, yyyy"
    private lateinit var textViewFrom: TextView
    private lateinit var textViewTo: TextView
    private lateinit var textViewFromValue: TextView
    private lateinit var textViewToValue: TextView
    private lateinit var calendar: MaterialCalendarView
    private var offsetFrom = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())
    private var offsetTo = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())

    companion object {
        private const val SELECTION_FROM_TAG = "DATE_RANGE_SELECTION_FROM_TAG"
        private const val SELECTION_TO_TAG = "DATE_RANGE_SELECTION_TO_TAG"
        private const val DRAFT_FROM_TAG = "DATE_RANGE_DRAFT_FROM_TAG"
        private const val DRAFT_TO_TAG = "DATE_RANGE_DRAFT_TO_TAG"
        private const val DATE_RANGE_FROM_TEXT_TAG = "DATE_RANGE_FROM_TEXT_TAG"
        private const val DATE_RANGE_TO_TEXT_TAG = "DATE_RANGE_TO_TEXT_TAG"
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TAG"
        private const val DATE_FROM_ZONE_TAG = "DATE_FROM_ZONE_TAG"
        private const val DATE_TO_ZONE_TAG = "DATE_TO_ZONE_TAG"

        fun newInstance(
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            dateFormat: String?,
            textFrom: String?,
            textTo: String?,
            cancelable: Boolean,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION,
        ): DateRangePickerDialog {
            return DateRangePickerDialog().apply {
                this.arguments = DialogBundleHelper().apply {
                    withPositiveButtonConfig(positive)
                    withNegativeButtonConfig(negative)
                    withCancelable(cancelable)
                    withShowing(false)
                    withDialogType(DialogTypes.NORMAL)
                    withAnimation(animationType)
                }.bundle.apply {
                    putString(DATE_RANGE_FROM_TEXT_TAG, textFrom)
                    putString(DATE_RANGE_TO_TEXT_TAG, textTo)
                    putString(DATE_FORMAT_TAG, dateFormat)
                }
            }
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState, dialogArguments)
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            textFrom = dialogArguments.getString(DATE_RANGE_FROM_TEXT_TAG) ?: ResourceUtils.getPhrase(requireContext(),
                R.string.dialog_range_picker_from_text)
            textTo = dialogArguments.getString(DATE_RANGE_TO_TEXT_TAG) ?: ResourceUtils.getPhrase(requireContext(),
                R.string.dialog_range_picker_to_text)
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat)
            if (hasSelection()) {
                selection.setCurrentSelection(
                    arrayOf(selection.getCurrentSelection()?.get(0), selection.getCurrentSelection()?.get(1))
                )
            } else {
                val from = getDateFromString(dialogArguments.getString(SELECTION_FROM_TAG))
                val to = getDateFromString(dialogArguments.getString(SELECTION_TO_TAG))
                selection.setCurrentSelection(arrayOf(from, to))
            }
        } else {
            offsetFrom = ZoneOffset.of(savedInstanceState.getString(DATE_FROM_ZONE_TAG))
            offsetTo = ZoneOffset.of(savedInstanceState.getString(DATE_TO_ZONE_TAG))
            textFrom = savedInstanceState.getString(DATE_RANGE_FROM_TEXT_TAG)
            textTo = savedInstanceState.getString(DATE_RANGE_TO_TEXT_TAG)
            dateFormat = StringUtils.getOrDefault(savedInstanceState.getString(DATE_FORMAT_TAG), dateFormat)
            val selectionFrom = getDateFromString(savedInstanceState.getString(SELECTION_FROM_TAG))
            val selectionTo = getDateFromString(savedInstanceState.getString(SELECTION_TO_TAG))
            val draftFrom = getDateFromString(savedInstanceState.getString(DRAFT_FROM_TAG))
            val draftTo = getDateFromString(savedInstanceState.getString(DRAFT_TO_TAG))
            selection.setCurrentSelection(arrayOf(selectionFrom, selectionTo), false)
            selection.setDraftSelection(arrayOf(draftFrom, draftTo), false)
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        outState!!.putString(DATE_FROM_ZONE_TAG, offsetFrom.toString())
        outState.putString(DATE_TO_ZONE_TAG, offsetTo.toString())
        selection.getCurrentSelection()?.get(0)?.apply {
            outState.putString(SELECTION_FROM_TAG, getDateString(this))
        }
        selection.getCurrentSelection()?.get(1)?.apply {
            outState.putString(SELECTION_TO_TAG, getDateString(this))
        }
        selection.getDraftSelection()?.get(0)?.apply {
            outState.putString(DRAFT_FROM_TAG, getDateString(this))
        }
        selection.getDraftSelection()?.get(1)?.apply {
            outState.putString(DRAFT_TO_TAG, getDateString(this))
        }
        outState.putString(DATE_RANGE_FROM_TEXT_TAG, textFrom)
        outState.putString(DATE_RANGE_TO_TEXT_TAG, textTo)
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutInflater.inflate(R.layout.dialog_picker_daterange_vertical, null)
        else layoutInflater.inflate(R.layout.dialog_picker_daterange_horizontal, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
        selectViews(view)
        val context = requireContext()
        textViewFrom.text = textFrom
        textViewTo.text = textTo
        calendar = view.findViewById(R.id.rangeCalendarView)
        calendar.leftArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
        calendar.rightArrow.setTint(ResourceUtils.getColorByAttribute(context, R.attr.colorAccent))
        calendar.setOnRangeSelectedListener { _: MaterialCalendarView?, dates: List<CalendarDay> ->
            if (dates.size < 2) return@setOnRangeSelectedListener
            val first = dates[0]
            val last = dates[dates.size - 1]
            val newFrom = DateUtilsOffsetDate.date(first.year, first.month, first.day, 0, 0, 0, offsetFrom)
            val newTo = DateUtilsOffsetDate.date(last.year, last.month, last.day, 23, 59, 59, offsetTo)
            if (isDialogShown) selection.setDraftSelection(arrayOf(newFrom, newTo))
            else selection.setCurrentSelection(arrayOf(newFrom, newTo))
        }
        synchronizeSelectUi()
    }

    @Override
    override fun synchronizeSelectUi() {
        val context = requireContext()
        val newFrom = if (selection.hasDraftSelection()) selection.getDraftSelection()?.get(0)
        else selection.getCurrentSelection()?.get(0)
        val newTo = if (selection.hasDraftSelection()) selection.getDraftSelection()?.get(1)
        else selection.getCurrentSelection()?.get(1)
        if (newFrom == null && newTo == null) calendar.post { calendar.clearSelection() }
        else if (newFrom != null && newTo != null) {
            calendar.post {
                calendar.selectRange(dateToCalendarDay(newFrom), dateToCalendarDay(newTo))
                calendar.setCurrentDate(dateToCalendarDay(newTo), false)
            }
        }
        var dateStringFrom = DateUtilsOffsetDate.getDateString(dateFormat, newFrom, Locale.getDefault())
        var dateStringTo = DateUtilsOffsetDate.getDateString(dateFormat, newTo, Locale.getDefault())
        if (dateStringFrom == null || dateStringFrom == "") dateStringFrom =
            ResourceUtils.getPhrase(context, R.string.dialog_date_picker_empty_text)
        if (dateStringTo == null || dateStringTo == "") dateStringTo =
            ResourceUtils.getPhrase(context, R.string.dialog_date_picker_empty_text)
        textViewFromValue.text = dateStringFrom
        textViewToValue.text = dateStringTo
    }

    @Override
    override fun setSelection(newSelection: Array<OffsetDateTime?>?) {
        var selection = newSelection
        if (selection == null) selection = arrayOf(null, null)
        setSelection(selection[0], selection[1])
    }

    fun setSelection(from: OffsetDateTime?, to: OffsetDateTime?) {
        var newFrom = from
        var newTo = to
        val preparedDates = prepareRangeForSet(newFrom, newTo)
        newFrom = preparedDates[0]
        newTo = preparedDates[1]
        val isValid = newFrom != null && newTo != null || newFrom == null && newTo == null
        if (!isValid) return
        selection.setCurrentSelection(arrayOf(newFrom, newTo))
        if (newFrom != null) offsetFrom = newFrom.offset
        if (newTo != null) offsetTo = newTo.offset
    }

    private fun selectViews(view: View) {
        textViewFrom = view.findViewById(R.id.rangePickerFromText)
        textViewTo = view.findViewById(R.id.rangePickerToText)
        textViewFromValue = view.findViewById(R.id.rangePickerFromValue)
        textViewToValue = view.findViewById(R.id.rangePickerToValue)
    }

    private fun getDateFromString(dateString: String?): OffsetDateTime? {
        return DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dateString)
    }

    private fun getDateString(date: OffsetDateTime?): String? {
        return DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, date)
    }

    private fun prepareRangeForSet(from: OffsetDateTime?, to: OffsetDateTime?): Array<OffsetDateTime?> {
        var newFrom = from
        var newTo = to
        if (newFrom != null && newTo != null) {
            if (DateUtilsOffsetDate.isDateAfter(newFrom, newTo)) {
                val tmp: OffsetDateTime = newFrom
                newFrom = newTo
                newTo = tmp
            } else if (DateUtilsOffsetDate.isDateBefore(newTo, newFrom)) {
                val tmp: OffsetDateTime = newFrom
                newFrom = newTo
                newTo = tmp
            }
        }
        newFrom = DateUtilsOffsetDate.setTimeToDate(newFrom, 0, 0, 0)
        newTo = DateUtilsOffsetDate.setTimeToDate(newTo, 23, 59, 59)
        return arrayOf(newFrom, newTo)
    }

    private fun dateToCalendarDay(date: OffsetDateTime?): CalendarDay? {
        if (date == null) return null
        val year = DateUtilsOffsetDate.extractYearFromDate(date)
        val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
        val day = DateUtilsOffsetDate.extractDayOfMonthFromDate(date)
        return CalendarDay.from(year, month, day)
    }
}