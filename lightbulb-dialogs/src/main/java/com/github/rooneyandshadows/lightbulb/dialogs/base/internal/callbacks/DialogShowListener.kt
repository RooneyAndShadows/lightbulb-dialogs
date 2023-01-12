package com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

interface DialogShowListener {
    fun doOnShow(dialogFragment: BaseDialogFragment)
}