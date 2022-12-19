package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.java.commons.string.StringUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class DateRangePickerDialog : BasePickerDialogFragment<Array<OffsetDateTime?>?>(DateRangeSelection(null, null)) {
    private var textFrom: String? = null
    private var textTo: String? = null
    private var dateFormat = "MMM dd, yyyy"
    private var textViewFromValue: TextView? = null
    private var textViewToValue: TextView? = null
    private var calendar: MaterialCalendarView? = null
    private var offsetFrom = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())
    private var offsetTo = ZoneOffset.of(DateUtilsOffsetDate.getLocalTimeZone())
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState, dialogArguments)
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            textFrom = dialogArguments.getString(DATE_RANGE_FROM_TEXT_TAG)
            textTo = dialogArguments.getString(DATE_RANGE_TO_TEXT_TAG)
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat)
            if (hasSelection()) {
                selection.setCurrentSelection(
                    arrayOf<OffsetDateTime>(
                        selection.currentSelection.get(0),
                        selection.currentSelection.get(1)
                    )
                )
            } else {
                val from = DateUtilsOffsetDate.getDateFromString(
                    DateUtilsOffsetDate.defaultFormatWithTimeZone, dialogArguments.getString(
                        SELECTION_FROM_TAG
                    )
                )
                val to = DateUtilsOffsetDate.getDateFromString(
                    DateUtilsOffsetDate.defaultFormatWithTimeZone, dialogArguments.getString(
                        SELECTION_TO_TAG
                    )
                )
                selection.setCurrentSelection(arrayOf<OffsetDateTime>(from, to))
            }
        } else {
            offsetFrom = ZoneOffset.of(savedInstanceState.getString(DATE_FROM_ZONE_TAG))
            offsetTo = ZoneOffset.of(savedInstanceState.getString(DATE_TO_ZONE_TAG))
            textFrom = savedInstanceState.getString(DATE_RANGE_FROM_TEXT_TAG)
            textTo = savedInstanceState.getString(DATE_RANGE_TO_TEXT_TAG)
            dateFormat = StringUtils.getOrDefault(savedInstanceState.getString(DATE_FORMAT_TAG), dateFormat)
            val selectionFrom = DateUtilsOffsetDate.getDateFromString(
                DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(
                    SELECTION_FROM_TAG
                )
            )
            val selectionTo = DateUtilsOffsetDate.getDateFromString(
                DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(
                    SELECTION_TO_TAG
                )
            )
            val draftFrom = DateUtilsOffsetDate.getDateFromString(
                DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(
                    DRAFT_FROM_TAG
                )
            )
            val draftTo = DateUtilsOffsetDate.getDateFromString(
                DateUtilsOffsetDate.defaultFormatWithTimeZone, savedInstanceState.getString(
                    DRAFT_TO_TAG
                )
            )
            selection.setCurrentSelection(arrayOf<OffsetDateTime>(selectionFrom, selectionTo), false)
            selection.setDraftSelection(arrayOf<OffsetDateTime>(draftFrom, draftTo), false)
        }
    }

    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        outState!!.putString(DATE_FROM_ZONE_TAG, offsetFrom.toString())
        outState.putString(DATE_TO_ZONE_TAG, offsetTo.toString())
        val selectionFrom = selection.currentSelection.get(0)
        val selectionTo = selection.currentSelection.get(1)
        val draftFrom = selection.draftSelection.get(0)
        val draftTo = selection.draftSelection.get(1)
        if (selectionFrom != null) outState.putString(
            SELECTION_FROM_TAG,
            DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, selectionFrom)
        )
        if (selectionTo != null) outState.putString(
            SELECTION_TO_TAG,
            DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, selectionTo)
        )
        if (draftFrom != null) outState.putString(
            DRAFT_FROM_TAG,
            DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, draftFrom)
        )
        if (draftTo != null) outState.putString(
            DRAFT_TO_TAG,
            DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, draftTo)
        )
        outState.putString(DATE_RANGE_FROM_TEXT_TAG, textFrom)
        outState.putString(DATE_RANGE_TO_TEXT_TAG, textTo)
    }

    override fun setDialogLayout(inflater: LayoutInflater?): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            View.inflate(
                context,
                R.layout.dialog_picker_daterange_vertical,
                null
            )
        } else {
            View.inflate(
                context,
                R.layout.dialog_picker_daterange_horizontal,
                null
            )
        }
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {
        val textViewFrom = view!!.findViewById<TextView>(R.id.rangePickerFromText)
        val textViewTo = view.findViewById<TextView>(R.id.rangePickerToText)
        textViewFromValue = view.findViewById(R.id.rangePickerFromValue)
        textViewToValue = view.findViewById(R.id.rangePickerToValue)
        textViewFrom.text = textFrom
        textViewTo.text = textTo
        calendar = view.findViewById(R.id.rangeCalendarView)
        calendar.getLeftArrow().setTint(ResourceUtils.getColorByAttribute(calendar.getContext(), R.attr.colorAccent))
        calendar.getRightArrow().setTint(ResourceUtils.getColorByAttribute(calendar.getContext(), R.attr.colorAccent))
        calendar.setOnRangeSelectedListener(OnRangeSelectedListener { widget: MaterialCalendarView?, dates: List<CalendarDay> ->
            if (dates.size < 2) return@setOnRangeSelectedListener
            val first = dates[0]
            val last = dates[dates.size - 1]
            val newFrom = DateUtilsOffsetDate.date(first.year, first.month, first.day, 0, 0, 0, offsetFrom)
            val newTo = DateUtilsOffsetDate.date(last.year, last.month, last.day, 23, 59, 59, offsetTo)
            if (isDialogShown) selection.setDraftSelection(
                arrayOf<OffsetDateTime>(
                    newFrom,
                    newTo
                )
            ) else selection.setCurrentSelection(arrayOf<OffsetDateTime>(newFrom, newTo))
        })
        synchronizeSelectUi()
    }

    override fun synchronizeSelectUi() {
        val newFrom =
            if (selection.hasDraftSelection()) selection.draftSelection.get(0) else selection.currentSelection.get(0)
        val newTo = if (selection.hasDraftSelection()) selection.draftSelection.get(1) else selection.currentSelection.get(1)
        if (calendar != null) {
            if (newFrom == null && newTo == null) calendar!!.post { calendar!!.clearSelection() } else if (newFrom != null && newTo != null) {
                calendar!!.post {
                    calendar!!.selectRange(dateToCalendarDay(newFrom), dateToCalendarDay(newTo))
                    calendar!!.setCurrentDate(dateToCalendarDay(newTo), false)
                }
            }
        }
        if (textViewFromValue != null && textViewToValue != null) {
            val ctx = textViewFromValue!!.context
            var dateStringFrom = DateUtilsOffsetDate.getDateString(dateFormat, newFrom, Locale.getDefault())
            var dateStringTo = DateUtilsOffsetDate.getDateString(dateFormat, newTo, Locale.getDefault())
            if (dateStringFrom == null || dateStringFrom == "") dateStringFrom =
                ResourceUtils.getPhrase(ctx, R.string.dialog_date_picker_empty_text)
            if (dateStringTo == null || dateStringTo == "") dateStringTo =
                ResourceUtils.getPhrase(ctx, R.string.dialog_date_picker_empty_text)
            textViewFromValue!!.text = dateStringFrom
            textViewToValue!!.text = dateStringTo
        }
    }

    fun setSelection(newFrom: OffsetDateTime?, newTo: OffsetDateTime?) {
        var newFrom = newFrom
        var newTo = newTo
        val preparedDates = prepareRangeForSet(newFrom, newTo)
        newFrom = preparedDates[0]
        newTo = preparedDates[1]
        val isValid = newFrom != null && newTo != null || newFrom == null && newTo == null
        if (!isValid) return
        selection.currentSelection = arrayOf(newFrom, newTo)
        if (newFrom != null) offsetFrom = newFrom.offset
        if (newTo != null) offsetTo = newTo.offset
    }

    override fun setSelection(range: Array<OffsetDateTime?>?) {
        var range = range
        if (range == null) range = arrayOf(null, null)
        setSelection(range[0], range[1])
    }

    private fun prepareRangeForSet(newFrom: OffsetDateTime?, newTo: OffsetDateTime?): Array<OffsetDateTime?> {
        var newFrom = newFrom
        var newTo = newTo
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
            positive: DialogButtonConfiguration?, negative: DialogButtonConfiguration?, dateFormat: String?,
            textFrom: String?, textTo: String?, cancelable: Boolean, animationType: DialogAnimationTypes?
        ): DateRangePickerDialog {
            val dialogFragment = DateRangePickerDialog()
            val bundleHelper = DialogBundleHelper()
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
            bundleHelper.bundle.putString(DATE_RANGE_FROM_TEXT_TAG, textFrom)
            bundleHelper.bundle.putString(DATE_RANGE_TO_TEXT_TAG, textTo)
            bundleHelper.bundle.putString(DATE_FORMAT_TAG, dateFormat)
            dialogFragment.arguments = bundleHelper.bundle
            return dialogFragment
        }
    }
}