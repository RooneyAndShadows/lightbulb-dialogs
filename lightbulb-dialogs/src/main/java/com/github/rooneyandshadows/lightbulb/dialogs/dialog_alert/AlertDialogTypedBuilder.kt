package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogCancelListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogHideListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogListeners
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener

class AlertDialogTypedBuilder<DialogType : AlertDialog> @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    private val dialogInitializer: AlertDialogInitializer<DialogType>,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null
) : BaseDialogBuilder<DialogType>(
    dialogTag,
    dialogParentFragmentManager,
    dialogLifecycleOwner,
    initialDialogState
) {

    @JvmOverloads
    constructor(
        dialogTag: String,
        fragment: Fragment,
        dialogInitializer: AlertDialogInitializer<DialogType>,
        initialDialogState: Bundle? = null
    ) : this(
        dialogTag,
        fragment.childFragmentManager,
        dialogInitializer,
        fragment,
        initialDialogState
    )

    @JvmOverloads
    constructor(
        dialogTag: String,
        activity: FragmentActivity,
        dialogInitializer: AlertDialogInitializer<DialogType>,
        initialDialogState: Bundle? = null
    ) : this(
        dialogTag,
        activity.supportFragmentManager,
        dialogInitializer,
        activity,
        initialDialogState
    )

    @Override
    override fun setupNonRetainableSettings(dialog: DialogType) {
    }

    @Override
    override fun setupRetainableSettings(dialog: DialogType) {
    }

    @Override
    override fun initializeNewDialog(): DialogType {
        return dialogInitializer.initialize()
    }

    @Override
    override fun withTitle(title: String): AlertDialogTypedBuilder<DialogType> {
        return super.withTitle(title) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withMessage(message: String): AlertDialogTypedBuilder<DialogType> {
        return super.withMessage(message) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withButton(configuration: DialogButton): AlertDialogTypedBuilder<DialogType> {
        return super.withButton(configuration) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): AlertDialogTypedBuilder<DialogType> {
        return super.withOnCancelListener(listener) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): AlertDialogTypedBuilder<DialogType> {
        return super.withOnShowListener(listener) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): AlertDialogTypedBuilder<DialogType> {
        return super.withOnHideListener(listener) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): AlertDialogTypedBuilder<DialogType> {
        return super.withCancelOnClickOutside(closeOnClickOutside) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): AlertDialogTypedBuilder<DialogType> {
        return super.withDialogType(dialogType) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): AlertDialogTypedBuilder<DialogType> {
        return super.withAnimations(animation) as AlertDialogTypedBuilder<DialogType>
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): AlertDialogTypedBuilder<DialogType> {
        return super.withDialogListeners(listeners) as AlertDialogTypedBuilder<DialogType>
    }

    fun interface AlertDialogInitializer<DialogType : AlertDialog> {
        fun initialize(): DialogType
    }
}