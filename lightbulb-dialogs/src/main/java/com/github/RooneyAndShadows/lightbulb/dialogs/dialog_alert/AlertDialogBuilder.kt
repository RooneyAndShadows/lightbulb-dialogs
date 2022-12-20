package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*

class AlertDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String
) : BaseDialogBuilder<AlertDialog>(lifecycleOwner, dialogParentFragmentManager, dialogTag) {

    @Override
    override fun withTitle(title: String): AlertDialogBuilder {
        return super.withTitle(title) as AlertDialogBuilder
    }

    @Override
    override fun withMessage(message: String): AlertDialogBuilder {
        return super.withMessage(message) as AlertDialogBuilder
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): AlertDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as AlertDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): AlertDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as AlertDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): AlertDialogBuilder {
        return super.withOnCancelListener(listener) as AlertDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): AlertDialogBuilder {
        return super.withOnShowListener(listener) as AlertDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): AlertDialogBuilder {
        return super.withOnHideListener(listener) as AlertDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): AlertDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as AlertDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): AlertDialogBuilder {
        return super.withDialogType(dialogType) as AlertDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): AlertDialogBuilder {
        return super.withAnimations(animation) as AlertDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): AlertDialogBuilder {
        return super.withDialogListeners(listeners) as AlertDialogBuilder
    }

    @Override
    override fun buildDialog(): AlertDialog {
        var alertDialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as AlertDialog?
        if (alertDialog == null)
            alertDialog = AlertDialog.newInstance(
                title,
                message,
                positiveButtonConfiguration,
                negativeButtonConfiguration,
                cancelableOnClickOutside,
                dialogType,
                animation
            ).apply {
                setLifecycleOwner(dialogLifecycleOwner)
                setParentFragManager(dialogParentFragmentManager)
                setDialogTag(dialogTag)
                if (onShowListener != null) addOnShowListener(onShowListener!!)
                if (onHideListener != null) addOnHideListener(onHideListener!!)
                if (onCancelListener != null) addOnCancelListener(onCancelListener!!)
                if (onNegativeClickListener != null) addOnNegativeClickListeners(onNegativeClickListener!!)
                if (onPositiveClickListener != null) addOnPositiveClickListener(onPositiveClickListener!!)
            }
        return alertDialog
    }
}