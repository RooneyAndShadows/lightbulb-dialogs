package com.github.rooneyandshadows.lightbulb.dialogsdemo

import android.content.Context
import android.graphics.drawable.Drawable
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter.ChipModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyItem

object Constants {
    const val DIALOG_POSITIVE_BUTTON_TAG = "DIALOG_POSITIVE_BUTTON_TAG"
    const val DIALOG_NEGATIVE_BUTTON_TAG = "DIALOG_NEGATIVE_BUTTON_TAG"
}

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
    return DialogButtonClickListener { buttonView, _ ->
        val context = buttonView!!.context
        val toastMessage = ResourceUtils.getPhrase(context, R.string.demo_positive_button_clicked_text)
        InteractionUtils.showMessage(context, toastMessage)
    }
}

fun getDefaultNegativeButtonClickListener(): DialogButtonClickListener {
    return DialogButtonClickListener { buttonView, _ ->
        val context = buttonView!!.context
        val toastMessage = ResourceUtils.getPhrase(context, R.string.demo_negative_button_clicked_text)
        InteractionUtils.showMessage(context, toastMessage)
    }
}

fun getDefaultPositiveButton(context: Context): DialogButton {
    return DialogButton(Constants.DIALOG_POSITIVE_BUTTON_TAG, getDefaultPositiveButtonText(context)).apply {
        addOnClickListener(getDefaultPositiveButtonClickListener())
    }
}

fun getDefaultNegativeButton(context: Context): DialogButton {
    return DialogButton(Constants.DIALOG_NEGATIVE_BUTTON_TAG, getDefaultNegativeButtonText(context)).apply {
        addOnClickListener(getDefaultNegativeButtonClickListener())
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
    models.add(ChipModel("Strength"))
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
    models.add(ChipModel("Strength"))
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
    models.add(ChipModel("Strength"))
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
    models.add(ChipModel("Strength"))
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
    models.add(ChipModel("Strength"))
    return models
}
