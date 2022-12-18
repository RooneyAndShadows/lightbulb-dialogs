package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

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
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager

class TimePickerDialogBuilder : BaseDialogBuilder<TimePickerDialog?> {
    private var timeSetListener: SelectionChangedListener<IntArray>? = null
    private var initialTime: IntArray

    constructor(manager: FragmentManager?, dialogTag: String?) : super(manager, dialogTag) {}
    constructor(lifecycleOwner: LifecycleOwner?, manager: FragmentManager?, dialogTag: String?) : super(
        lifecycleOwner,
        manager,
        dialogTag
    ) {
    }

    override fun withTitle(title: String?): TimePickerDialogBuilder? {
        return super.withTitle(title) as TimePickerDialogBuilder
    }

    override fun withMessage(message: String?): TimePickerDialogBuilder? {
        return super.withMessage(message) as TimePickerDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): TimePickerDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as TimePickerDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): TimePickerDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as TimePickerDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): TimePickerDialogBuilder? {
        return super.withOnCancelListener(listener) as TimePickerDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): TimePickerDialogBuilder? {
        return super.withOnShowListener(listener) as TimePickerDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): TimePickerDialogBuilder? {
        return super.withOnHideListener(listener) as TimePickerDialogBuilder
    }

    override fun withCancelOnClickOutsude(closeOnClickOutside: Boolean): TimePickerDialogBuilder? {
        return super.withCancelOnClickOutsude(closeOnClickOutside) as TimePickerDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): TimePickerDialogBuilder? {
        return super.withDialogType(dialogType) as TimePickerDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): TimePickerDialogBuilder? {
        return super.withAnimations(animation) as TimePickerDialogBuilder
    }

    override fun withDialogCallbacks(callbacks: DialogCallbacks?): TimePickerDialogBuilder? {
        return super.withDialogCallbacks(callbacks) as TimePickerDialogBuilder
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<IntArray>?): TimePickerDialogBuilder {
        timeSetListener = listener
        return this
    }

    fun withInitialTime(hour: Int, minute: Int): TimePickerDialogBuilder {
        initialTime = intArrayOf(hour, minute)
        return this
    }

    fun withInitialTime(time: IntArray): TimePickerDialogBuilder {
        initialTime = time
        return this
    }

    override fun buildDialog(): TimePickerDialog? {
        var dialogFragment = fragmentManager!!.findFragmentByTag(dialogTag) as TimePickerDialog?
        if (dialogFragment == null) dialogFragment = TimePickerDialog.Companion.newInstance(
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            cancelableOnClickOutside,
            animation
        )
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogCallbacks(dialogCallbacks)
        dialogFragment.setFragmentManager(fragmentManager)
        dialogFragment.setDialogTag(dialogTag)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.selection = initialTime
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        dialogFragment.setOnSelectionChangedListener(timeSetListener)
        return dialogFragment
    }
}