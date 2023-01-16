package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialogBuilder
import java.time.OffsetDateTime

@Suppress("unused")
class DateTimePickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<DateTimePickerDialog>(lifecycleOwner,
    manager,
    dialogTag) {
    private var dateSetListener: SelectionChangedListener<OffsetDateTime?>? = null
    private var initialDate: OffsetDateTime? = null
    private var dateFormat: String? = null

    @Override
    override fun withSavedState(savedState: Bundle?): DateTimePickerDialogBuilder {
        return super.withSavedState(savedState) as DateTimePickerDialogBuilder
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
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): DateTimePickerDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as DateTimePickerDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): DateTimePickerDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as DateTimePickerDialogBuilder
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

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<OffsetDateTime?>): DateTimePickerDialogBuilder {
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

    @Override
    override fun buildDialog(): DateTimePickerDialog {
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
            dateSetListener?.apply { setOnSelectionChangedListener(this) }
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): DateTimePickerDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as DateTimePickerDialog?
        return dialog ?: DateTimePickerDialog.newInstance().apply {
            if (savedState != null) {
                restoreDialogState(savedState)
                return@apply
            }
            dialogAnimationType = animation
            isCancelable = cancelableOnClickOutside
            dialogNegativeButton = negativeButtonConfiguration
            dialogPositiveButton = positiveButtonConfiguration
            dateFormat?.apply { dialogDateFormat = this }
            setSelection(initialDate)
        }
    }
}