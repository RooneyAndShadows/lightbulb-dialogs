package com.github.rooneyandshadows.lightbulb.dialogsdemo

import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.adapter.DialogPropertyItem

fun DialogTypes.Companion.getAllAsDialogPropertyItems(): Array<DialogPropertyItem> {
    return DialogTypes.values().asList()
        .map { return@map DialogPropertyItem(it.name, it.value) }
        .toTypedArray()
}

fun DialogAnimationTypes.Companion.getAllAsDialogPropertyItems(): Array<DialogPropertyItem> {
    return DialogTypes.values().asList()
        .map { return@map DialogPropertyItem(it.name, it.value) }
        .toTypedArray()
}