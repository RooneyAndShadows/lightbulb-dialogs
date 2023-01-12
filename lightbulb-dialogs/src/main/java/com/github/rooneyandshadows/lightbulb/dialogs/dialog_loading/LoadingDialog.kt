package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.R

class LoadingDialog : BaseDialogFragment() {
    companion object {
        fun newInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        isCancelable = false
    }

    @Override
    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(false)
    }

    @Override
    override fun isCancelable(): Boolean {
        return false
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_loading_normal, null)
    }

    @Override
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
    }
}