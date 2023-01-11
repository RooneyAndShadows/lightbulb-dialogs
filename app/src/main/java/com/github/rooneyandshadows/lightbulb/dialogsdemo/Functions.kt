package com.github.rooneyandshadows.lightbulb.dialogsdemo

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyItem
import com.github.rooneyandshadows.lightbulb.textinputview.TextInputView
import java.util.*

fun DialogTypes.Companion.getAllAsDialogPropertyItems(): Array<DialogPropertyItem> {
    return DialogTypes.values().asList()
        .map { return@map DialogPropertyItem(it.name, it.value) }
        .toTypedArray()
}

fun DialogAnimationTypes.Companion.getAllAsDialogPropertyItems(): Array<DialogPropertyItem> {
    return DialogAnimationTypes.values().asList()
        .map { return@map DialogPropertyItem(it.name, it.value) }
        .toTypedArray()
}

@InverseBindingAdapter(attribute = "dialogButtonText", event = "dialogButtonTextChanged")
fun getSelectedValue(view: TextInputView): DialogButtonConfiguration? {
    if (view.text == "") return null
    return DialogButtonConfiguration(view.text, true, true)
}

@BindingAdapter(value = ["dialogButtonText"])
fun setPickerSelection(view: TextInputView, newButtonConfiguration: DialogButtonConfiguration?) {
    val newButtonTitle = newButtonConfiguration?.buttonTitle
    if (newButtonTitle == null) {
        view.text = ""
        return
    }
    if (view.text == newButtonTitle) return
    view.text = newButtonTitle
}

@BindingAdapter(value = ["dialogButtonTextChanged"], requireAll = false)
fun bindPickerEvent(view: TextInputView, bindingListener: InverseBindingListener) {
    view.addTextChangedCallback { newValue, oldValue ->
        bindingListener.onChange()
    }
}