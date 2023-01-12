package com.github.rooneyandshadowss.lightbulb.dialogs.base

import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener

abstract class BasePickerDialogFragment<SelectionType>(
    protected val selection: BaseDialogSelection<SelectionType>,
    private val synchronizeUiOnDraftChange: Boolean
) : BaseDialogFragment() {
    private var onSelectionChangedListener: SelectionChangedListener<SelectionType>? = null

    protected constructor(selection: BaseDialogSelection<SelectionType>) : this(selection, true)

    init {
        initializeListeners()
    }

    protected abstract fun synchronizeSelectUi()

    fun setOnSelectionChangedListener(onSelectionChangedListener: SelectionChangedListener<SelectionType>?) {
        this.onSelectionChangedListener = onSelectionChangedListener
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

    protected fun dispatchSelectionChangedEvent(oldValue: SelectionType?, newValue: SelectionType?) {
        if (onSelectionChangedListener != null) onSelectionChangedListener!!.onSelectionChanged(oldValue, newValue)
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
                synchronizeSelectUi()
                dispatchSelectionChangedEvent(oldValue, newValue)
            }

            override fun onDraftSelectionChangedListener(newValue: SelectionType?) {
                if (synchronizeUiOnDraftChange) synchronizeSelectUi()
            }

            override fun onDraftCommit(newValue: SelectionType?, beforeCommit: SelectionType?) {
                synchronizeSelectUi()
                dispatchSelectionChangedEvent(beforeCommit, newValue)
            }

            override fun onDraftReverted() {
                synchronizeSelectUi()
            }
        })
    }

    interface SelectionChangedListener<SType> {
        fun onSelectionChanged(oldValue: SType?, newValue: SType?)
    }
}