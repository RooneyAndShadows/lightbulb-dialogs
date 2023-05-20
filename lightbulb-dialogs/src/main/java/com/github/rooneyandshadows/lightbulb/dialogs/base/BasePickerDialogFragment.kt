package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener

@Suppress("unused")
@JvmSuppressWildcards
abstract class BasePickerDialogFragment<SelectionType>(
    protected val dialogSelection: BaseDialogSelection<SelectionType>,
    private val synchronizeUiOnDraftChange: Boolean,
) : BaseDialogFragment() {
    private val selectionStateKey = "DIALOG_SELECTION_STATE_KEY"
    private var onSelectionChangedListeners: MutableList<SelectionChangedListener<SelectionType>> = mutableListOf()

    protected constructor(selection: BaseDialogSelection<SelectionType>) : this(selection, true)

    init {
        initializeListeners()
    }

    protected abstract fun onSelectionChange(newSelection: SelectionType?)

    @Override
    override fun doOnSaveDialogProperties(outState: Bundle) {
        super.doOnSaveDialogProperties(outState)
        outState.apply {
            putBundle(selectionStateKey, dialogSelection.saveState())
        }
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        savedState.apply {
            getBundle(selectionStateKey)?.apply {
                dialogSelection.restoreState(this)
            }
        }
    }

    fun addOnSelectionChangedListener(onSelectionChangedListener: SelectionChangedListener<SelectionType>) {
        onSelectionChangedListeners.add(onSelectionChangedListener)
    }

    fun removeOnSelectionChangedListener(onSelectionChangedListener: SelectionChangedListener<SelectionType>) {
        onSelectionChangedListeners.remove(onSelectionChangedListener)
    }

    open fun setSelection(newSelection: SelectionType?) {
        dialogSelection.setCurrentSelection(newSelection)
    }

    fun confirmSelection() {
        dialogSelection.commitDraft()
        dismiss()
    }

    fun cancelSelection() {
        dialogSelection.revertDraft()
        dismiss()
    }

    fun getSelection(): SelectionType? {
        return dialogSelection.getCurrentSelection()
    }

    fun hasSelection(): Boolean {
        return dialogSelection.hasCurrentSelection()
    }

    protected fun dispatchSelectionChangedEvent(newValue: SelectionType?, oldValue: SelectionType?) {
        onSelectionChangedListeners.forEach {
            it.onSelectionChanged(this, newValue, oldValue)
        }
    }

    private fun initializeListeners() {
        addOnShowListener(object : DialogShowListener {
            override fun doOnShow(dialogFragment: BaseDialogFragment) {
                dialogSelection.startDraft()
            }
        })
        addOnCancelListener(object : DialogCancelListener {
            override fun doOnCancel(dialogFragment: BaseDialogFragment) {
                dialogSelection.revertDraft()
            }
        })
        /*addOnNegativeClickListeners(object : DialogButtonClickListener {
            override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                dialogSelection.revertDraft()
            }
        })
        addOnPositiveClickListener(object : DialogButtonClickListener {
            override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                dialogSelection.commitDraft()
            }
        })*/
        dialogSelection.addSelectionListeners(object : PickerSelectionListeners<SelectionType> {
            override fun onCurrentSelectionChangedListener(newValue: SelectionType?, oldValue: SelectionType?) {
                if (isAttached) onSelectionChange(newValue)
                dispatchSelectionChangedEvent(newValue, oldValue)
            }

            override fun onDraftSelectionChangedListener(newValue: SelectionType?, oldValue: SelectionType?) {
                if (synchronizeUiOnDraftChange && isAttached) onSelectionChange(newValue)
            }

            override fun onDraftCommit(newValue: SelectionType?, beforeCommit: SelectionType?) {
                if (isAttached) onSelectionChange(newValue)
                dispatchSelectionChangedEvent(newValue, beforeCommit)
            }

            override fun onDraftReverted() {
                if (isAttached) onSelectionChange(dialogSelection.getCurrentSelection())
            }
        })
    }

    interface SelectionChangedListener<SelectionType> {
        fun onSelectionChanged(
            dialog: BasePickerDialogFragment<SelectionType>,
            newValue: SelectionType?,
            oldValue: SelectionType?,
        )
    }
}