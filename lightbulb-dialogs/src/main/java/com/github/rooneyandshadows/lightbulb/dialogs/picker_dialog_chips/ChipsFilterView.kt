package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.github.rooneyandshadows.lightbulb.commons.utils.KeyboardUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.ChipModel


class ChipsFilterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    private var filterInput: AppCompatEditText? = null
    private var addButton: AppCompatImageButton? = null
    private var allowAddition: Boolean = false
    private var onOptionCreatedListener: OnOptionCreatedListener? = null
    private val adapter: ChipsPickerAdapter
        get() = (dialog!!.adapter as ChipsPickerAdapter)
    var dialog: ChipsPickerDialog? = null
        set(value) {
            field = value
            filterInput?.onFocusChangeListener =
                OnFocusChangeListener { _, hasFocus -> if (!hasFocus) KeyboardUtils.hideKeyboard(filterInput) }
            filterInput?.doOnTextChanged { text, _, _, _ ->
                adapter.filter.filter(text)
                handleAddButtonVisibillity()
            }
            addButton?.setOnClickListener {
                if (!allowAddition) return@setOnClickListener
                val currentQuery = filterInput?.text.toString()
                if (currentQuery.isBlank()) return@setOnClickListener
                val newChip = ChipModel(currentQuery)
                adapter.addItem(ChipModel(currentQuery))
                filterInput?.setText("")
                onOptionCreatedListener?.execute(newChip)
                handleAddButtonVisibillity()
            }
        }

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
        layoutTransition = customTransition
    }

    private fun renderLayout() {
        inflate(context, R.layout.chip_filter_layot, this)
        filterInput = findViewById(R.id.dialogPickerFilter)
        addButton = findViewById(R.id.addButton)
        setupSizes()
    }

    fun setOnOptionCreatedListener(onOptionCreatedListener: OnOptionCreatedListener?) {
        this.onOptionCreatedListener = onOptionCreatedListener
    }

    fun setAllowAddition(newValue: Boolean) {
        allowAddition = newValue
        addButton?.apply {
            if (!newValue && isVisible) isVisible = false
        }
    }

    fun setHintText(hintText: String) {
        filterInput?.hint = hintText
    }

    private fun setupSizes() {
        val padding = ResourceUtils.getDimenById(context, R.dimen.spacing_size_medium).toInt()
        setPadding(padding, 0, padding, 0)
        val widthMeasureSpec = makeMeasureSpec(0, UNSPECIFIED)
        val heightMeasureSpec = makeMeasureSpec(0, UNSPECIFIED)
        this.measure(widthMeasureSpec, heightMeasureSpec)
        addButton!!.layoutParams.width = filterInput!!.measuredHeight
        addButton!!.layoutParams.height = filterInput!!.measuredHeight
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

    interface OnOptionCreatedListener {
        fun execute(option: ChipModel)
    }
}