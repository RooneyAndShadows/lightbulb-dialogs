package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.CustomDialogInflater
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialog

@Suppress("UNCHECKED_CAST")
class CustomDialogBuilder<DialogType : CustomDialog> @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String,
    private val dialogInitializer: CustomDialogInitializer<DialogType>,
    private val dialogInflater: CustomDialogInflater
) : BaseDialogBuilder<CustomDialog>(lifecycleOwner, dialogParentFragmentManager, dialogTag) {
    private var loading = false

    @Override
    override fun withTitle(title: String): CustomDialogBuilder<DialogType> {
        return super.withTitle(title) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withMessage(message: String): CustomDialogBuilder<DialogType> {
        return super.withMessage(message) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): CustomDialogBuilder<DialogType> {
        return super.withPositiveButton(configuration, onClickListener) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): CustomDialogBuilder<DialogType> {
        return super.withNegativeButton(configuration, onClickListener) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): CustomDialogBuilder<DialogType> {
        return super.withOnCancelListener(listener) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): CustomDialogBuilder<DialogType> {
        return super.withOnShowListener(listener) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): CustomDialogBuilder<DialogType> {
        return super.withOnHideListener(listener) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): CustomDialogBuilder<DialogType> {
        return super.withCancelOnClickOutside(closeOnClickOutside) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): CustomDialogBuilder<DialogType> {
        return super.withDialogType(dialogType) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): CustomDialogBuilder<DialogType> {
        return super.withAnimations(animation) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): CustomDialogBuilder<DialogType> {
        return super.withDialogListeners(listeners) as CustomDialogBuilder<DialogType>
    }

    fun withLoading(isLoading: Boolean): CustomDialogBuilder<DialogType> {
        loading = isLoading
        return this
    }

    @Override
    override fun buildDialog(): CustomDialog {
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogInflater(dialogInflater)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): DialogType {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as DialogType?
        return dialog ?: dialogInitializer.initialize().apply {
            dialogTitle = title
            dialogMessage = message
            dialogType = type
            dialogAnimationType = animation
            isCancelable = cancelableOnClickOutside
            dialogNegativeButton = negativeButtonConfiguration
            dialogPositiveButton = positiveButtonConfiguration
            isLoading = loading
        }
    }

    interface CustomDialogInitializer<DialogType : CustomDialog> {
        fun initialize(): DialogType
    }
}