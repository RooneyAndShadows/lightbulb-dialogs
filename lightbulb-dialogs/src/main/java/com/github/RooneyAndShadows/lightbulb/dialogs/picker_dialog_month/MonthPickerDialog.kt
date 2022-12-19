package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.java.commons.string.StringUtils
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView.SelectionChangeListener
import com.github.rooneyandshadows.lightbulb.dialogs.R
import java.time.OffsetDateTime
import java.util.*

class MonthPickerDialog : BasePickerDialogFragment<IntArray?>(MonthSelection(null, null)) {
    private var monthCalendar: MonthCalendarView? = null
    private var dateFormat = "MMMM YYYY"
    private var minYear = 1970
    private var maxYear = 2100
    private var disabledMonths: ArrayList<IntArray>? = null
    private var enabledMonths: ArrayList<IntArray>? = null
    private var pickerHeadingSelectionTextView: TextView? = null
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            dateFormat = StringUtils.getOrDefault(dialogArguments.getString(DATE_FORMAT_TAG), dateFormat)
            if (hasSelection()) selection.setCurrentSelection(selection.currentSelection) else selection.setCurrentSelection(
                dialogArguments.getIntArray(
                    MONTH_SELECTION_TAG
                )
            )
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(MONTH_SELECTION_TAG), false)
            selection.setDraftSelection(savedInstanceState.getIntArray(MONTH_SELECTION_DRAFT_TAG), false)
            minYear = savedInstanceState.getInt(PICKER_MIN_YEAR)
            maxYear = savedInstanceState.getInt(PICKER_MAX_YEAR)
            val previouslyEnabled = savedInstanceState.getStringArrayList(PICKER_ENABLED_MONTHS)
            val previouslyDisabled = savedInstanceState.getStringArrayList(PICKER_DISABLED_MONTHS)
            if (previouslyEnabled == null && previouslyDisabled == null) setCalendarBounds(minYear, minYear)
            if (previouslyDisabled != null) {
                val previouslyDisabledMonths = ArrayList<IntArray>()
                for (disabledMonth in previouslyDisabled) {
                    val monthAsDate = DateUtilsOffsetDate.getDateFromStringInDefaultFormat(disabledMonth)
                    val year = DateUtilsOffsetDate.extractYearFromDate(monthAsDate)
                    val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(monthAsDate)
                    previouslyDisabledMonths.add(intArrayOf(year, month))
                }
                setDisabledMonths(previouslyDisabledMonths)
            }
            if (previouslyEnabled != null) {
                val previouslyEnabledMonths = ArrayList<IntArray>()
                for (enabledMonth in previouslyEnabled) {
                    val monthAsDate = DateUtilsOffsetDate.getDateFromStringInDefaultFormat(enabledMonth)
                    val year = DateUtilsOffsetDate.extractYearFromDate(monthAsDate)
                    val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(monthAsDate)
                    previouslyEnabledMonths.add(intArrayOf(year, month))
                }
                setEnabledMonths(previouslyEnabledMonths)
            }
        }
    }

    override fun doOnSaveInstanceState(outState: Bundle?) {
        outState!!.putInt(PICKER_MIN_YEAR, minYear)
        outState.putInt(PICKER_MAX_YEAR, maxYear)
        if (selection.currentSelection != null) outState.putIntArray(MONTH_SELECTION_TAG, selection.currentSelection)
        if (selection.draftSelection != null) outState.putIntArray(MONTH_SELECTION_DRAFT_TAG, selection.draftSelection)
        if (enabledMonths != null) {
            val enabledMonths = ArrayList<String>()
            for (enabledMonth in this.enabledMonths!!) enabledMonths.add(
                DateUtilsOffsetDate.getDateStringInDefaultFormat(
                    DateUtilsOffsetDate.date(
                        enabledMonth[0], enabledMonth[1]
                    )
                )
            )
            outState.putStringArrayList(PICKER_ENABLED_MONTHS, enabledMonths)
        }
        if (disabledMonths != null) {
            val disabledMonths = ArrayList<String>()
            for (disabledMonth in this.disabledMonths!!) disabledMonths.add(
                DateUtilsOffsetDate.getDateStringInDefaultFormat(
                    DateUtilsOffsetDate.date(
                        disabledMonth[0], disabledMonth[1]
                    )
                )
            )
            outState.putStringArrayList(PICKER_DISABLED_MONTHS, disabledMonths)
        }
        if (monthCalendar != null) outState.putInt(MONTH_CALENDAR_SHOWN_YEAR, monthCalendar!!.currentShownYear)
    }

    override fun setDialogLayout(inflater: LayoutInflater?): View {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            View.inflate(
                context,
                R.layout.dialog_picker_month_vertical,
                null
            )
        } else {
            View.inflate(
                context,
                R.layout.dialog_picker_month_horizontal,
                null
            )
        }
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {
        pickerHeadingSelectionTextView = view!!.findViewById(R.id.dateSelectionValue)
        monthCalendar = view.findViewById(R.id.dialogMonthPicker)
        monthCalendar.setCalendarBounds(minYear, maxYear)
        monthCalendar.setDisabledMonths(disabledMonths)
        monthCalendar.setEnabledMonths(enabledMonths)
        monthCalendar.addSelectioChangeListener(SelectionChangeListener { newSelection: IntArray? ->
            if (isDialogShown) selection.setDraftSelection(
                newSelection
            ) else selection.setCurrentSelection(newSelection)
        })
        synchronizeSelectUi()
    }

    override fun synchronizeSelectUi() {
        val newValue = if (selection.hasDraftSelection()) selection.draftSelection else selection.currentSelection
        if (monthCalendar != null) {
            if (newValue == null) monthCalendar!!.clearSelection() else {
                monthCalendar!!.setSelectedMonthAndScrollToYear(newValue[0], newValue[1])
            }
        }
        if (pickerHeadingSelectionTextView != null) {
            val date = if (newValue == null) null else DateUtilsOffsetDate.date(newValue[0], newValue[1])
            val ctx = pickerHeadingSelectionTextView!!.context
            var dateString = DateUtilsOffsetDate.getDateString(dateFormat, date, Locale.getDefault())
            if (dateString == null || dateString == "") dateString =
                ResourceUtils.getPhrase(ctx, R.string.dialog_month_picker_empty_text)
            pickerHeadingSelectionTextView!!.text = dateString
        }
    }

    fun clearSelection() {
        selection.currentSelection = null
    }

    override fun setSelection(newSelection: IntArray?) {
        val processedInput = validateSelectionInput(newSelection)
        selection.currentSelection = processedInput
    }

    fun setSelection(year: Int, month: Int) {
        val processedInput = validateSelectionInput(year, month)
        selection.currentSelection = processedInput
    }

    fun setCalendarBounds(min: Int, max: Int) {
        if (min > max) {
            maxYear = min
            minYear = max
        } else {
            minYear = min
            maxYear = max
        }
        val targetDate =
            if (isDialogShown) getMonthAsDate(selection.draftSelection) else getMonthAsDate(selection.currentSelection)
        if (targetDate != null && !DateUtilsOffsetDate.isDateInRange(
                targetDate,
                DateUtilsOffsetDate.date(minYear, 1),
                DateUtilsOffsetDate.date(maxYear, 12)
            )
        ) setSelection(null)
        if (monthCalendar != null) monthCalendar!!.setCalendarBounds(minYear, maxYear)
    }

    fun setDisabledMonths(disabled: ArrayList<IntArray>?) {
        disabledMonths = disabled
        if (disabledMonths != null) for (disabledMonth in disabled!!) if (Arrays.equals(
                disabledMonth,
                selectionAsArray
            )
        ) setSelection(null)
        if (monthCalendar != null) monthCalendar!!.setDisabledMonths(disabled)
    }

    fun setEnabledMonths(enabled: ArrayList<IntArray>?) {
        enabledMonths = enabled
        if (enabledMonths != null) {
            minYear = DateUtilsOffsetDate.extractYearFromDate(DateUtilsOffsetDate.nowLocal())
            maxYear = minYear
            if (enabledMonths!!.size > 0) {
                minYear = enabled!![0][0]
                maxYear = enabled[0][0]
            }
            var clearCurrentSelection = true
            for (month in enabledMonths!!) {
                val currentYear = month[0]
                if (Arrays.equals(month, selectionAsArray)) clearCurrentSelection = false
                if (currentYear < minYear) minYear = currentYear
                if (currentYear > maxYear) maxYear = currentYear
            }
            if (clearCurrentSelection) setSelection(null)
        }
        if (monthCalendar != null) monthCalendar!!.setEnabledMonths(enabledMonths)
    }

    val selectionAsArray: IntArray?
        get() = selection.currentSelection

    private fun validateSelectionInput(selection: IntArray?): IntArray? {
        return if (selection == null) null else validateSelectionInput(selection[0], selection[1])
    }

    private fun validateSelectionInput(year: Int, month: Int): IntArray {
        var year = year
        var month = month
        if (year < minYear) year = minYear
        if (year > maxYear) year = maxYear
        if (month < 1) month = 1
        if (month > 12) month = 12
        return intArrayOf(year, month)
    }

    fun getMonthAsDate(month: IntArray?): OffsetDateTime? {
        return if (month == null) null else DateUtilsOffsetDate.date(month[0], month[1])
    }

    companion object {
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TEXT"
        private const val MONTH_SELECTION_TAG = "MONTH_PICKER_SELECTION_TAG"
        private const val MONTH_SELECTION_DRAFT_TAG = "DATE_PICKER_SELECTION_DRAFT_TAG"
        private const val PICKER_MIN_YEAR = "MONTH_PICKER_MIN_YEAR"
        private const val PICKER_MAX_YEAR = "MONTH_PICKER_MAX_YEAR"
        private const val PICKER_DISABLED_MONTHS = "PICKER_DISABLED_MONTHS"
        private const val PICKER_ENABLED_MONTHS = "PICKER_ENABLED_MONTHS"
        private const val MONTH_CALENDAR_SHOWN_YEAR = "MONTH_PICKER_SHOWN_YEAR"
        fun newInstance(
            positive: DialogButtonConfiguration?, negative: DialogButtonConfiguration?, dateFormat: String?,
            cancelable: Boolean, animationType: DialogAnimationTypes?
        ): MonthPickerDialog {
            val dialogFragment = MonthPickerDialog()
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