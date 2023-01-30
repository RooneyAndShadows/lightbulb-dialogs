package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener

@Suppress("unused")
abstract class BasePickerDialogFragment<SelectionType>(
    protected val selection: BaseDialogSelection<SelectionType>,
    private val synchronizeUiOnDraftChange: Boolean,
) : BaseDialogFragment() {
    private var onSelectionChangedListeners: MutableList<SelectionChangedListener<SelectionType>> = mutableListOf()

    protected constructor(selection: BaseDialogSelection<SelectionType>) : this(selection, true)

    init {
        initializeListeners()
    }

    protected abstract fun synchronizeSelectUi()

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
                if (isAttached) synchronizeSelectUi()
                dispatchSelectionChangedEvent(newValue, oldValue)
            }

            override fun onDraftSelectionChangedListener(newValue: SelectionType?, oldValue: SelectionType?) {
                if (synchronizeUiOnDraftChange && isAttached) synchronizeSelectUi()
            }

            override fun onDraftCommit(newValue: SelectionType?, beforeCommit: SelectionType?) {
                if (isAttached) synchronizeSelectUi()
                dispatchSelectionChangedEvent(newValue, beforeCommit)
            }

            override fun onDraftReverted() {
                if (isAttached) synchronizeSelectUi()
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