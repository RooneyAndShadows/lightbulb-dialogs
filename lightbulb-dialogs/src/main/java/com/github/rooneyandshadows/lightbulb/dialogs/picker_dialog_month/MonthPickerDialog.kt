package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.data.MonthEntry
import java.time.OffsetDateTime
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class MonthPickerDialog : BasePickerDialogFragment<MonthEntry?>(MonthSelection(null, null)) {
    private var minYear = 1970
    private var maxYear = 2100
    private val disabledMonths: MutableList<MonthEntry> = mutableListOf()
    private val enabledMonths: MutableList<MonthEntry> = mutableListOf()
    private var monthCalendar: MonthCalendarView? = null
    private lateinit var pickerHeadingSelectionTextView: TextView
    var dialogDateFormat = "MMMM YYYY"
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}

    companion object {
        private const val DATE_FORMAT_TAG = "DATE_FORMAT_TEXT"
        private const val MONTH_SELECTION_TAG = "MONTH_PICKER_SELECTION_TAG"
        private const val MONTH_SELECTION_DRAFT_TAG = "DATE_PICKER_SELECTION_DRAFT_TAG"
        private const val PICKER_MIN_YEAR = "MONTH_PICKER_MIN_YEAR"
        private const val PICKER_MAX_YEAR = "MONTH_PICKER_MAX_YEAR"
        private const val PICKER_DISABLED_MONTHS = "PICKER_DISABLED_MONTHS"
        private const val PICKER_ENABLED_MONTHS = "PICKER_ENABLED_MONTHS"
        private const val MONTH_CALENDAR_SHOWN_YEAR = "MONTH_PICKER_SHOWN_YEAR"
        fun newInstance(): MonthPickerDialog {
            return MonthPickerDialog()
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        if (hasSelection()) selection.setCurrentSelection(selection.getCurrentSelection())
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            putInt(PICKER_MIN_YEAR, minYear)
            putInt(PICKER_MAX_YEAR, maxYear)
            selection.getCurrentSelection()?.apply {
                BundleUtils.putParcelable(MONTH_SELECTION_TAG, outState, this)
            }
            selection.getDraftSelection()?.apply {
                BundleUtils.putParcelable(MONTH_SELECTION_DRAFT_TAG, outState, this)
            }
            enabledMonths.apply {
                BundleUtils.putParcelableArrayList(PICKER_ENABLED_MONTHS, outState, this as ArrayList<MonthEntry>)
            }
            disabledMonths.apply {
                BundleUtils.putParcelableArrayList(PICKER_DISABLED_MONTHS, outState, this as ArrayList<MonthEntry>)
            }
            putInt(MONTH_CALENDAR_SHOWN_YEAR, monthCalendar!!.currentShownYear)
            putString(DATE_FORMAT_TAG, dialogDateFormat)
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        BundleUtils.getParcelable(MONTH_SELECTION_TAG, savedState, MonthEntry::class.java)?.apply {
            selection.setCurrentSelection(this, false)
        }
        BundleUtils.getParcelable(MONTH_SELECTION_DRAFT_TAG, savedState, MonthEntry::class.java)?.apply {
            selection.setDraftSelection(this, false)
        }
        BundleUtils.getParcelableArrayList(PICKER_ENABLED_MONTHS, savedState, MonthEntry::class.java)?.apply {
            enabledMonths.addAll(this)
        }
        BundleUtils.getParcelableArrayList(PICKER_DISABLED_MONTHS, savedState, MonthEntry::class.java)?.apply {
            disabledMonths.addAll(this)
        }
        minYear = savedState.getInt(PICKER_MIN_YEAR)
        maxYear = savedState.getInt(PICKER_MAX_YEAR)
        dialogDateFormat = savedState.getString(DATE_FORMAT_TAG, dialogDateFormat)
    }

    private fun getMonthArrayFromDateString(dateString: String): IntArray {
        val monthAsDate = getDateFromString(dateString)
        val year = DateUtilsOffsetDate.extractYearFromDate(monthAsDate)
        val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(monthAsDate)
        return intArrayOf(year, month)
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        val orientation = resources.configuration.orientation
        val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
        val layoutId = if (isPortrait) R.layout.dialog_picker_month_vertical else R.layout.dialog_picker_month_horizontal
        return layoutInflater.inflate(layoutId, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
        selectViews(view)
        val monthCalendar = this.monthCalendar!!
        monthCalendar.setCalendarBounds(minYear, maxYear)
        disabledMonths.apply { monthCalendar.setDisabledMonths(ArrayList(this)) }
        enabledMonths.apply { monthCalendar.setEnabledMonths(ArrayList(this)) }
        monthCalendar.addSelectioChangeListener { newSelection: IntArray? ->
            if (isDialogShown) selection.setDraftSelection(newSelection)
            else selection.setCurrentSelection(newSelection)
        }
        synchronizeSelectUi()
    }

    @Override
    override fun synchronizeSelectUi() {
        val newValue = if (selection.hasDraftSelection()) selection.getDraftSelection()
        else selection.getCurrentSelection()
        if (newValue == null) monthCalendar?.clearSelection()
        else monthCalendar?.setSelectedMonthAndScrollToYear(newValue.year, newValue.month)
        val date = if (newValue == null) null else DateUtilsOffsetDate.date(newValue[0], newValue[1])
        val ctx = pickerHeadingSelectionTextView.context
        var dateString = DateUtilsOffsetDate.getDateString(dialogDateFormat, date, Locale.getDefault())
        if (dateString.isNullOrBlank()) dateString = ResourceUtils.getPhrase(ctx, R.string.dialog_month_picker_empty_text)
        pickerHeadingSelectionTextView.text = dateString
    }

    @Override
    override fun setSelection(newSelection: MonthEntry?) {
        val selection = validateSelectionInput(newSelection)
        this.selection.setCurrentSelection(selection)
    }

    fun clearSelection() {
        selection.setCurrentSelection(null)
    }

    fun getSelectionAsArray(): IntArray? {
        return selection.getCurrentSelection()
    }

    fun setSelection(year: Int, month: Int) {
        val selection = validateSelectionInput(year, month)
        this.selection.setCurrentSelection(selection)
    }

    fun setCalendarBounds(min: Int, max: Int) {
        if (min > max) {
            maxYear = min
            minYear = max
        } else {
            minYear = min
            maxYear = max
        }
        val targetDate = if (isDialogShown) getMonthAsDate(selection.getDraftSelection())
        else getMonthAsDate(selection.getCurrentSelection())
        if (targetDate != null && !DateUtilsOffsetDate.isDateInRange(
                targetDate,
                DateUtilsOffsetDate.date(minYear, 1),
                DateUtilsOffsetDate.date(maxYear, 12)
            )
        ) setSelection(null)
        monthCalendar?.setCalendarBounds(minYear, maxYear)
    }

    fun setDisabledMonths(disabled: ArrayList<IntArray>?) {
        disabledMonths = disabled
        if (disabledMonths != null)
            for (disabledMonth in disabled!!)
                if (Arrays.equals(disabledMonth, getSelectionAsArray()))
                    setSelection(null)
        monthCalendar?.setDisabledMonths(disabled)
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
                if (Arrays.equals(month, getSelectionAsArray())) clearCurrentSelection = false
                if (currentYear < minYear) minYear = currentYear
                if (currentYear > maxYear) maxYear = currentYear
            }
            if (clearCurrentSelection) setSelection(null)
        }
        enabledMonths?.apply { monthCalendar?.setEnabledMonths(ArrayList(this)) }
    }

    private fun getDateString(year: Int, month: Int): String? {
        return getDateString(DateUtilsOffsetDate.date(year, month))
    }

    private fun getDateFromString(dateString: String?): OffsetDateTime? {
        return DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dateString)
    }

    private fun getDateString(date: OffsetDateTime?): String? {
        return DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, date)
    }

    private fun selectViews(view: View) {
        pickerHeadingSelectionTextView = view.findViewById(R.id.dateSelectionValue)
        monthCalendar = view.findViewById(R.id.dialogMonthPicker)
    }

    private fun validateSelectionInput(selection: IntArray?): IntArray? {
        return if (selection == null) null else validateSelectionInput(selection[0], selection[1])
    }

    private fun validateSelectionInput(year: Int, month: Int): IntArray {
        var validatedYear = year
        var validatedMonth = month
        if (validatedYear < minYear) validatedYear = minYear
        if (validatedYear > maxYear) validatedYear = maxYear
        if (validatedMonth < 1) validatedMonth = 1
        if (validatedMonth > 12) validatedMonth = 12
        return intArrayOf(validatedYear, validatedMonth)
    }

    private fun getMonthAsDate(month: IntArray?): OffsetDateTime? {
        return if (month == null) null else DateUtilsOffsetDate.date(month[0], month[1])
    }
}