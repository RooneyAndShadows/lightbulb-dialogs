package com.github.rooneyandshadows.lightbulb.dialogsdemo.dialogs

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog

class DemoCustomDialog : CustomDialog() {

    init {

    }

    companion object {
        fun newInstance(): DemoCustomDialog {
            return DemoCustomDialog()
        }
    }

    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        super.doOnCreate(dialogArguments, savedInstanceState)

    }

    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        super.setupDialogContent(view, savedInstanceState)
    }
}