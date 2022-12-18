package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

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
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.CustomDialogInflater
import androidx.fragment.app.FragmentManager

class CustomDialogBuilder<DialogType : CustomDialog?> : BaseDialogBuilder<DialogType?> {
    private val dialogInitializer: CustomDialogInitializer<DialogType>
    private val dialogInflater: CustomDialogInflater
    private var loading = false

    constructor(
        lifecycleOwner: LifecycleOwner?,
        manager: FragmentManager?,
        dialogTag: String?,
        dialogInitializer: CustomDialogInitializer<DialogType>,
        dialogInflater: CustomDialogInflater
    ) : super(lifecycleOwner, manager, dialogTag) {
        this.dialogInitializer = dialogInitializer
        this.dialogInflater = dialogInflater
    }

    constructor(
        manager: FragmentManager?,
        dialogTag: String?,
        dialogInitializer: CustomDialogInitializer<DialogType>,
        dialogInflater: CustomDialogInflater
    ) : super(manager, dialogTag) {
        this.dialogInitializer = dialogInitializer
        this.dialogInflater = dialogInflater
    }

    override fun withTitle(title: String?): CustomDialogBuilder<DialogType?>? {
        return super.withTitle(title) as CustomDialogBuilder<DialogType?>
    }

    override fun withMessage(message: String?): CustomDialogBuilder<DialogType?>? {
        return super.withMessage(message) as CustomDialogBuilder<DialogType?>
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): CustomDialogBuilder<DialogType?>? {
        return super.withPositiveButton(configuration, onClickListener) as CustomDialogBuilder<DialogType?>
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): CustomDialogBuilder<DialogType?>? {
        return super.withNegativeButton(configuration, onClickListener) as CustomDialogBuilder<DialogType?>
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): CustomDialogBuilder<DialogType?>? {
        return super.withOnCancelListener(listener) as CustomDialogBuilder<DialogType?>
    }

    override fun withOnShowListener(listener: DialogShowListener?): CustomDialogBuilder<DialogType?>? {
        return super.withOnShowListener(listener) as CustomDialogBuilder<DialogType?>
    }

    override fun withOnHideListener(listener: DialogHideListener?): CustomDialogBuilder<DialogType?>? {
        return super.withOnHideListener(listener) as CustomDialogBuilder<DialogType?>
    }

    override fun withCancelOnClickOutsude(closeOnClickOutside: Boolean): CustomDialogBuilder<DialogType?>? {
        return super.withCancelOnClickOutsude(closeOnClickOutside) as CustomDialogBuilder<DialogType?>
    }

    override fun withDialogType(dialogType: DialogTypes?): CustomDialogBuilder<DialogType?>? {
        return super.withDialogType(dialogType) as CustomDialogBuilder<DialogType?>
    }

    override fun withAnimations(animation: DialogAnimationTypes?): CustomDialogBuilder<DialogType?>? {
        return super.withAnimations(animation) as CustomDialogBuilder<DialogType?>
    }

    override fun withDialogCallbacks(callbacks: DialogCallbacks?): CustomDialogBuilder<DialogType?>? {
        return super.withDialogCallbacks(callbacks) as CustomDialogBuilder<DialogType?>
    }

    fun withLoading(isLoading: Boolean): CustomDialogBuilder<DialogType> {
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

    override fun buildDialog(): DialogType? {
        var dialogFragment = fragmentManager!!.findFragmentByTag(dialogTag) as DialogType?
        if (dialogFragment == null) dialogFragment = dialogInitializer.initialize(
            title, message, positiveButtonConfiguration, negativeButtonConfiguration,
            cancelableOnClickOutside, loading, dialogType, animation
        )
        dialogFragment!!.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogInflater(dialogInflater)
        dialogFragment.setDialogCallbacks(dialogCallbacks)
        dialogFragment.fragmentManager = fragmentManager
        dialogFragment.setDialogTag(dialogTag)
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        return dialogFragment
    }
}