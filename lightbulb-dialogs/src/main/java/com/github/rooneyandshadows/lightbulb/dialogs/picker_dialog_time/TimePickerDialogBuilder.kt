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
    private var timeSetListener: SelectionChangedListener<Time>? = null
    private var initialTime: Time? = null

    @Override
    override fun setupNonRetainableSettings(dialog: TimePickerDialog) {
        dialog.apply {
            timeSetListener?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: TimePickerDialog) {
        dialog.apply {
            setSelection(initialTime)
        }
    }

    @Override
    override fun initializeNewDialog(): TimePickerDialog {
        return TimePickerDialog.newInstance()
    }

    @Override
    override fun withInitialDialogState(savedState: Bundle?): TimePickerDialogBuilder {
        return super.withInitialDialogState(savedState) as TimePickerDialogBuilder
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
    override fun withButton(configuration: DialogButton): TimePickerDialogBuilder {
        return super.withButton(configuration) as TimePickerDialogBuilder
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
    fun withOnDateSelectedEvent(listener: SelectionChangedListener<Time>): TimePickerDialogBuilder {
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
}