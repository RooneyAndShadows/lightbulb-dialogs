package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import java.time.OffsetDateTime

@Suppress("unused")
class TimePickerDialog : BasePickerDialogFragment<IntArray?>(
    TimeSelection(
        intArrayOf(
            DateUtilsOffsetDate.getHourOfDay(DateUtilsOffsetDate.nowLocal()),
            DateUtilsOffsetDate.getMinuteOfHour(DateUtilsOffsetDate.nowLocal())
        ), null
    ), false
) {
    private lateinit var picker: TimePicker
    private val ignorePickerEvent = false

    companion object {
        private const val TIME_SELECTION_TAG = "TIME_SELECTION_TAG"
        private const val TIME_SELECTION_DRAFT_TAG = "TIME_SELECTION_DRAFT_TAG"
        fun newInstance(
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            cancelable: Boolean,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION,
        ): TimePickerDialog {
            return TimePickerDialog().apply {
                this.arguments = DialogBundleHelper().apply {
                    withPositiveButtonConfig(positive)
                    withNegativeButtonConfig(negative)
                    withCancelable(cancelable)
                    withShowing(false)
                    withDialogType(DialogTypes.NORMAL)
                    withAnimation(animationType)
                }.bundle
            }
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            if (hasSelection()) selection.setCurrentSelection(selection.getCurrentSelection())
            else selection.setCurrentSelection(dialogArguments.getIntArray(TIME_SELECTION_TAG))
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(TIME_SELECTION_TAG), false)
            selection.setDraftSelection(savedInstanceState.getIntArray(TIME_SELECTION_DRAFT_TAG), false)
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            selection.getCurrentSelection()?.apply {
                putIntArray(TIME_SELECTION_TAG, selection.getCurrentSelection())
            }
            selection.getDraftSelection()?.apply {
                putIntArray(TIME_SELECTION_DRAFT_TAG, selection.getDraftSelection())
            }
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(R.layout.dialog_picker_time_picker, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
        picker = view.findViewById(R.id.dialogTimePicker)
        picker.setIs24HourView(true)
        picker.isSaveEnabled = false
        synchronizeSelectUi()
        picker.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minutesOfHour: Int ->
            val newSelection = intArrayOf(hourOfDay, minutesOfHour)
            if (isDialogShown) selection.setDraftSelection(newSelection) else selection.setCurrentSelection(newSelection)
        }
    }

    @Suppress("DEPRECATION")
    @Override
    override fun synchronizeSelectUi() {
        val newDate = if (selection.hasDraftSelection()) selection.getDraftSelection()
        else selection.getCurrentSelection()
        if (newDate != null) {
            val hour = newDate[0]
            val minutes = newDate[1]
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
    override fun setSelection(newSelection: IntArray?) {
        if (newSelection == null) selection.setCurrentSelection(null) else setSelection(newSelection[0], newSelection[1])
    }

    fun setSelection(hour: Int, minutes: Int) {
        val time = validateTime(hour, minutes)
        selection.setCurrentSelection(time)
    }

    fun setSelectionFromDate(newSelection: OffsetDateTime?) {
        if (newSelection == null) setSelection(null) else setSelection(
            DateUtilsOffsetDate.extractYearFromDate(newSelection),
            DateUtilsOffsetDate.extractMonthOfYearFromDate(newSelection)
        )
    }

    private fun validateTime(hour: Int, minutes: Int): IntArray {
        var validatedHour = if (hour < 0) 0 else hour
        var validatedMinute = if (minutes < 0) 0 else minutes
        if (validatedMinute > 59)
            validatedMinute = 59
        if (validatedHour > 23)
            validatedHour = 23
        return intArrayOf(validatedHour, validatedMinute)
    }

    private fun getTimeFromDate(date: OffsetDateTime?): IntArray? {
        return if (date == null) null else intArrayOf(
            DateUtilsOffsetDate.getHourOfDay(date),
            DateUtilsOffsetDate.getMinuteOfHour(date)
        )
    }
}