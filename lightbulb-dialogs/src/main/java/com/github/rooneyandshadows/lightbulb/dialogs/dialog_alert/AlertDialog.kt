package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

open class AlertDialog : BaseDialogFragment() {
    companion object {
        fun newInstance(): AlertDialog {
            return AlertDialog()
        }
    }

    @Override
    final override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_alert, null)
    }

    @Override
    final override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
    }

    @Override
    final override fun setupFullScreenDialog(dialogWindow: Window, dialogLayout: View) {
        super.setupFullScreenDialog(dialogWindow, dialogLayout)
        val headingLayoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        dialogHeaderView?.titleAndMessageContainer?.layoutParams = headingLayoutParams
    }
}