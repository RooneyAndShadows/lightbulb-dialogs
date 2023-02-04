package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.os.Bundle
import java.util.ArrayList
import java.util.HashMap

@JvmSuppressWildcards
abstract class BaseDialogSelection<SelectionType>(current: SelectionType?, draft: SelectionType?) {
    private val selectionCurrentKey = "SELECTION_CURRENT"
    private val selectionDraftKey = "SELECTION_DRAFT"
    protected val CURRENT_SELECTION_STATE_KEY = "CURRENT_SELECTION_STATE_KEY"
    protected val DRAFT_SELECTION_STATE_KEY = "DRAFT_SELECTION_STATE_KEY"
    private val selection = HashMap<String, SelectionType?>()
    private val selectionListeners = ArrayList<PickerSelectionListeners<SelectionType>>()
    protected abstract fun compareValues(v1: SelectionType?, v2: SelectionType?): Boolean
    abstract fun hasCurrentSelection(): Boolean
    abstract fun hasDraftSelection(): Boolean
    abstract fun saveState(): Bundle
    abstract fun restoreState(source: Bundle)

    fun getActiveSelection(): SelectionType? {
        return if (hasDraftSelection()) getDraftSelection()
        else getCurrentSelection()
    }

    init {
        selection[selectionCurrentKey] = current
        selection[selectionDraftKey] = draft
    }

    fun getCurrentSelection(): SelectionType? {
        return selection[selectionCurrentKey]
    }

    fun getDraftSelection(): SelectionType? {
        return selection[selectionDraftKey]
    }

    fun addSelectionListeners(selectionListeners: PickerSelectionListeners<SelectionType>) {
        this.selectionListeners.add(selectionListeners)
    }

    fun setCurrentSelection(newValue: SelectionType?) {
        setCurrentSelection(newValue, true)
    }

    fun setDraftSelection(newValue: SelectionType?) {
        setDraftSelection(newValue, true)
    }

    open fun setCurrentSelection(newValue: SelectionType?, notify: Boolean) {
        val current = selection[selectionCurrentKey]
        if (compareValues(current, newValue)) return
        selection.remove(selectionCurrentKey)
        selection[selectionCurrentKey] = newValue
        if (!notify) return
        for (listener in selectionListeners)
            listener.onCurrentSelectionChangedListener(newValue, current)
    }

    open fun setDraftSelection(newValue: SelectionType?, notify: Boolean) {
        val draft = selection[selectionDraftKey]
        if (compareValues(draft, newValue)) return
        selection.remove(selectionDraftKey)
        selection[selectionDraftKey] = newValue
        if (!notify) return
        for (listener in selectionListeners)
            listener.onDraftSelectionChangedListener(newValue, draft)
    }

    fun clearDraft() {
        setDraftSelection(null, false)
    }

    fun startDraft() {
        val current = getCurrentSelection()
        setDraftSelection(current, false)
    }

    fun commitDraft() {
        val beforeCommit = getCurrentSelection()
        val draft = getDraftSelection()
        setCurrentSelection(draft, false)
        clearDraft()
        for (listener in selectionListeners) listener.onDraftCommit(draft, beforeCommit)
    }

    fun revertDraft() {
        clearDraft()
        for (listener in selectionListeners) listener.onDraftReverted()
    }

    interface PickerSelectionListeners<SelectionType> {
        fun onCurrentSelectionChangedListener(newValue: SelectionType?, oldValue: SelectionType?)
        fun onDraftSelectionChangedListener(newValue: SelectionType?, oldValue: SelectionType?)
        fun onDraftCommit(newValue: SelectionType?, beforeCommit: SelectionType?)
        fun onDraftReverted()
    }
}