package com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

fun interface DialogShowListener {
    fun doOnShow(dialogFragment: BaseDialogFragment)
}