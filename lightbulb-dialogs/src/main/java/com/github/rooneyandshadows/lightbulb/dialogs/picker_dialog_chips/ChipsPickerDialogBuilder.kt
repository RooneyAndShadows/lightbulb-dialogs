package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsFilterView.OnOptionCreatedListener

@Suppress("unused")
class ChipsPickerDialogBuilder @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null
) : BaseDialogBuilder<ChipsPickerDialog>(dialogTag, dialogParentFragmentManager, dialogLifecycleOwner, initialDialogState) {
    private var isFilterable: Boolean = true
    private var allowOptionAddition: Boolean = true
    private var maxRows: Int? = null
    private var filterHintText: String? = null
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var onChipCreatedListener: OnOptionCreatedListener? = null
    private var selection: IntArray? = null

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
    override fun setupNonRetainableSettings(dialog: ChipsPickerDialog) {
        dialog.apply {
            changedCallback?.apply { addOnSelectionChangedListener(this) }
            onChipCreatedListener?.apply { setOnNewOptionListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: ChipsPickerDialog) {
        dialog.apply {
            setFilterable(isFilterable)
            setAllowAddNewOptions(allowOptionAddition)
            maxRows?.apply { setMaxRows(this) }
            filterHintText?.apply { setFilterHintText(this) }
            setSelection(selection)
        }
    }

    @Override
    override fun initializeNewDialog(): ChipsPickerDialog {
        return ChipsPickerDialog.newInstance()
    }

    @Override
    override fun withTitle(title: String): ChipsPickerDialogBuilder {
        return super.withTitle(title) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): ChipsPickerDialogBuilder {
        return super.withMessage(message) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withButton(configuration: DialogButton): ChipsPickerDialogBuilder {
        return super.withButton(configuration) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): ChipsPickerDialogBuilder {
        return super.withOnCancelListener(listener) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): ChipsPickerDialogBuilder {
        return super.withOnShowListener(listener) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): ChipsPickerDialogBuilder {
        return super.withOnHideListener(listener) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): ChipsPickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): ChipsPickerDialogBuilder {
        return super.withDialogType(dialogType) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): ChipsPickerDialogBuilder {
        return super.withAnimations(animation) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): ChipsPickerDialogBuilder {
        return super.withDialogListeners(listeners) as ChipsPickerDialogBuilder
    }

    fun withOnChipCreatedListener(listener: OnOptionCreatedListener): ChipsPickerDialogBuilder {
        this.onChipCreatedListener = listener
        return this
    }

    fun withAllowNewOptions(allowNewOptions: Boolean): ChipsPickerDialogBuilder {
        this.allowOptionAddition = allowNewOptions
        return this
    }

    fun withFilterHintText(hintText: String): ChipsPickerDialogBuilder {
        this.filterHintText = hintText
        return this
    }

    fun withFilterable(filterable: Boolean): ChipsPickerDialogBuilder {
        this.isFilterable = filterable
        return this
    }

    fun withMaxRows(maxRows: Int): ChipsPickerDialogBuilder {
        this.maxRows = maxRows
        return this
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray>): ChipsPickerDialogBuilder {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): ChipsPickerDialogBuilder {
        this.selection = selection
        return this
    }
}