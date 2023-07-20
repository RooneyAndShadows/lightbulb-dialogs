package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog.*

@Suppress("unused")
class DateRangePickerDialogBuilder @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null
) : BaseDialogBuilder<DateRangePickerDialog>(dialogTag, dialogParentFragmentManager, dialogLifecycleOwner, initialDialogState) {
    private var dateSetListener: SelectionChangedListener<DateRange>? = null
    private var textFrom: String? = null
    private var textTo: String? = null
    private var dateFormat: String? = null
    private var initialRange: DateRange? = null

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
    override fun setupNonRetainableSettings(dialog: DateRangePickerDialog) {
        dialog.apply {
            dateSetListener?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: DateRangePickerDialog) {
        dialog.apply {
            setDialogDateFormat(dateFormat)
            setDialogTextFrom(textFrom)
            setDialogTextTo(textTo)
            setSelection(initialRange)
        }
    }

    @Override
    override fun initializeNewDialog(): DateRangePickerDialog {
        return DateRangePickerDialog.newInstance()
    }

    @Override
    override fun withTitle(title: String): DateRangePickerDialogBuilder {
        return super.withTitle(title) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): DateRangePickerDialogBuilder {
        return super.withMessage(message) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withButton(configuration: DialogButton): DateRangePickerDialogBuilder {
        return super.withButton(configuration) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): DateRangePickerDialogBuilder {
        return super.withOnCancelListener(listener) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): DateRangePickerDialogBuilder {
        return super.withOnShowListener(listener) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): DateRangePickerDialogBuilder {
        return super.withOnHideListener(listener) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): DateRangePickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): DateRangePickerDialogBuilder {
        return super.withDialogType(dialogType) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): DateRangePickerDialogBuilder {
        return super.withAnimations(animation) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): DateRangePickerDialogBuilder {
        return super.withDialogListeners(listeners) as DateRangePickerDialogBuilder
    }

    fun withTextFrom(textFrom: String?): DateRangePickerDialogBuilder {
        this.textFrom = textFrom
        return this
    }

    fun withTextTo(textTo: String?): DateRangePickerDialogBuilder {
        this.textTo = textTo
        return this
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<DateRange>?): DateRangePickerDialogBuilder {
        dateSetListener = listener
        return this
    }

    fun withSelection(range: DateRange?): DateRangePickerDialogBuilder {
        initialRange = range
        return this
    }

    fun withDateFormat(dateFormat: String?): DateRangePickerDialogBuilder {
        this.dateFormat = dateFormat
        return this
    }
}