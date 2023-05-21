package com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks

import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

fun interface DialogButtonClickListener {
    fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment)
}