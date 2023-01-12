package com.github.rooneyandshadowss.lightbulb.dialogs.base.internal.callbacks

import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

interface DialogButtonClickListener {
    fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment)
}