package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.dialogs.R
import java.time.OffsetDateTime

class TimePickerDialog : BasePickerDialogFragment<IntArray?>(
    TimeSelection(
        intArrayOf(
            DateUtilsOffsetDate.getHourOfDay(DateUtilsOffsetDate.nowLocal()),
            DateUtilsOffsetDate.getMinuteOfHour(DateUtilsOffsetDate.nowLocal())
        ), null
    ), false
) {
    private var picker: TimePicker? = null
    private val ignorePickerEvent = false
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            if (hasSelection()) selection.setCurrentSelection(selection.currentSelection) else selection.setCurrentSelection(
                dialogArguments.getIntArray(
                    TIME_SELECTION_TAG
                )
            )
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(TIME_SELECTION_TAG), false)
            selection.setDraftSelection(savedInstanceState.getIntArray(TIME_SELECTION_DRAFT_TAG), false)
        }
    }

    override fun doOnSaveInstanceState(outState: Bundle?) {
        if (selection.currentSelection != null) outState!!.putIntArray(TIME_SELECTION_TAG, selection.currentSelection)
        if (selection.draftSelection != null) outState!!.putIntArray(TIME_SELECTION_DRAFT_TAG, selection.draftSelection)
    }

    override fun setDialogLayout(inflater: LayoutInflater?): View {
        return View.inflate(context, R.layout.dialog_picker_time_picker, null)
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {
        picker = view!!.findViewById(R.id.dialogTimePicker)
        picker.setIs24HourView(true)
        picker.setSaveEnabled(false)
        synchronizeSelectUi()
        picker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { timePicker: TimePicker?, hourOfDay: Int, minutesOfHour: Int ->
            val newSelection = intArrayOf(hourOfDay, minutesOfHour)
            if (isDialogShown) selection.setDraftSelection(newSelection) else selection.setCurrentSelection(newSelection)
        })
    }

    fun setSelection(hour: Int, minutes: Int) {
        val newHour = validateHour(hour)
        val newMinute = validateMinute(minutes)
        selection.currentSelection = intArrayOf(newHour, newMinute)
    }

    override fun synchronizeSelectUi() {
        val newDate = if (selection.hasDraftSelection()) selection.draftSelection else selection.currentSelection
        if (newDate != null) {
            if (picker == null) return
            val hour = newDate[0]
            val minutes = newDate[1]
            val apiLevel = Build.VERSION.SDK_INT
            if (apiLevel < 23) {
                picker!!.currentHour = hour
                picker!!.currentMinute = minutes
            } else {
                picker!!.hour = hour
                picker!!.minute = minutes
            }
        }
    }

    override fun setSelection(newSelection: IntArray?) {
        if (newSelection == null) selection.setCurrentSelection(null) else setSelection(newSelection[0], newSelection[1])
    }

    fun setSelectionFromDate(newSelection: OffsetDateTime?) {
        if (newSelection == null) setSelection(null) else setSelection(
            DateUtilsOffsetDate.extractYearFromDate(newSelection),
            DateUtilsOffsetDate.extractMonthOfYearFromDate(newSelection)
        )
    }

    private fun validateHour(hour: Int): Int {
        var hour = hour
        var minutes = selection.currentSelection.get(1)
        if (hour >= 24) {
            hour = 23
            minutes = 59
        }
        if (hour < 0) {
            hour = 0
        }
        return hour
    }

    private fun validateMinute(minutes: Int): Int {
        var minutes = minutes
        val hour = selection.currentSelection.get(0)
        if (minutes == 60) {
            validateHour(hour + 1)
            minutes = 0
        }
        if (minutes < 0) minutes = 0
        return minutes
    }

    private fun getTimeFromDate(date: OffsetDateTime?): IntArray? {
        return if (date == null) null else intArrayOf(
            DateUtilsOffsetDate.getHourOfDay(date),
            DateUtilsOffsetDate.getMinuteOfHour(date)
        )
    }

    companion object {
        private const val TIME_SELECTION_TAG = "TIME_SELECTION_TAG"
        private const val TIME_SELECTION_DRAFT_TAG = "TIME_SELECTION_DRAFT_TAG"
        fun newInstance(
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            cancelable: Boolean,
            animationType: DialogAnimationTypes?
        ): TimePickerDialog {
            val f = TimePickerDialog()
            val bundleHelper = DialogBundleHelper()
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
            f.arguments = bundleHelper.bundle
            return f
        }
    }
}