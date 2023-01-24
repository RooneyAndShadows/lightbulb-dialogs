package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog.*

@Suppress("unused")
class DateRangePickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<DateRangePickerDialog>(lifecycleOwner, manager, dialogTag) {
    private var dateSetListener: SelectionChangedListener<DateRange>? = null
    private var textFrom: String? = null
    private var textTo: String? = null
    private var dateFormat: String? = null
    private var initialRange: DateRange? = null

    @Override
    override fun withSavedState(savedState: Bundle?): DateRangePickerDialogBuilder {
        return super.withSavedState(savedState) as DateRangePickerDialogBuilder
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
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): DateRangePickerDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as DateRangePickerDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): DateRangePickerDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as DateRangePickerDialogBuilder
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

    @Override
    override fun buildDialog(): DateRangePickerDialog {
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            dateFormat?.apply { dialogDateFormat = this }
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
            dateSetListener?.apply { addOnSelectionChangedListener(this) }
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): DateRangePickerDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as DateRangePickerDialog?
        return dialog ?: DateRangePickerDialog.newInstance().apply {
            if (savedState != null) {
                restoreDialogState(savedState)
                return@apply
            }
            dialogAnimationType = animation
            isCancelable = cancelableOnClickOutside
            dialogNegativeButton = negativeButtonConfiguration
            dialogPositiveButton = positiveButtonConfiguration
            dialogTextFrom = textFrom
            dialogTextTo = textTo
            setSelection(initialRange)
        }
    }
}