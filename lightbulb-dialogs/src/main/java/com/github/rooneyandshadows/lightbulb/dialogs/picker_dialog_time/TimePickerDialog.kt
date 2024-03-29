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
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialog.*
import java.time.OffsetDateTime

@Suppress("unused", "UNUSED_PARAMETER", "DEPRECATION")
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
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}

    companion object {
        fun newInstance(): TimePickerDialog {
            return TimePickerDialog()
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        if (hasSelection()) dialogSelection.setCurrentSelection(dialogSelection.getCurrentSelection())
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(R.layout.dialog_picker_time_picker, null)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        picker = view.findViewById(R.id.dialogTimePicker)
        picker.setIs24HourView(true)
        picker.isSaveEnabled = false
        dialogSelection.getActiveSelection()?.apply {
            picker.hour = hour
            picker.minute = minute
        }
    }

    @Override
    override fun onSelectionChange(newSelection: Time?) {
        if (!checkIfTimePickerNeedsSync(newSelection)) return
        picker.apply {
            newSelection?.apply {
                val hour = hour
                val minutes = minute
                val apiLevel = Build.VERSION.SDK_INT
                if (apiLevel < Build.VERSION_CODES.M) {
                    picker.currentHour = hour
                    picker.currentMinute = minutes
                } else {
                    picker.hour = hour
                    picker.minute = minutes
                }
            }
        }
    }

    private fun checkIfTimePickerNeedsSync(currentValue: Time?): Boolean {
        val timePickerValue = picker.let {
            Time(it.hour, it.minute)
        }
        return !timePickerValue.compare(currentValue)
    }

    @Override
    override fun doOnViewStateRestored(savedInstanceState: Bundle?) {
        super.doOnViewStateRestored(savedInstanceState)
        picker.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minutesOfHour: Int ->
            val newSelection = Time(hourOfDay, minutesOfHour)
            if (isDialogShown) dialogSelection.setDraftSelection(newSelection)
            else dialogSelection.setCurrentSelection(newSelection)
        }
    }

    @Override
    override fun setSelection(newSelection: Time?) {
        dialogSelection.apply {
            if (newSelection == null) setCurrentSelection(null)
            else setCurrentSelection(newSelection)
        }
    }

    fun setSelection(hour: Int, minutes: Int) {
        dialogSelection.apply {
            val time = Time(hour, minutes)
            setCurrentSelection(time)
        }
    }

    fun setSelection(newSelection: OffsetDateTime?) {
        dialogSelection.apply {
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