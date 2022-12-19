package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.R

class LoadingDialog : BaseDialogFragment() {
    override fun setDialogLayout(inflater: LayoutInflater?): View {
        return View.inflate(context, R.layout.dialog_loading_normal, null)
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {}

    companion object {
        fun newInstance(
            title: String?,
            message: String?,
            dialogType: DialogTypes?,
            animationType: DialogAnimationTypes?
        ): LoadingDialog {
            val dialogFragment = LoadingDialog()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withCancelable(false)
                .withShowing(false)
                .withDialogType(dialogType ?: DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
                .bundle
            return dialogFragment
        }
    }
}