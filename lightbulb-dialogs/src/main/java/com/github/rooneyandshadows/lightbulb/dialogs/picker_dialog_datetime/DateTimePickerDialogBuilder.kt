package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import java.time.OffsetDateTime

@Suppress("unused")
class DateTimePickerDialogBuilder @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null
) : BaseDialogBuilder<DateTimePickerDialog>(dialogTag, dialogParentFragmentManager, dialogLifecycleOwner, initialDialogState) {
    private var dateSetListener: SelectionChangedListener<OffsetDateTime>? = null
    private var initialDate: OffsetDateTime? = null
    private var dateFormat: String? = null

    @JvmOverloads
    constructor(dialogTag: String, fragment: Fragment, initialDialogState: Bundle? = null) : this(
        dialogTag,
        fragment.childFragmentManager,
        fragment,
        initialDialogState
    )

    @JvmOverloads
    constructor(dialogTag: String, activity: FragmentActivity, initialDialogState: Bundle? = null) : this(
        dialogTag,
        activity.supportFragmentManager,
        activity,
        initialDialogState
    )

    @Override
    override fun setupNonRetainableSettings(dialog: DateTimePickerDialog) {
        dialog.apply {
            dateSetListener?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: DateTimePickerDialog) {
        dialog.apply {
            setDialogDateFormat(dateFormat)
            setSelection(initialDate)
        }
    }

    @Override
    override fun initializeNewDialog(): DateTimePickerDialog {
        return DateTimePickerDialog.newInstance()
    }

    @Override
    override fun withTitle(title: String): DateTimePickerDialogBuilder {
        return super.withTitle(title) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): DateTimePickerDialogBuilder {
        return super.withMessage(message) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withButton(configuration: DialogButton): DateTimePickerDialogBuilder {
        return super.withButton(configuration) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): DateTimePickerDialogBuilder {
        return super.withOnCancelListener(listener) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): DateTimePickerDialogBuilder {
        return super.withOnShowListener(listener) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): DateTimePickerDialogBuilder {
        return super.withOnHideListener(listener) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): DateTimePickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): DateTimePickerDialogBuilder {
        return super.withDialogType(dialogType) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): DateTimePickerDialogBuilder {
        return super.withAnimations(animation) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): DateTimePickerDialogBuilder {
        return super.withDialogListeners(listeners) as DateTimePickerDialogBuilder
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<OffsetDateTime>): DateTimePickerDialogBuilder {
        dateSetListener = listener
        return this
    }

    fun withSelection(date: OffsetDateTime?): DateTimePickerDialogBuilder {
        initialDate = date
        return this
    }

    fun withDateFormat(dateFormat: String?): DateTimePickerDialogBuilder {
        this.dateFormat = dateFormat
        return this
    }
}