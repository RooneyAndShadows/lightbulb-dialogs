package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.github.rooneyandshadows.lightbulb.commons.utils.KeyboardUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R


class ChipsFilterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var filterInput: AppCompatEditText? = null
    private var addButton: AppCompatImageButton? = null
    private val adapter: ChipsPickerAdapter
        get() = (dialog!!.adapter as ChipsPickerAdapter)
    var dialog: ChipsPickerDialog? = null
        set(value) {
            field = value
            filterInput?.onFocusChangeListener =
                OnFocusChangeListener { v, hasFocus -> if (!hasFocus) KeyboardUtils.hideKeyboard(filterInput) }
            filterInput?.doOnTextChanged { text, _, _, _ ->
                adapter.filter.filter(text)
                handleAddButtonVisibillity()
            }
            addButton?.setOnClickListener {
                if (!allowAddition) return@setOnClickListener
                val currentQuery = filterInput?.text.toString()
                if (currentQuery.isBlank()) return@setOnClickListener
                adapter.addItem(ChipsPickerAdapter.ChipModel(currentQuery))
                filterInput?.setText("")
                handleAddButtonVisibillity()
            }
        }
    var allowAddition: Boolean = false

    init {
        isSaveEnabled = true
        renderLayout()
        handleAddButtonVisibillity()
        val customTransition = LayoutTransition()
        customTransition.enableTransitionType(LayoutTransition.CHANGING)
        customTransition.enableTransitionType(LayoutTransition.APPEARING)
        customTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
        customTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING)
        customTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)
        layoutTransition = customTransition;
    }

    private fun renderLayout() {
        inflate(context, R.layout.chip_filter_layot, this)
        filterInput = findViewById(R.id.dialogPickerFilter)
        addButton = findViewById(R.id.addButton)
        val padding = ResourceUtils.getDimenById(context, R.dimen.spacing_size_medium).toInt()
        setPadding(padding, 0, padding, 0)
    }

    private fun handleAddButtonVisibillity() {
        if (allowAddition) {
            val currentQuery = filterInput?.text
            if (!currentQuery.isNullOrBlank() && !adapter.hasItemWithName(currentQuery.toString()))
                addButton?.visibility = VISIBLE
            else addButton?.visibility = GONE
        } else {
            addButton?.visibility = GONE
        }
    }
}