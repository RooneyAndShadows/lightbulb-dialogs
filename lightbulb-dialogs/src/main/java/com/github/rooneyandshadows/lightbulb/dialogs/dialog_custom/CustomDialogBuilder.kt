package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.CustomDialogInflater

@Suppress("unused")
class CustomDialogBuilder<DialogType : CustomDialog> @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    private val dialogInitializer: CustomDialogInitializer<DialogType>,
    private val dialogInflater: CustomDialogInflater? = null,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null,
) : BaseDialogBuilder<DialogType>(dialogTag, dialogParentFragmentManager, dialogLifecycleOwner, initialDialogState) {
    private var loading = false

    @JvmOverloads
    constructor(
        dialogTag: String,
        fragment: Fragment,
        dialogInitializer: CustomDialogInitializer<DialogType>,
        dialogInflater: CustomDialogInflater? = null,
        initialDialogState: Bundle? = null
    ) : this(
        dialogTag,
        fragment.childFragmentManager,
        dialogInitializer,
        dialogInflater,
        fragment,
        initialDialogState
    )

    @JvmOverloads
    constructor(
        dialogTag: String,
        activity: FragmentActivity,
        dialogInitializer: CustomDialogInitializer<DialogType>,
        dialogInflater: CustomDialogInflater? = null,
        initialDialogState: Bundle? = null
    ) : this(
        dialogTag,
        activity.supportFragmentManager,
        dialogInitializer,
        dialogInflater,
        activity,
        initialDialogState
    )

    @Override
    override fun setupNonRetainableSettings(dialog: DialogType) {
        dialog.apply {
            setDialogInflater(dialogInflater)
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: DialogType) {
        dialog.apply {
            setLoading(loading)
        }
    }

    @Override
    override fun initializeNewDialog(): DialogType {
        return dialogInitializer.initialize()
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
    override fun withButton(configuration: DialogButton): CustomDialogBuilder<DialogType> {
        return super.withButton(configuration) as CustomDialogBuilder<DialogType>
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

    fun interface CustomDialogInitializer<out DialogType : CustomDialog> {
        fun initialize(): DialogType
    }
}