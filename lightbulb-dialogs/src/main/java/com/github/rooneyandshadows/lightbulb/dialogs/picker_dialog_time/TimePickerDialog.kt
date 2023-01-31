package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialog.*
import java.time.OffsetDateTime

@Suppress("unused", "UNUSED_PARAMETER")
class TimePickerDialog : BasePickerDialogFragment<Time>(
    TimeSelection(
        Time(
            DateUtilsOffsetDate.getHourOfDay(DateUtilsOffsetDate.nowLocal()),
            DateUtilsOffsetDate.getMinuteOfHour(DateUtilsOffsetDate.nowLocal())
        ),
        null
    ), false
) {
    private lateinit var picker: TimePicker
    private val ignorePickerEvent = false
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}

    companion object {
        private const val TIME_SELECTION_TAG = "TIME_SELECTION_TAG"
        private const val TIME_SELECTION_DRAFT_TAG = "TIME_SELECTION_DRAFT_TAG"
        fun newInstance(): TimePickerDialog {
            return TimePickerDialog()
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
            selection.getCurrentSelection()?.apply {
                BundleUtils.putParcelable(TIME_SELECTION_TAG, outState, selection.getCurrentSelection())
            }
            selection.getDraftSelection()?.apply {
                BundleUtils.putParcelable(TIME_SELECTION_DRAFT_TAG, outState, selection.getDraftSelection())
            }
        }
    }

    @Override
    override fun doOnRestoreViewsState(savedState: Bundle) {
        super.doOnRestoreViewsState(savedState)
        savedState.apply {
            BundleUtils.getParcelable(TIME_SELECTION_TAG, this, Time::class.java)?.apply {
                selection.setCurrentSelection(this, false)
            }
            BundleUtils.getParcelable(TIME_SELECTION_DRAFT_TAG, this, Time::class.java)?.apply {
                selection.setDraftSelection(this, false)
            }
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(R.layout.dialog_picker_time_picker, null)
    }

    @Override
    override fun doOnConfigureContent(view: View, savedInstanceState: Bundle?) {
        picker = view.findViewById(R.id.dialogTimePicker)
        picker.setIs24HourView(true)
        picker.isSaveEnabled = false
        picker.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minutesOfHour: Int ->
            val newSelection = Time(hourOfDay, minutesOfHour)
            if (isDialogShown) selection.setDraftSelection(newSelection)
            else selection.setCurrentSelection(newSelection)
        }
    }

    @Suppress("DEPRECATION")
    @Override
    override fun synchronizeSelectUi() {
        val newTime = if (selection.hasDraftSelection()) selection.getDraftSelection()
        else selection.getCurrentSelection()
        if (newTime != null) {
            val hour = newTime.hour
            val minutes = newTime.minute
            val apiLevel = Build.VERSION.SDK_INT
            if (apiLevel < 23) {
                picker.currentHour = hour
                picker.currentMinute = minutes
            } else {
                picker.hour = hour
                picker.minute = minutes
            }
        }
    }

    @Override
    override fun setSelection(newSelection: Time?) {
        selection.apply {
            if (newSelection == null) setCurrentSelection(null)
            else setCurrentSelection(newSelection)
        }
    }

    fun setSelection(hour: Int, minutes: Int) {
        selection.apply {
            val time = Time(hour, minutes)
            setCurrentSelection(time)
        }
    }

    fun setSelection(newSelection: OffsetDateTime?) {
        selection.apply {
            if (newSelection == null) setCurrentSelection(null)
            else setCurrentSelection(Time.fromDate(newSelection))
        }
    }

    class Time(hour: Int, minute: Int) : Parcelable {
        var hour: Int = -1
            private set
        var minute: Int = -1
            private set

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt()
        )

        init {
            val validatedTime = validateTime(hour, minute)
            this.hour = validatedTime[0]
            this.minute = validatedTime[1]
        }

        fun compare(hour: Int, minute: Int): Boolean {
            return hour == this.hour && minute == this.minute
        }

        fun compare(target: Time?): Boolean {
            if (target == null) return false
            return hour == target.hour && minute == target.minute
        }

        fun toArray(): IntArray {
            return intArrayOf(hour, minute)
        }

        private fun validateTime(hour: Int, minute: Int): IntArray {
            val validatedHour = when {
                hour < 0 -> 0
                hour > 23 -> 23
                else -> hour
            }
            val validatedMinute = when {
                minute < 0 -> 0
                minute > 59 -> 59
                else -> minute
            }
            return intArrayOf(validatedHour, validatedMinute)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(hour)
            parcel.writeInt(minute)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Time> {
            override fun createFromParcel(parcel: Parcel): Time {
                return Time(parcel)
            }

            override fun newArray(size: Int): Array<Time?> {
                return arrayOfNulls(size)
            }

            @JvmStatic
            fun fromDate(date: OffsetDateTime): Time {
                val hour = DateUtilsOffsetDate.getHourOfDay(date)
                val minute = DateUtilsOffsetDate.getMinuteOfHour(date)
                return Time(hour, minute)
            }

            @JvmStatic
            fun fromDateString(dateString: String): Time {
                val date = DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dateString)
                return fromDate(date)
            }
        }
    }
}