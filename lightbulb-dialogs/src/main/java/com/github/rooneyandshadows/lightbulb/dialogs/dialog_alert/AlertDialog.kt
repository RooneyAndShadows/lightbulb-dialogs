package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

class AlertDialog : BaseDialogFragment() {
    companion object {
        fun newInstance(): AlertDialog {
            return AlertDialog()
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_alert, null)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
    }

    @Override
    override fun setupFullScreenDialog(dialogWindow: Window, dialogLayout: View) {
        super.setupFullScreenDialog(dialogWindow, dialogLayout)
        dialogHeaderView?.titleAndMessageContainer?.apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}