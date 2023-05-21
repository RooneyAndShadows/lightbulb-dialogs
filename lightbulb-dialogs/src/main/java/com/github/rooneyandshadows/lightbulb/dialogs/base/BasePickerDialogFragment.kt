package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection.PickerSelectionListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener

@Suppress("unused")
@JvmSuppressWildcards
abstract class BasePickerDialogFragment<SelectionType>(
    protected val dialogSelection: BaseDialogSelection<SelectionType>,
    private val synchronizeUiOnDraftChange: Boolean,
) : BaseDialogFragment() {
    private var onSelectionChangedListeners: MutableList<SelectionChangedListener<SelectionType>> = mutableListOf()

    protected constructor(selection: BaseDialogSelection<SelectionType>) : this(selection, true)

    init {
        initializeListeners()
    }

    companion object {
        private val SELECTION_STATE_KEY = "SELECTION_STATE_KEY"
    }

    protected abstract fun onSelectionChange(newSelection: SelectionType?)

    @Override
    override fun doOnSaveDialogProperties(outState: Bundle) {
        super.doOnSaveDialogProperties(outState)
        outState.apply {
            putBundle(SELECTION_STATE_KEY, dialogSelection.saveState())
        }
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        savedState.apply {
            getBundle(SELECTION_STATE_KEY)?.apply {
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

    class Buttons {
        companion object {
            private const val CANCEL_SELECTION_BUTTON_TAG = "CANCEL_SELECTION_BUTTON_TAG"
            private const val CONFIRM_SELECTION_BUTTON_TAG = "CONFIRM_SELECTION_BUTTON_TAG"

            @JvmStatic
            @JvmOverloads
            fun confirmSelectionButton(
                buttonText: String,
                onClick: DialogButtonClickListener? = null,
            ): DialogButton {
                return DialogButton(CONFIRM_SELECTION_BUTTON_TAG, buttonText).apply {
                    addOnClickListener { _, dialog ->
                        if (dialog !is BasePickerDialogFragment<*>) return@addOnClickListener
                        dialog.confirmSelection()
                    }
                    onClick?.apply { addOnClickListener(this) }
                }
            }

            @JvmStatic
            @JvmOverloads
            fun cancelSelectionButton(
                buttonText: String,
                onClick: DialogButtonClickListener? = null,
            ): DialogButton {
                return DialogButton(CANCEL_SELECTION_BUTTON_TAG, buttonText).apply {
                    addOnClickListener { _, dialog ->
                        if (dialog !is BasePickerDialogFragment<*>) return@addOnClickListener
                        dialog.cancelSelection()
                    }
                    onClick?.apply { addOnClickListener(this) }
                }
            }
        }
    }
}