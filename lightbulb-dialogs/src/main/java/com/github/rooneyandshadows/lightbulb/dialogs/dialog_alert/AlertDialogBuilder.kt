package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

class AlertDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<AlertDialog>(
    lifecycleOwner,
    dialogParentFragmentManager,
    dialogTag
) {
    @Override
    override fun setupNonRetainableSettings(dialog: AlertDialog) {
    }

    @Override
    override fun setupRetainableSettings(dialog: AlertDialog) {
    }

    @Override
    override fun initializeNewDialog(): AlertDialog {
        return AlertDialog.newInstance()
    }

    @Override
    override fun withInitialDialogState(savedState: Bundle?): AlertDialogBuilder {
        return super.withInitialDialogState(savedState) as AlertDialogBuilder
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
        onClickListener: DialogButtonClickListener?,
    ): AlertDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as AlertDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener?,
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
}