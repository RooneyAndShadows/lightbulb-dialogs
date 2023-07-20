package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialog.*

@Suppress("unused")
class MonthPickerDialogBuilder @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null
) : BaseDialogBuilder<MonthPickerDialog>(dialogTag, dialogParentFragmentManager, dialogLifecycleOwner, initialDialogState) {
    private var monthSetListener: SelectionChangedListener<Month>? = null
    private var disabledMonths: MutableList<Month> = mutableListOf()
    private var enabledMonths: MutableList<Month> = mutableListOf()
    private var dateFormat: String? = null
    private var initialSelection: Month? = null
    private var minYear = 1970
    private var maxYear = 2100

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
    override fun setupNonRetainableSettings(dialog: MonthPickerDialog) {
        dialog.apply {
            monthSetListener?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: MonthPickerDialog) {
        dialog.apply {
            val builder = this@MonthPickerDialogBuilder
            setDisabledMonths(builder.disabledMonths)
            setEnabledMonths(builder.enabledMonths)
            setDialogDateFormat(dateFormat)
            setCalendarBounds(minYear, maxYear)
            setSelection(initialSelection)
        }
    }

    @Override
    override fun initializeNewDialog(): MonthPickerDialog {
        return MonthPickerDialog.newInstance()
    }

    @Override
    override fun withTitle(title: String): MonthPickerDialogBuilder {
        return super.withTitle(title) as MonthPickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): MonthPickerDialogBuilder {
        return super.withMessage(message) as MonthPickerDialogBuilder
    }

    @Override
    override fun withButton(configuration: DialogButton): MonthPickerDialogBuilder {
        return super.withButton(configuration) as MonthPickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): MonthPickerDialogBuilder {
        return super.withOnCancelListener(listener) as MonthPickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): MonthPickerDialogBuilder {
        return super.withOnShowListener(listener) as MonthPickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): MonthPickerDialogBuilder {
        return super.withOnHideListener(listener) as MonthPickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): MonthPickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as MonthPickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): MonthPickerDialogBuilder {
        return super.withDialogType(dialogType) as MonthPickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): MonthPickerDialogBuilder {
        return super.withAnimations(animation) as MonthPickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): MonthPickerDialogBuilder {
        return super.withDialogListeners(listeners) as MonthPickerDialogBuilder
    }

    fun withDisabledMonths(disabledMonths: List<Month>): MonthPickerDialogBuilder {
        this.disabledMonths.apply {
            clear()
            addAll(disabledMonths)
        }
        return this
    }

    fun withEnabledMonths(enabledMonths: List<Month>): MonthPickerDialogBuilder {
        this.enabledMonths.apply {
            clear()
            addAll(enabledMonths)
        }
        return this
    }

    fun withOnDateSelectedEvent(listener: SelectionChangedListener<Month>): MonthPickerDialogBuilder {
        monthSetListener = listener
        return this
    }

    fun withSelection(month: Month): MonthPickerDialogBuilder {
        initialSelection = month
        return this
    }

    fun withSelection(year: Int, month: Int): MonthPickerDialogBuilder {
        initialSelection = Month(year, month)
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
}