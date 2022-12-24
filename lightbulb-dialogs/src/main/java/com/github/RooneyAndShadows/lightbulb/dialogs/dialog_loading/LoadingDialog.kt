package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes

class LoadingDialog : BaseDialogFragment() {
    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_loading_normal, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
    }

    companion object {
        fun newInstance(
            title: String?,
            message: String?,
            dialogType: DialogTypes = DialogTypes.NORMAL,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION
        ): LoadingDialog {
            val dialogFragment = LoadingDialog()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withCancelable(false)
                .withShowing(false)
                .withDialogType(dialogType)
                .withAnimation(animationType)
                .bundle
            return dialogFragment
        }
    }
}