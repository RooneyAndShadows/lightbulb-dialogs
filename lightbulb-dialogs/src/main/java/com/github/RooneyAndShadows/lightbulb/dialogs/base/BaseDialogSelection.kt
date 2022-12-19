package com.github.rooneyandshadows.lightbulb.dialogs.base

import java.util.ArrayList
import java.util.HashMap

abstract class BaseDialogSelection<SelectionType>(current: SelectionType?, draft: SelectionType?) {
    private val selection = HashMap<String, SelectionType?>()
    private val selectionListeners = ArrayList<PickerSelectionListeners<SelectionType?>>()
    abstract fun compareValues(v1: SelectionType?, v2: SelectionType?): Boolean
    abstract fun hasCurrentSelection(): Boolean
    abstract fun hasDraftSelection(): Boolean
    val currentSelection: SelectionType?
        get() = selection[SELECTION_CURRENT]
    val draftSelection: SelectionType?
        get() = selection[SELECTION_DRAFT]

    fun addSelectionListeners(selectionListeners: PickerSelectionListeners<SelectionType>) {
        this.selectionListeners.add(selectionListeners)
    }

    fun setCurrentSelection(newValue: SelectionType) {
        setCurrentSelection(newValue, true)
    }

    fun setDraftSelection(newValue: SelectionType) {
        setDraftSelection(newValue, true)
    }

    open fun setCurrentSelection(newValue: SelectionType?, notify: Boolean) {
        val current = selection[SELECTION_CURRENT]
        if (compareValues(current, newValue)) return
        selection.remove(SELECTION_CURRENT)
        selection[SELECTION_CURRENT] = newValue
        if (notify) for (listener in selectionListeners) listener.onCurrentSelectionChangedListener(newValue, current)
    }

    open fun setDraftSelection(newValue: SelectionType?, notify: Boolean) {
        val draft = selection[SELECTION_DRAFT]
        if (compareValues(draft, newValue)) return
        selection.remove(SELECTION_DRAFT)
        selection[SELECTION_DRAFT] = newValue
        if (notify) for (listener in selectionListeners) listener.onDraftSelectionChangedListener(newValue)
    }

    fun clearDraft() {
        setDraftSelection(null, false)
    }

    fun startDraft() {
        val current = currentSelection
        setDraftSelection(current, false)
    }

    fun commitDraft() {
        val beforeCommit = currentSelection
        val draft = draftSelection
        setCurrentSelection(draft, false)
        clearDraft()
        for (listener in selectionListeners) listener.onDraftCommit(draft, beforeCommit)
    }

    fun revertDraft() {
        clearDraft()
        for (listener in selectionListeners) listener.onDraftReverted()
    }

    interface PickerSelectionListeners<SelectionType> {
        fun onCurrentSelectionChangedListener(newValue: SelectionType, oldValue: SelectionType)
        fun onDraftSelectionChangedListener(newValue: SelectionType)
        fun onDraftCommit(newValue: SelectionType, beforeCommit: SelectionType)
        fun onDraftReverted()
    }

    companion object {
        private const val SELECTION_CURRENT = "SELECTION_CURRENT"
        private const val SELECTION_DRAFT = "SELECTION_DRAFT"
    }

    init {
        selection[SELECTION_CURRENT] = current
        selection[SELECTION_DRAFT] = draft
    }
}