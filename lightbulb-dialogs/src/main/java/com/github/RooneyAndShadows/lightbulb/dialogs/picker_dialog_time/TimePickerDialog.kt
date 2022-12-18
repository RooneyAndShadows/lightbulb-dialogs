package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.getItems
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.setCollection
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.appendCollection
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.isItemSelected
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.selectItemAt
import com.mikepenz.iconics.typeface.IIcon.name
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.addOrReplaceSelectionChangedListener
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter.selectPositions
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.commons.utils.WindowUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.base.BaseDialogConstraintBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogShowListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogHideListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogCallbacks
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.appcompat.widget.LinearLayoutCompat
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogButtonConfigurationCreator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialogBuilder
import androidx.appcompat.widget.AppCompatTextView
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.CustomDialogInflater
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder.CustomDialogInitializer
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter.IconModel
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerDialog
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterSelectableModes
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import androidx.appcompat.widget.AppCompatImageButton
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter.IconVH
import com.mikepenz.iconics.typeface.IIcon
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter.IconSet
import com.github.rooneyandshadows.lightbulb.commons.utils.IconUtils
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimeSelection
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter.ColorVH
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthSelection
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialog
import com.github.rooneyandshadows.lightbulb.calendars.month.MonthCalendarView.SelectionChangeListener
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialogSelection
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.callbacks.EasyAdapterSelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime.DateTimeSelection
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime.DateTimePickerDialog
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime.DateTimePickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangeSelection
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialogBuilder
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
    override fun create(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
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

    override fun saveInstanceState(outState: Bundle?) {
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