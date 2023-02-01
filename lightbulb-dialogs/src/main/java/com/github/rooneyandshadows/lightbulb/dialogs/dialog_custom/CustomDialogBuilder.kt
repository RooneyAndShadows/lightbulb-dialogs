package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.CustomDialogInflater
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialogBuilder

@Suppress("UNCHECKED_CAST", "unused")
class CustomDialogBuilder<DialogType : CustomDialog> @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    dialogParentFragmentManager: FragmentManager,
    dialogTag: String,
    private val dialogInitializer: CustomDialogInitializer<DialogType>,
    private val dialogInflater: CustomDialogInflater,
) : BaseDialogBuilder<CustomDialog>(lifecycleOwner, dialogParentFragmentManager, dialogTag) {
    private var loading = false

    @Override
    override fun setupNonRetainableSettings(dialog: CustomDialog) {
        dialog.apply {
            setDialogInflater(dialogInflater)
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: CustomDialog) {
        dialog.apply {
            setLoading(loading)
        }
    }

    @Override
    override fun initializeNewDialog(): CustomDialog {
        return dialogInitializer.initialize()
    }

    @Override
    override fun withInitialDialogState(savedState: Bundle?): CustomDialogBuilder<DialogType> {
        return super.withInitialDialogState(savedState) as CustomDialogBuilder<DialogType>
    }

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
        onClickListener: DialogButtonClickListener,
    ): CustomDialogBuilder<DialogType> {
        return super.withPositiveButton(configuration, onClickListener) as CustomDialogBuilder<DialogType>
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
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

    interface CustomDialogInitializer<DialogType : CustomDialog> {
        fun initialize(): DialogType
    }
}