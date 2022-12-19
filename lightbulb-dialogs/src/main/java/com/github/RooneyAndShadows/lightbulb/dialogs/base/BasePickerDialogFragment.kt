package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners

abstract class BasePickerDialogFragment<SelectionType>(
    protected val selection: BaseDialogSelection<SelectionType>,
    private val synchronizeUiOnDraftChange: Boolean
) : BaseDialogFragment() {
    private var onSelectionChangedListener: SelectionChangedListener<SelectionType>? = null

    protected constructor(selection: BaseDialogSelection<SelectionType>) : this(selection, true) {}

    override fun canShowDialog(dialogLifecycleOwner: LifecycleOwner?): Boolean {
        return true
    }

    protected abstract fun synchronizeSelectUi()

    fun setOnSelectionChangedListener(onSelectionChangedListener: SelectionChangedListener<SelectionType>?) {
        this.onSelectionChangedListener = onSelectionChangedListener
    }

    open fun setSelection(newSelection: SelectionType) {
        selection.currentSelection = newSelection
    }

    fun getSelection(): SelectionType? {
        return selection.currentSelection
    }

    fun hasSelection(): Boolean {
        return selection.hasCurrentSelection()
    }

    protected fun dispatchSelectionChangedEvent(oldValue: SelectionType, newValue: SelectionType) {
        if (onSelectionChangedListener != null) onSelectionChangedListener!!.onSelectionChanged(oldValue, newValue)
    }

    interface SelectionChangedListener<SType> {
        fun onSelectionChanged(oldValue: SType, newValue: SType)
    }

    init {
        addOnShowListener { dialogFragment: BaseDialogFragment? -> selection.startDraft() }
        addOnCancelListener { dialogFragment: BaseDialogFragment? -> selection.revertDraft() }
        addOnNegativeClickListeners { view: View?, dialogFragment: BaseDialogFragment? -> selection.revertDraft() }
        addOnPositiveClickListener { view: View?, dialogFragment: BaseDialogFragment? -> selection.commitDraft() }
        selection.addSelectionListeners(object : PickerSelectionListeners<SelectionType> {
            override fun onCurrentSelectionChangedListener(newValue: SelectionType, oldValue: SelectionType) {
                synchronizeSelectUi()
                dispatchSelectionChangedEvent(oldValue, newValue)
            }

            override fun onDraftSelectionChangedListener(newValue: SelectionType) {
                if (synchronizeUiOnDraftChange) synchronizeSelectUi()
            }

            override fun onDraftCommit(newValue: SelectionType, beforeCommit: SelectionType) {
                synchronizeSelectUi()
                dispatchSelectionChangedEvent(beforeCommit, newValue)
            }

            override fun onDraftReverted() {
                synchronizeSelectUi()
            }
        })
    }
}