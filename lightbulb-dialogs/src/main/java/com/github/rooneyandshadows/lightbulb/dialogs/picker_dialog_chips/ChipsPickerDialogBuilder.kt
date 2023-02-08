package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

@Suppress("unused")
class ChipsPickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<ChipsPickerDialog>(lifecycleOwner, manager, dialogTag) {
    private var isFilterable: Boolean = true
    private var allowOptionAddition: Boolean = true
    private var maxRows: Int = -1
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var selection: IntArray? = null

    @Override
    override fun setupNonRetainableSettings(dialog: ChipsPickerDialog) {
        dialog.apply {
            changedCallback?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: ChipsPickerDialog) {
        dialog.apply {
            setFilterable(isFilterable)
            setAllowNewOptionCreation(allowOptionAddition)
            if (maxRows != -1) setMaxRows(maxRows)
            setSelection(selection)
        }
    }

    @Override
    override fun initializeNewDialog(): ChipsPickerDialog {
        return ChipsPickerDialog.newInstance()
    }

    @Override
    override fun withInitialDialogState(savedState: Bundle?): ChipsPickerDialogBuilder {
        return super.withInitialDialogState(savedState) as ChipsPickerDialogBuilder
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
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener?,
    ): ChipsPickerDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as ChipsPickerDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener?,
    ): ChipsPickerDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as ChipsPickerDialogBuilder
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

    fun withAllowNewOptions(allowNewOptions: Boolean): ChipsPickerDialogBuilder {
        this.allowOptionAddition = allowNewOptions
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