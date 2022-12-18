package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

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
import java.time.OffsetDateTime

class DateTimePickerDialogBuilder : BaseDialogBuilder<DateTimePickerDialog?> {
    private var dateSetListener: SelectionChangedListener<OffsetDateTime>? = null
    private var initialDate: OffsetDateTime? = null
    private var dateFormat: String? = null

    constructor(manager: FragmentManager?, dialogTag: String?) : super(manager, dialogTag) {}
    constructor(lifecycleOwner: LifecycleOwner?, manager: FragmentManager?, dialogTag: String?) : super(
        lifecycleOwner,
        manager,
        dialogTag
    ) {
    }

    override fun withTitle(title: String?): DateTimePickerDialogBuilder? {
        return super.withTitle(title) as DateTimePickerDialogBuilder
    }

    override fun withMessage(message: String?): DateTimePickerDialogBuilder? {
        return super.withMessage(message) as DateTimePickerDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): DateTimePickerDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as DateTimePickerDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): DateTimePickerDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as DateTimePickerDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): DateTimePickerDialogBuilder? {
        return super.withOnCancelListener(listener) as DateTimePickerDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): DateTimePickerDialogBuilder? {
        return super.withOnShowListener(listener) as DateTimePickerDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): DateTimePickerDialogBuilder? {
        return super.withOnHideListener(listener) as DateTimePickerDialogBuilder
    }

    override fun withCancelOnClickOutsude(closeOnClickOutside: Boolean): DateTimePickerDialogBuilder? {
        return super.withCancelOnClickOutsude(closeOnClickOutside) as DateTimePickerDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): DateTimePickerDialogBuilder? {
        return super.withDialogType(dialogType) as DateTimePickerDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): DateTimePickerDialogBuilder? {
        return super.withAnimations(animation) as DateTimePickerDialogBuilder
    }

    override fun withDialogCallbacks(callbacks: DialogCallbacks?): DateTimePickerDialogBuilder? {
        return super.withDialogCallbacks(callbacks) as DateTimePickerDialogBuilder
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<OffsetDateTime>?): DateTimePickerDialogBuilder {
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

    override fun buildDialog(): DateTimePickerDialog? {
        var dialogFragment = fragmentManager!!.findFragmentByTag(dialogTag) as DateTimePickerDialog?
        if (dialogFragment == null) dialogFragment = DateTimePickerDialog.Companion.newInstance(
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            dateFormat,
            cancelableOnClickOutside,
            animation
        )
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogCallbacks(dialogCallbacks)
        dialogFragment.setFragmentManager(fragmentManager)
        dialogFragment.setDialogTag(dialogTag)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.setOnSelectionChangedListener(dateSetListener)
        dialogFragment.selection = initialDate
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        return dialogFragment
    }
}