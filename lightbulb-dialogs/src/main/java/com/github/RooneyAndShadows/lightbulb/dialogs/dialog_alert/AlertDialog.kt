package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes

class AlertDialog : BaseDialogFragment() {
    companion object {
        @JvmOverloads
        fun newInstance(
            title: String?,
            message: String?,
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            cancelable: Boolean = true,
            dialogType: DialogTypes = DialogTypes.NORMAL,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION
        ): AlertDialog {
            return AlertDialog().apply {
                arguments = DialogBundleHelper()
                    .withTitle(title)
                    .withMessage(message)
                    .withPositiveButtonConfig(positive)
                    .withNegativeButtonConfig(negative)
                    .withCancelable(cancelable)
                    .withShowing(false)
                    .withDialogType(dialogType)
                    .withAnimation(animationType)
                    .bundle
            }
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_alert, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
    }

    @Override
    override fun setupFullScreenDialog(dialogWindow: Window?, dialogLayout: View?) {
        super.setupFullScreenDialog(dialogWindow, dialogLayout)
        headerViewHierarchy.titleAndMessageContainer?.apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}