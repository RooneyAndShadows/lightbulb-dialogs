package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

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
import java.util.ArrayList

class MonthPickerDialogBuilder : BaseDialogBuilder<MonthPickerDialog?> {
    private var monthSetListener: SelectionChangedListener<IntArray>? = null
    private var disabledMonths: ArrayList<IntArray>? = null
    private var enabledMonths: ArrayList<IntArray>? = null
    private var dateFormat: String? = null
    private var initialSelection: IntArray?
    private var minYear = 1970
    private var maxYear = 2100

    constructor(manager: FragmentManager?, dialogTag: String?) : super(manager, dialogTag) {}
    constructor(lifecycleOwner: LifecycleOwner?, manager: FragmentManager?, dialogTag: String?) : super(
        lifecycleOwner,
        manager,
        dialogTag
    ) {
    }

    override fun withTitle(title: String?): MonthPickerDialogBuilder? {
        return super.withTitle(title) as MonthPickerDialogBuilder
    }

    override fun withMessage(message: String?): MonthPickerDialogBuilder? {
        return super.withMessage(message) as MonthPickerDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): MonthPickerDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as MonthPickerDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): MonthPickerDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as MonthPickerDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): MonthPickerDialogBuilder? {
        return super.withOnCancelListener(listener) as MonthPickerDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): MonthPickerDialogBuilder? {
        return super.withOnShowListener(listener) as MonthPickerDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): MonthPickerDialogBuilder? {
        return super.withOnHideListener(listener) as MonthPickerDialogBuilder
    }

    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): MonthPickerDialogBuilder? {
        return super.withCancelOnClickOutside(closeOnClickOutside) as MonthPickerDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): MonthPickerDialogBuilder? {
        return super.withDialogType(dialogType) as MonthPickerDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): MonthPickerDialogBuilder? {
        return super.withAnimations(animation) as MonthPickerDialogBuilder
    }

    override fun withDialogListeners(callbacks: DialogCallbacks?): MonthPickerDialogBuilder? {
        return super.withDialogListeners(callbacks) as MonthPickerDialogBuilder
    }

    fun withDisabledMonths(disabledMonths: ArrayList<IntArray>?): MonthPickerDialogBuilder {
        this.disabledMonths = disabledMonths
        return this
    }

    fun withEnabledMonths(enabledMonths: ArrayList<IntArray>?): MonthPickerDialogBuilder {
        this.enabledMonths = enabledMonths
        return this
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<IntArray>?): MonthPickerDialogBuilder {
        monthSetListener = listener
        return this
    }

    fun withSelection(year: Int, month: Int): MonthPickerDialogBuilder {
        initialSelection = intArrayOf(year, month)
        return this
    }

    fun withMinYear(year: Int): MonthPickerDialogBuilder {
        minYear = year
        return this
    }

    fun withMaxYear(year: Int): MonthPickerDialogBuilder {
        maxYear = year
        return this
    }

    fun withDateFormat(dateFormat: String?): MonthPickerDialogBuilder {
        this.dateFormat = dateFormat
        return this
    }

    override fun buildDialog(): MonthPickerDialog? {
        var dialogFragment = dialogParentFragmentManager!!.findFragmentByTag(dialogParentFragmentManager) as MonthPickerDialog?
        if (dialogFragment == null) dialogFragment = MonthPickerDialog.Companion.newInstance(
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            dateFormat,
            cancelableOnClickOutside,
            animation
        )
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogCallbacks(dialogListeners)
        dialogFragment.setParentFragManager(dialogParentFragmentManager)
        dialogFragment.setDialogTag(dialogParentFragmentManager)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        dialogFragment.setOnSelectionChangedListener(monthSetListener)
        dialogFragment.setDisabledMonths(disabledMonths)
        dialogFragment.setEnabledMonths(enabledMonths)
        dialogFragment.setCalendarBounds(minYear, maxYear)
        if (initialSelection != null) dialogFragment.setSelection(initialSelection!![0], initialSelection!![1])
        return dialogFragment
    }
}