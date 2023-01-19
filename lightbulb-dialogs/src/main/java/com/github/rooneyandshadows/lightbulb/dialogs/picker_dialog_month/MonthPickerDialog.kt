package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialog.*
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.max
import kotlin.math.min

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class MonthPickerDialog : BasePickerDialogFragment<Month?>(MonthSelection(null, null)) {
    private var minYear = DEFAULT_YEAR_MIN
    private var maxYear = DEFAULT_YEAR_MAX
    private val disabledMonths: MutableList<Month> = mutableListOf()
    private val enabledMonths: MutableList<Month> = mutableListOf()
    private var monthCalendar: MonthCalendarView? = null
    private lateinit var pickerHeadingSelectionTextView: TextView
    var dialogDateFormat = DEFAULT_DATE_FORMAT
    val selectionAsArray: IntArray?
        get() = selectionAsMonth?.toArray()
    val selectionAsDate: OffsetDateTime?
        get() = selectionAsMonth?.toDate()
    val selectionAsMonth: Month?
        get() = selection.getCurrentSelection()
    override var dialogType: DialogTypes
        get() = NORMAL
        set(value) {}

    companion object {
        private const val DEFAULT_YEAR_MIN = 1970
        private const val DEFAULT_YEAR_MAX = 2100
        private const val DEFAULT_DATE_FORMAT = "MMMM YYYY"
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
                BundleUtils.putParcelableArrayList(PICKER_ENABLED_MONTHS, outState, this as ArrayList<Month>)
            }
            disabledMonths.apply {
                BundleUtils.putParcelableArrayList(PICKER_DISABLED_MONTHS, outState, this as ArrayList<Month>)
            }
            putInt(MONTH_CALENDAR_SHOWN_YEAR, monthCalendar!!.currentShownYear)
            putString(DATE_FORMAT_TAG, dialogDateFormat)
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        BundleUtils.getParcelable(MONTH_SELECTION_TAG, savedState, Month::class.java)?.apply {
            selection.setCurrentSelection(this, false)
        }
        BundleUtils.getParcelable(MONTH_SELECTION_DRAFT_TAG, savedState, Month::class.java)?.apply {
            selection.setDraftSelection(this, false)
        }
        BundleUtils.getParcelableArrayList(PICKER_ENABLED_MONTHS, savedState, Month::class.java)?.apply {
            enabledMonths.addAll(this)
        }
        BundleUtils.getParcelableArrayList(PICKER_DISABLED_MONTHS, savedState, Month::class.java)?.apply {
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

        disabledMonths.apply {
            val entries = monthsToMonthEntries(this)
            monthCalendar.setDisabledMonths(entries)
        }
        enabledMonths.apply {
            val entries = monthsToMonthEntries(this)
            monthCalendar.setEnabledMonths(entries)
        }
        monthCalendar.addSelectionChangeListener(object : MonthCalendarView.SelectionChangeListener {
            @Override
            override fun onSelectionChanged(
                monthCalendarView: MonthCalendarView,
                newSelection: MonthEntry?,
                oldSelection: MonthEntry?
            ) {
                val selectedMonthEntry = newSelection?.let { monthEntry -> return@let monthEntrytoMonth(monthEntry) }
                if (isDialogShown) selection.setDraftSelection(selectedMonthEntry)
                else selection.setCurrentSelection(selectedMonthEntry)
            }
        })
        synchronizeSelectUi()
    }

    @Override
    override fun synchronizeSelectUi() {
        val ctx = requireContext()
        val newValue = if (selection.hasDraftSelection()) selection.getDraftSelection()
        else selection.getCurrentSelection()
        if (newValue == null) monthCalendar?.clearSelection()
        else monthCalendar?.setSelectedMonthAndScrollToYear(monthToMonthEntry(newValue))
        pickerHeadingSelectionTextView.apply {
            text = newValue?.getMonthString(dialogDateFormat) ?: ResourceUtils.getPhrase(
                ctx,
                R.string.dialog_month_picker_empty_text
            )
        }
    }

    @Override
    override fun setSelection(newSelection: Month?) {
        validateSelectionInput(newSelection).apply {
            selection.setCurrentSelection(this)
        }
    }

    fun setSelection(year: Int, month: Int) {
        setSelection(Month(year, month))
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


    fun setDisabledMonths(disabled: List<Month>) {
        disabledMonths.apply {
            clear()
            addAll(disabled)
            val currentSelection = selection.getCurrentSelection()
            if (any { it.compare(currentSelection) })
                clearSelection()
        }
        val monthEntries = monthsToMonthEntries(disabledMonths)
        monthCalendar?.setDisabledMonths(monthEntries)
    }

    fun setEnabledMonths(enabled: List<Month>) {
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
            val monthEntries = monthsToMonthEntries(enabledMonths)
            monthCalendar?.setEnabledMonths(monthEntries)
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

    private fun validateSelectionInput(month: Month?): Month? {
        month ?: return null
        return month.getWithinYearBounds(minYear, maxYear)
    }

    private fun monthEntrytoMonth(month: MonthEntry): Month {
        return Month(month.year, month.month)
    }

    private fun monthToMonthEntry(month: Month): MonthEntry {
        return MonthEntry(month.year, month.month)
    }

    private fun monthsToMonthEntries(months: List<Month>): List<MonthEntry> {
        return mutableListOf<MonthEntry>().apply {
            months.forEach { month ->
                add(monthToMonthEntry(month))
            }
        }
    }

    class Month(val year: Int, val month: Int) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt()
        )

        val monthName: String
            get() = DateUtilsOffsetDate.getDateString("MMM", toDate())

        fun compare(year: Int, month: Int): Boolean {
            return year == this.year && month == this.month
        }

        fun compare(target: Month?): Boolean {
            if (target == null) return false
            return year == target.year && month == target.month
        }

        fun toArray(): IntArray {
            return intArrayOf(year, month)
        }

        fun toDate(): OffsetDateTime {
            return DateUtilsOffsetDate.date(year, month)
        }

        fun getWithinYearBounds(minYear: Int, maxYear: Int): Month {
            return CREATOR.getWithinYearBounds(year, month, minYear, maxYear)
        }

        fun getMonthString(format: String = "MMMM YYYY"): String {
            return DateUtilsOffsetDate.getDateString(format, toDate())
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(year)
            parcel.writeInt(month)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Month> {
            override fun createFromParcel(parcel: Parcel): Month {
                return Month(parcel)
            }

            override fun newArray(size: Int): Array<Month?> {
                return arrayOfNulls(size)
            }

            @JvmStatic
            fun fromDate(date: OffsetDateTime): Month {
                val year = DateUtilsOffsetDate.extractYearFromDate(date)
                val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
                return Month(year, month)
            }

            @JvmStatic
            fun getWithinYearBounds(targetYear: Int, targetMonth: Int, minYear: Int, maxYear: Int): Month {
                val year = min(maxYear, max(minYear, targetYear))
                val month = min(12, max(1, targetMonth))
                return Month(year, month)
            }

            @JvmStatic
            fun fromDateString(dateString: String): Month {
                val date = DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dateString)
                val year = DateUtilsOffsetDate.extractYearFromDate(date)
                val month = DateUtilsOffsetDate.extractMonthOfYearFromDate(date)
                return Month(year, month)
            }
        }
    }
}