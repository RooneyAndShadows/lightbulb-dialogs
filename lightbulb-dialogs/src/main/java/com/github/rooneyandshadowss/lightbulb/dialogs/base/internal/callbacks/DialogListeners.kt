package com.github.rooneyandshadowss.lightbulb.dialogs.base.internal.callbacks

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

interface DialogListeners {
    fun doOnCreate(dialog: BaseDialogFragment, dialogArguments: Bundle?, savedInstanceState: Bundle?)
    fun doOnInflated(dialog: BaseDialogFragment, layout: View?, savedInstanceState: Bundle?)
    fun doOnSaveInstanceState(dialog: BaseDialogFragment, layout: View?, outState: Bundle?)
}