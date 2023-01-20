package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialog.Time

@Suppress("unused")
class TimePickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<TimePickerDialog>(lifecycleOwner, manager, dialogTag) {
    private var timeSetListener: SelectionChangedListener<Time?>? = null
    private var initialTime: Time? = null

    @Override
    override fun withSavedState(savedState: Bundle?): TimePickerDialogBuilder {
        return super.withSavedState(savedState) as TimePickerDialogBuilder
    }

    @Override
    override fun withTitle(title: String): TimePickerDialogBuilder {
        return super.withTitle(title) as TimePickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): TimePickerDialogBuilder {
        return super.withMessage(message) as TimePickerDialogBuilder
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): TimePickerDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as TimePickerDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): TimePickerDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as TimePickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): TimePickerDialogBuilder {
        return super.withOnCancelListener(listener) as TimePickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): TimePickerDialogBuilder {
        return super.withOnShowListener(listener) as TimePickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): TimePickerDialogBuilder {
        return super.withOnHideListener(listener) as TimePickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): TimePickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as TimePickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): TimePickerDialogBuilder {
        return super.withDialogType(dialogType) as TimePickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): TimePickerDialogBuilder {
        return super.withAnimations(animation) as TimePickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): TimePickerDialogBuilder {
        return super.withDialogListeners(listeners) as TimePickerDialogBuilder
    }

    @Override
    fun withOnDateSelectedEvent(listener: SelectionChangedListener<Time?>): TimePickerDialogBuilder {
        timeSetListener = listener
        return this
    }

    @Override
    fun withInitialTime(hour: Int, minute: Int): TimePickerDialogBuilder {
        initialTime = Time(hour, minute)
        return this
    }

    @Override
    fun withInitialTime(time: Time): TimePickerDialogBuilder {
        initialTime = time
        return this
    }

    @Override
    override fun buildDialog(): TimePickerDialog {
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            setOnSelectionChangedListener(timeSetListener)
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): TimePickerDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as TimePickerDialog?
        return dialog ?: TimePickerDialog.newInstance().apply {
            dialogPositiveButton = positiveButtonConfiguration
            dialogNegativeButton = negativeButtonConfiguration
            isCancelable = cancelableOnClickOutside
            dialogAnimationType = animation
            setSelection(initialTime)
        }
    }
}