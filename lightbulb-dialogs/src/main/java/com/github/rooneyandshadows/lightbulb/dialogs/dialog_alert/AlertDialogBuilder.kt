package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialogBuilder

class AlertDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<AlertDialog>(lifecycleOwner,
    dialogParentFragmentManager,
    dialogTag) {

    @Override
    override fun withSavedState(savedState: Bundle?): AlertDialogBuilder {
        return super.withSavedState(savedState) as AlertDialogBuilder
    }

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
        onClickListener: DialogButtonClickListener,
    ): AlertDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as AlertDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
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
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): AlertDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as AlertDialog?
        return dialog ?: AlertDialog.newInstance().apply {
            if (savedState != null) {
                restoreDialogState(savedState)
                return@apply
            }
            dialogTitle = title
            dialogMessage = message
            dialogType = type
            dialogAnimationType = animation
            isCancelable = cancelableOnClickOutside
            dialogNegativeButton = negativeButtonConfiguration
            dialogPositiveButton = positiveButtonConfiguration
        }
    }
}