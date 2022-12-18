package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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

class LoadingDialog : BaseDialogFragment() {
    override fun setDialogLayout(inflater: LayoutInflater?): View {
        return View.inflate(context, R.layout.dialog_loading_normal, null)
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {}

    companion object {
        fun newInstance(
            title: String?,
            message: String?,
            dialogType: DialogTypes?,
            animationType: DialogAnimationTypes?
        ): LoadingDialog {
            val dialogFragment = LoadingDialog()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withCancelable(false)
                .withShowing(false)
                .withDialogType(dialogType ?: DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
                .bundle
            return dialogFragment
        }
    }
}