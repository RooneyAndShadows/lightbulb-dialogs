package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

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

class AlertDialogBuilder : BaseDialogBuilder<AlertDialog?> {
    constructor(lifecycleOwner: LifecycleOwner?, manager: FragmentManager?, dialogTag: String?) : super(
        lifecycleOwner,
        manager,
        dialogTag
    ) {
    }

    constructor(manager: FragmentManager?, dialogTag: String?) : super(manager, dialogTag) {}

    override fun withTitle(title: String?): AlertDialogBuilder? {
        return super.withTitle(title) as AlertDialogBuilder
    }

    override fun withMessage(message: String?): AlertDialogBuilder? {
        return super.withMessage(message) as AlertDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): AlertDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as AlertDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): AlertDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as AlertDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): AlertDialogBuilder? {
        return super.withOnCancelListener(listener) as AlertDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): AlertDialogBuilder? {
        return super.withOnShowListener(listener) as AlertDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): AlertDialogBuilder? {
        return super.withOnHideListener(listener) as AlertDialogBuilder
    }

    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): AlertDialogBuilder? {
        return super.withCancelOnClickOutside(closeOnClickOutside) as AlertDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): AlertDialogBuilder? {
        return super.withDialogType(dialogType) as AlertDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): AlertDialogBuilder? {
        return super.withAnimations(animation) as AlertDialogBuilder
    }

    override fun withDialogListeners(callbacks: DialogCallbacks?): AlertDialogBuilder? {
        return super.withDialogListeners(callbacks) as AlertDialogBuilder
    }

    override fun buildDialog(): AlertDialog? {
        var alertDialog = fragmentManager!!.findFragmentByTag(dialogTag) as AlertDialog?
        if (alertDialog == null) alertDialog = AlertDialog.Companion.newInstance(
            title,
            message,
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            cancelableOnClickOutside,
            dialogType,
            animation
        )
        alertDialog.setLifecycleOwner(dialogLifecycleOwner)
        alertDialog.setFragmentManager(fragmentManager)
        alertDialog.setDialogTag(dialogTag)
        alertDialog.addOnShowListener(onShowListener)
        alertDialog.addOnHideListener(onHideListener)
        alertDialog.addOnCancelListener(onCancelListener)
        alertDialog.addOnNegativeClickListeners(onNegativeClickListener)
        alertDialog.addOnPositiveClickListener(onPositiveClickListener)
        return alertDialog
    }
}