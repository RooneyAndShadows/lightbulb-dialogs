package com.github.rooneyandshadowss.lightbulb.dialogs.base.internal.callbacks

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

interface DialogCancelListener {
    fun doOnCancel(dialogFragment: BaseDialogFragment)
}