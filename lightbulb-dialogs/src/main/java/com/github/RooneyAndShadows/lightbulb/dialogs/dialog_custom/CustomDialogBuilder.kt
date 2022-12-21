package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.CustomDialogInflater
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

@Suppress("UNCHECKED_CAST")
class CustomDialogBuilder<DialogType : CustomDialog> @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String,
    private val dialogInitializer: CustomDialogInitializer<CustomDialog>,
    private val dialogInflater: CustomDialogInflater
) : BaseDialogBuilder<CustomDialog>(lifecycleOwner, dialogParentFragmentManager, dialogTag) {
    private var loading = false

    override fun withTitle(title: String): CustomDialogBuilder<CustomDialog> {
        return super.withTitle(title) as CustomDialogBuilder<CustomDialog>
    }

    override fun withMessage(message: String): CustomDialogBuilder<CustomDialog> {
        return super.withMessage(message) as CustomDialogBuilder<CustomDialog>
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): CustomDialogBuilder<CustomDialog> {
        return super.withPositiveButton(configuration, onClickListener) as CustomDialogBuilder<CustomDialog>
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): CustomDialogBuilder<CustomDialog> {
        return super.withNegativeButton(configuration, onClickListener) as CustomDialogBuilder<CustomDialog>
    }

    override fun withOnCancelListener(listener: DialogCancelListener): CustomDialogBuilder<CustomDialog> {
        return super.withOnCancelListener(listener) as CustomDialogBuilder<CustomDialog>
    }

    override fun withOnShowListener(listener: DialogShowListener): CustomDialogBuilder<CustomDialog> {
        return super.withOnShowListener(listener) as CustomDialogBuilder<CustomDialog>
    }

    override fun withOnHideListener(listener: DialogHideListener): CustomDialogBuilder<CustomDialog> {
        return super.withOnHideListener(listener) as CustomDialogBuilder<CustomDialog>
    }

    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): CustomDialogBuilder<CustomDialog> {
        return super.withCancelOnClickOutside(closeOnClickOutside) as CustomDialogBuilder<CustomDialog>
    }

    override fun withDialogType(dialogType: DialogTypes): CustomDialogBuilder<CustomDialog> {
        return super.withDialogType(dialogType) as CustomDialogBuilder<CustomDialog>
    }

    override fun withAnimations(animation: DialogAnimationTypes): CustomDialogBuilder<CustomDialog> {
        return super.withAnimations(animation) as CustomDialogBuilder<CustomDialog>
    }

    override fun withDialogListeners(listeners: DialogListeners): CustomDialogBuilder<CustomDialog> {
        return super.withDialogListeners(listeners) as CustomDialogBuilder<CustomDialog>
    }

    fun withLoading(isLoading: Boolean): CustomDialogBuilder<CustomDialog> {
        loading = isLoading
        return this
    }

    interface CustomDialogInitializer<DialogType : CustomDialog?> {
        fun initialize(
            title: String?,
            message: String?,
            positiveButtonConfiguration: DialogButtonConfiguration?,
            negativeButtonConfiguration: DialogButtonConfiguration?,
            cancelable: Boolean,
            loading: Boolean,
            dialogType: DialogTypes?,
            animationType: DialogAnimationTypes?
        ): DialogType
    }

    override fun buildDialog(): CustomDialog {
        var dialogFragment = dialogParentFragmentManager.findFragmentByTag(dialogTag) as DialogType?
        if (dialogFragment == null)
            dialogFragment = dialogInitializer.initialize(
                title, message, positiveButtonConfiguration, negativeButtonConfiguration,
                cancelableOnClickOutside, loading, dialogType, animation
            )
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogInflater(dialogInflater)
        dialogFragment.setDialogCallbacks(dialogListeners)
        dialogFragment.fragmentManager = dialogParentFragmentManager
        dialogFragment.setDialogTag(dialogTag)
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        return dialogFragment
    }
}