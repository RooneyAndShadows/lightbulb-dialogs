package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView
import com.github.rooneyandshadows.lightbulb.calendars.month.adapter.MonthEntry
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
    val selectionAsArray: IntArray?
        get() = selectionAsMonthEntry?.toArray()
    val selectionAsDate: OffsetDateTime?
        get() = selectionAsMonthEntry?.toDate()
    val selectionAsMonthEntry: MonthEntry?
        get() = selection.getCurrentSelection()

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
        validateSelectionInput(newSelection).apply {
            selection.setCurrentSelection(this)
        }
    }

    fun setSelection(year: Int, month: Int) {
        setSelection(MonthEntry(year, month))
    }

    fun clearSelection() {
        selection.setCurrentSelection(null)
    }

    fun setCalendarBounds(min: Int, max: Int) {
        if (min > max) {
            maxYear = min
            minYear = max
        } else {
            minYear = min
            maxYear = max
        }
        val minDate = DateUtilsOffsetDate.date(minYear, 1)
        val maxDate = DateUtilsOffsetDate.date(maxYear, 12)
        val currentDate = selection.getCurrentSelection()?.toDate()
        val draftDate = selection.getDraftSelection()?.toDate()
        val targetDate = if (isDialogShown) draftDate
        else currentDate
        if (targetDate != null && !DateUtilsOffsetDate.isDateInRange(targetDate, minDate, maxDate))
            setSelection(null)
        monthCalendar?.setCalendarBounds(minYear, maxYear)
    }

    fun setDisabledMonths(disabled: List<MonthEntry>) {
        disabledMonths.apply {
            clear()
            addAll(disabled)
            val currentSelection = selection.getCurrentSelection()
            if (any { it.compare(currentSelection) })
                clearSelection()
        }
        monthCalendar?.setDisabledMonths(disabled)
    }

    fun setEnabledMonths(enabled: List<MonthEntry>) {
        enabledMonths.apply {
            clear()
            addAll(enabled)
            if (isNotEmpty()) {
                minYear = first().year
                maxYear = last().year
                var clearCurrentSelection = true
                forEach { enabledMonth ->
                    val currentYear = enabledMonth.year
                    if (enabledMonth.compare(selection.getCurrentSelection()))
                        clearCurrentSelection = false
                    if (currentYear < minYear) minYear = currentYear
                    if (currentYear > maxYear) maxYear = currentYear
                }
                if (clearCurrentSelection) clearSelection()
            }
            monthCalendar?.apply { setEnabledMonths(enabledMonths) }
        }
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

    private fun validateSelectionInput(monthEntry: MonthEntry?): MonthEntry? {
        monthEntry ?: return null
        return monthEntry.getWithinYearBounds(minYear, maxYear)
    }
}