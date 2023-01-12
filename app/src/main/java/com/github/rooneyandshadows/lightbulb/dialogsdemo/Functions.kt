package com.github.rooneyandshadows.lightbulb.dialogsdemo

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.rooneyandshadows.lightbulb.commons.utils.DrawableUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyItem
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.AppIconUtils
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.CheckBoxSelectableAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.RadioButtonSelectableAdapter
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

fun getRadioButtonAdapter(context: Context): RadioButtonSelectableAdapter<DemoModel> {
    return object : RadioButtonSelectableAdapter<DemoModel>() {
        @Override
        override fun getItemIcon(item: DemoModel): Drawable {
            return AppIconUtils.getIconWithAttributeColor(
                context,
                item.icon,
                R.attr.colorOnSurface,
                R.dimen.ICON_SIZE_RECYCLER_ITEM)
        }

        @Override
        override fun getItemIconBackground(item: DemoModel): Drawable {
            return DrawableUtils.getRoundedCornersDrawable(
                item.iconBackgroundColor.color,
                ResourceUtils.dpToPx(100)
            )
        }
    }
}

fun getCheckboxAdapter(context: Context): CheckBoxSelectableAdapter<DemoModel> {
    return object : CheckBoxSelectableAdapter<DemoModel>() {
        override fun getItemIcon(item: DemoModel): Drawable {
            return AppIconUtils.getIconWithAttributeColor(
                context,
                item.icon,
                R.attr.colorOnSurface,
                R.dimen.ICON_SIZE_RECYCLER_ITEM
            )
        }

        override fun getItemIconBackground(item: DemoModel): Drawable {
            return DrawableUtils.getRoundedCornersDrawable(
                item.iconBackgroundColor.color,
                ResourceUtils.dpToPx(100)
            )
        }
    }
}