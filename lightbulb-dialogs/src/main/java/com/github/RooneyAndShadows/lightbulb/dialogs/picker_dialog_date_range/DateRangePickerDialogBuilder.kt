package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

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

class DateRangePickerDialogBuilder : BaseDialogBuilder<DateRangePickerDialog?> {
    private var dateSetListener: SelectionChangedListener<Array<OffsetDateTime>>? = null
    private var textFrom: String? = null
    private var textTo: String? = null
    private var dateFormat: String? = null
    private var initialRange: Array<OffsetDateTime?>

    constructor(manager: FragmentManager?, dialogTag: String?) : super(manager, dialogTag) {}
    constructor(lifecycleOwner: LifecycleOwner?, manager: FragmentManager?, dialogTag: String?) : super(
        lifecycleOwner,
        manager,
        dialogTag
    ) {
    }

    override fun withTitle(title: String?): DateRangePickerDialogBuilder? {
        return super.withTitle(title) as DateRangePickerDialogBuilder
    }

    override fun withMessage(message: String?): DateRangePickerDialogBuilder? {
        return super.withMessage(message) as DateRangePickerDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): DateRangePickerDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as DateRangePickerDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): DateRangePickerDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as DateRangePickerDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): DateRangePickerDialogBuilder? {
        return super.withOnCancelListener(listener) as DateRangePickerDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): DateRangePickerDialogBuilder? {
        return super.withOnShowListener(listener) as DateRangePickerDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): DateRangePickerDialogBuilder? {
        return super.withOnHideListener(listener) as DateRangePickerDialogBuilder
    }

    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): DateRangePickerDialogBuilder? {
        return super.withCancelOnClickOutside(closeOnClickOutside) as DateRangePickerDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): DateRangePickerDialogBuilder? {
        return super.withDialogType(dialogType) as DateRangePickerDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): DateRangePickerDialogBuilder? {
        return super.withAnimations(animation) as DateRangePickerDialogBuilder
    }

    override fun withDialogListeners(callbacks: DialogCallbacks?): DateRangePickerDialogBuilder? {
        return super.withDialogListeners(callbacks) as DateRangePickerDialogBuilder
    }

    fun withTextFrom(textFrom: String?): DateRangePickerDialogBuilder {
        this.textFrom = textFrom
        return this
    }

    fun withTextTo(textTo: String?): DateRangePickerDialogBuilder {
        this.textTo = textTo
        return this
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<Array<OffsetDateTime>>?): DateRangePickerDialogBuilder {
        dateSetListener = listener
        return this
    }

    fun withSelection(range: Array<OffsetDateTime?>): DateRangePickerDialogBuilder {
        initialRange = range
        return this
    }

    fun withDateFormat(dateFormat: String?): DateRangePickerDialogBuilder {
        this.dateFormat = dateFormat
        return this
    }

    override fun buildDialog(): DateRangePickerDialog? {
        var dialogFragment = fragmentManager!!.findFragmentByTag(dialogTag) as DateRangePickerDialog?
        if (dialogFragment == null) dialogFragment = DateRangePickerDialog.Companion.newInstance(
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            dateFormat,
            textFrom,
            textTo,
            cancelableOnClickOutside,
            animation
        )
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogCallbacks(dialogListeners)
        dialogFragment.setFragmentManager(fragmentManager)
        dialogFragment.setDialogTag(dialogTag)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.setOnSelectionChangedListener(dateSetListener)
        dialogFragment.selection = initialRange
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        return dialogFragment
    }
}