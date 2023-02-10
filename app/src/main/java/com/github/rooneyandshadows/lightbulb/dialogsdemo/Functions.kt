package com.github.rooneyandshadows.lightbulb.dialogsdemo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.commons.utils.DrawableUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.ChipModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyItem
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.AppIconUtils
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.CheckBoxSelectableAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.RadioButtonSelectableAdapter
import com.github.rooneyandshadows.lightbulb.textinputview.TextInputView

fun getHomeDrawable(context: Context): Drawable {
    return ShowMenuDrawable(context).apply {
        setEnabled(false)
        backgroundColor = ResourceUtils.getColorByAttribute(context, R.attr.colorError)
    }
}

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

fun getDefaultDialogTitle(context: Context): String {
    return ResourceUtils.getPhrase(context, R.string.demo_dialog_default_title_text)
}

fun getDefaultDialogMessage(context: Context): String {
    return ResourceUtils.getPhrase(context, R.string.demo_dialog_default_message_text)
}

fun getDefaultPositiveButtonText(context: Context): String {
    return ResourceUtils.getPhrase(context, R.string.demo_dialog_default_positive_button)
}

fun getDefaultNegativeButtonText(context: Context): String {
    return ResourceUtils.getPhrase(context, R.string.demo_dialog_default_negative_button)
}

fun getDefaultPositiveButtonClickListener(): DialogButtonClickListener {
    return object : DialogButtonClickListener {
        override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
            val context = buttonView!!.context
            val toastMessage = ResourceUtils.getPhrase(context, R.string.demo_positive_button_clicked_text)
            InteractionUtils.showMessage(context, toastMessage)
        }
    }
}

fun getDefaultNegativeButtonClickListener(): DialogButtonClickListener {
    return object : DialogButtonClickListener {
        override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
            val context = buttonView!!.context
            val toastMessage = ResourceUtils.getPhrase(context, R.string.demo_negative_button_clicked_text)
            InteractionUtils.showMessage(context, toastMessage)
        }
    }
}

fun <SelectionType> getDefaultSelectionChangedListener(context: Context): SelectionChangedListener<SelectionType> {
    return object : SelectionChangedListener<SelectionType> {
        override fun onSelectionChanged(
            dialog: BasePickerDialogFragment<SelectionType>,
            newValue: SelectionType?,
            oldValue: SelectionType?,
        ) {
            val toastMessage = ResourceUtils.getPhrase(context, R.string.demo_selection_changed_text)
            InteractionUtils.showMessage(context, toastMessage)
        }
    }
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
    view.addTextChangedCallback { _, _ ->
        bindingListener.onChange()
    }
}

fun getRadioButtonAdapter(): RadioButtonSelectableAdapter<DemoModel> {
    return object : RadioButtonSelectableAdapter<DemoModel>() {
        @Override
        override fun getItemIcon(context: Context, item: DemoModel): Drawable {
            return AppIconUtils.getIconWithAttributeColor(
                context,
                item.icon,
                R.attr.colorOnSurface,
                R.dimen.ICON_SIZE_RECYCLER_ITEM
            )
        }

        @Override
        override fun getItemIconBackground(context: Context, item: DemoModel): Drawable {
            return DrawableUtils.getRoundedCornersDrawable(
                item.iconBackgroundColor.color,
                ResourceUtils.dpToPx(100)
            )
        }
    }
}

fun getCheckboxAdapter(): CheckBoxSelectableAdapter<DemoModel> {
    return object : CheckBoxSelectableAdapter<DemoModel>() {
        @Override
        override fun getItemIcon(context: Context, item: DemoModel): Drawable {
            return AppIconUtils.getIconWithAttributeColor(
                context,
                item.icon,
                R.attr.colorOnSurface,
                R.dimen.ICON_SIZE_RECYCLER_ITEM
            )
        }

        @Override
        override fun getItemIconBackground(context: Context, item: DemoModel): Drawable {
            return DrawableUtils.getRoundedCornersDrawable(
                item.iconBackgroundColor.color,
                ResourceUtils.dpToPx(100)
            )
        }
    }
}

fun generateChips(): List<ChipModel> {
    val models: MutableList<ChipModel> = mutableListOf()
    models.add(ChipModel("Star"))
    models.add(ChipModel("Tag"))
    models.add(ChipModel("Search"))
    models.add(ChipModel("Block"))
    models.add(ChipModel("Center"))
    models.add(ChipModel("Right"))
    models.add(ChipModel("Cat"))
    models.add(ChipModel("Tree"))
    models.add(ChipModel("Person"))
    models.add(ChipModel("Generation"))
    models.add(ChipModel("Utility"))
    models.add(ChipModel("Category"))
    models.add(ChipModel("Label"))
    models.add(ChipModel("Side"))
    models.add(ChipModel("Section"))
    models.add(ChipModel("Page"))
    models.add(ChipModel("Class"))
    models.add(ChipModel("Type"))
    models.add(ChipModel("Performance"))
    models.add(ChipModel("Object"))
    models.add(ChipModel("Count"))
    models.add(ChipModel("Letter"))
    models.add(ChipModel("Subtitle"))
    models.add(ChipModel("Height"))
    models.add(ChipModel("Strenght"))
    models.add(ChipModel("Star"))
    models.add(ChipModel("Tag"))
    models.add(ChipModel("Search"))
    models.add(ChipModel("Block"))
    models.add(ChipModel("Center"))
    models.add(ChipModel("Right"))
    models.add(ChipModel("Cat"))
    models.add(ChipModel("Tree"))
    models.add(ChipModel("Person"))
    models.add(ChipModel("Generation"))
    models.add(ChipModel("Utility"))
    models.add(ChipModel("Category"))
    models.add(ChipModel("Label"))
    models.add(ChipModel("Side"))
    models.add(ChipModel("Section"))
    models.add(ChipModel("Page"))
    models.add(ChipModel("Class"))
    models.add(ChipModel("Type"))
    models.add(ChipModel("Performance"))
    models.add(ChipModel("Object"))
    models.add(ChipModel("Count"))
    models.add(ChipModel("Letter"))
    models.add(ChipModel("Subtitle"))
    models.add(ChipModel("Height"))
    models.add(ChipModel("Strenght"))
    models.add(ChipModel("Star"))
    models.add(ChipModel("Tag"))
    models.add(ChipModel("Search"))
    models.add(ChipModel("Block"))
    models.add(ChipModel("Center"))
    models.add(ChipModel("Right"))
    models.add(ChipModel("Cat"))
    models.add(ChipModel("Tree"))
    models.add(ChipModel("Person"))
    models.add(ChipModel("Generation"))
    models.add(ChipModel("Utility"))
    models.add(ChipModel("Category"))
    models.add(ChipModel("Label"))
    models.add(ChipModel("Side"))
    models.add(ChipModel("Section"))
    models.add(ChipModel("Page"))
    models.add(ChipModel("Class"))
    models.add(ChipModel("Type"))
    models.add(ChipModel("Performance"))
    models.add(ChipModel("Object"))
    models.add(ChipModel("Count"))
    models.add(ChipModel("Letter"))
    models.add(ChipModel("Subtitle"))
    models.add(ChipModel("Height"))
    models.add(ChipModel("Strenght"))
    models.add(ChipModel("Star"))
    models.add(ChipModel("Tag"))
    models.add(ChipModel("Search"))
    models.add(ChipModel("Block"))
    models.add(ChipModel("Center"))
    models.add(ChipModel("Right"))
    models.add(ChipModel("Cat"))
    models.add(ChipModel("Tree"))
    models.add(ChipModel("Person"))
    models.add(ChipModel("Generation"))
    models.add(ChipModel("Utility"))
    models.add(ChipModel("Category"))
    models.add(ChipModel("Label"))
    models.add(ChipModel("Side"))
    models.add(ChipModel("Section"))
    models.add(ChipModel("Page"))
    models.add(ChipModel("Class"))
    models.add(ChipModel("Type"))
    models.add(ChipModel("Performance"))
    models.add(ChipModel("Object"))
    models.add(ChipModel("Count"))
    models.add(ChipModel("Letter"))
    models.add(ChipModel("Subtitle"))
    models.add(ChipModel("Height"))
    models.add(ChipModel("Strenght"))
    models.add(ChipModel("Star"))
    models.add(ChipModel("Tag"))
    models.add(ChipModel("Search"))
    models.add(ChipModel("Block"))
    models.add(ChipModel("Center"))
    models.add(ChipModel("Right"))
    models.add(ChipModel("Cat"))
    models.add(ChipModel("Tree"))
    models.add(ChipModel("Person"))
    models.add(ChipModel("Generation"))
    models.add(ChipModel("Utility"))
    models.add(ChipModel("Category"))
    models.add(ChipModel("Label"))
    models.add(ChipModel("Side"))
    models.add(ChipModel("Section"))
    models.add(ChipModel("Page"))
    models.add(ChipModel("Class"))
    models.add(ChipModel("Type"))
    models.add(ChipModel("Performance"))
    models.add(ChipModel("Object"))
    models.add(ChipModel("Count"))
    models.add(ChipModel("Letter"))
    models.add(ChipModel("Subtitle"))
    models.add(ChipModel("Height"))
    models.add(ChipModel("Strenght"))
    return models
}
