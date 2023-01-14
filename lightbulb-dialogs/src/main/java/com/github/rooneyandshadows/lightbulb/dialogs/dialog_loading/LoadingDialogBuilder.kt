package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder

class LoadingDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<LoadingDialog?>(lifecycleOwner, dialogParentFragmentManager, dialogTag) {

    @Override
    override fun withSavedState(savedState: Bundle): LoadingDialogBuilder {
        return super.withSavedState(savedState) as LoadingDialogBuilder
    }

    @Override
    override fun withTitle(title: String): LoadingDialogBuilder {
        return super.withTitle(title) as LoadingDialogBuilder
    }

    @Override
    override fun withMessage(message: String): LoadingDialogBuilder {
        return super.withMessage(message) as LoadingDialogBuilder
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): LoadingDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as LoadingDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): LoadingDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as LoadingDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): LoadingDialogBuilder {
        return super.withOnCancelListener(listener) as LoadingDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): LoadingDialogBuilder {
        return super.withOnShowListener(listener) as LoadingDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): LoadingDialogBuilder {
        return super.withOnHideListener(listener) as LoadingDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): LoadingDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as LoadingDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): LoadingDialogBuilder {
        return super.withDialogType(dialogType) as LoadingDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): LoadingDialogBuilder {
        return super.withAnimations(animation) as LoadingDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): LoadingDialogBuilder {
        return super.withDialogListeners(listeners) as LoadingDialogBuilder
    }

    @Override
    override fun buildDialog(): LoadingDialog {
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): LoadingDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as LoadingDialog?
        return dialog ?: LoadingDialog.newInstance().apply {
            restoreDialogState(savedState)
            if (savedState != null) return@apply
            dialogTitle = title
            dialogMessage = message
            dialogType = type
            dialogAnimationType = animation
        }
    }
}