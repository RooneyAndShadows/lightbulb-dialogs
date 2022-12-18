package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading

import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogShowListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogHideListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.DialogCallbacks
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import androidx.fragment.app.FragmentManager

class LoadingDialogBuilder : BaseDialogBuilder<LoadingDialog?> {
    constructor(manager: FragmentManager?, dialogTag: String?) : super(manager, dialogTag) {}
    constructor(lifecycleOwner: LifecycleOwner?, manager: FragmentManager?, dialogTag: String?) : super(
        lifecycleOwner,
        manager,
        dialogTag
    ) {
    }

    override fun withTitle(title: String?): LoadingDialogBuilder? {
        return super.withTitle(title) as LoadingDialogBuilder
    }

    override fun withMessage(message: String?): LoadingDialogBuilder? {
        return super.withMessage(message) as LoadingDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): LoadingDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as LoadingDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): LoadingDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as LoadingDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): LoadingDialogBuilder? {
        return super.withOnCancelListener(listener) as LoadingDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): LoadingDialogBuilder? {
        return super.withOnShowListener(listener) as LoadingDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): LoadingDialogBuilder? {
        return super.withOnHideListener(listener) as LoadingDialogBuilder
    }

    override fun withCancelOnClickOutsude(closeOnClickOutside: Boolean): LoadingDialogBuilder? {
        return super.withCancelOnClickOutsude(closeOnClickOutside) as LoadingDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): LoadingDialogBuilder? {
        return super.withDialogType(dialogType) as LoadingDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): LoadingDialogBuilder? {
        return super.withAnimations(animation) as LoadingDialogBuilder
    }

    override fun withDialogCallbacks(callbacks: DialogCallbacks?): LoadingDialogBuilder? {
        return super.withDialogCallbacks(callbacks) as LoadingDialogBuilder
    }

    override fun buildDialog(): LoadingDialog? {
        var dialogFragment = fragmentManager!!.findFragmentByTag(dialogTag) as LoadingDialog?
        if (dialogFragment == null) dialogFragment =
            LoadingDialog.Companion.newInstance(title, message, dialogType, animation)
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogCallbacks(dialogCallbacks)
        dialogFragment.setFragmentManager(fragmentManager)
        dialogFragment.setDialogTag(dialogTag)
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        return dialogFragment
    }
}