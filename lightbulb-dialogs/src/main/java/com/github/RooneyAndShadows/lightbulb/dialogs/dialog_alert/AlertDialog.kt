package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.rooneyandshadows.lightbulb.dialogs.R

class AlertDialog : BaseDialogFragment() {
    override fun setDialogLayout(inflater: LayoutInflater?): View {
        return View.inflate(context, R.layout.dialog_alert, null)
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {}
    override fun setupFullScreenDialog(dialogWindow: Window?, dialogLayout: View?) {
        super.setupFullScreenDialog(dialogWindow, dialogLayout)
        if (titleAndMessageContainer != null) titleAndMessageContainer.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        fun newInstance(
            title: String?, message: String?, positive: DialogButtonConfiguration?, negative: DialogButtonConfiguration?,
            cancelable: Boolean, dialogType: DialogTypes?, animationType: DialogAnimationTypes?
        ): AlertDialog {
            val dialogFragment = AlertDialog()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(dialogType ?: DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
                .bundle
            return dialogFragment
        }
    }
}