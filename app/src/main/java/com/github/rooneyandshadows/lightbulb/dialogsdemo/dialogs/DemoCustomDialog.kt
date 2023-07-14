package com.github.rooneyandshadows.lightbulb.dialogsdemo.dialogs

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R

class DemoCustomDialog : CustomDialog() {
    override val contentLayoutId: Int
        get() = R.layout.dialog_demo_custom

    companion object {
        fun newInstance(): DemoCustomDialog {
            return DemoCustomDialog()
        }
    }

    override fun setupCustomDialogContent(view: View, savedInstanceState: Bundle?) {
        super.setupCustomDialogContent(view, savedInstanceState)
    }
}