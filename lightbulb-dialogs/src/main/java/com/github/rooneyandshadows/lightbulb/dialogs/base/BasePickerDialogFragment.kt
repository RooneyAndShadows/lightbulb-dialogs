package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener

@Suppress("unused")
@JvmSuppressWildcards
abstract class BasePickerDialogFragment<SelectionType>(
    protected val selection: BaseDialogSelection<SelectionType>,
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
            putBundle(selectionStateKey, selection.saveState())
        }
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        savedState.apply {
            getBundle(selectionStateKey)?.apply {
                selection.restoreState(this)
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
        selection.setCurrentSelection(newSelection)
    }

    fun getSelection(): SelectionType? {
        return selection.getCurrentSelection()
    }

    fun hasSelection(): Boolean {
        return selection.hasCurrentSelection()
    }

    protected fun dispatchSelectionChangedEvent(newValue: SelectionType?, oldValue: SelectionType?) {
        onSelectionChangedListeners.forEach {
            it.onSelectionChanged(this, newValue, oldValue)
        }
    }

    private fun initializeListeners() {
        addOnShowListener(object : DialogShowListener {
            override fun doOnShow(dialogFragment: BaseDialogFragment) {
                selection.startDraft()
            }
        })
        addOnCancelListener(object : DialogCancelListener {
            override fun doOnCancel(dialogFragment: BaseDialogFragment) {
                selection.revertDraft()
            }
        })
        addOnNegativeClickListeners(object : DialogButtonClickListener {
            override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                selection.revertDraft()
            }
        })
        addOnPositiveClickListener(object : DialogButtonClickListener {
            override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                selection.commitDraft()
            }
        })
        selection.addSelectionListeners(object : PickerSelectionListeners<SelectionType> {
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
                if (isAttached) onSelectionChange(selection.getCurrentSelection())
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