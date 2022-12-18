package com.github.rooneyandshadows.lightbulb.dialogs.base

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment.*

abstract class BaseDialogBuilder<DialogType : BaseDialogFragment?>(
    protected val dialogLifecycleOwner: LifecycleOwner?,
    protected val fragmentManager: FragmentManager?,
    protected val dialogTag: String?
) {
    protected var title: String? = null
    protected var message: String? = null
    protected var positiveButtonConfiguration: DialogButtonConfiguration? = null
    protected var negativeButtonConfiguration: DialogButtonConfiguration? = null
    protected var onPositiveClickListener: DialogButtonClickListener? = null
    protected var onNegativeClickListener: DialogButtonClickListener? = null
    protected var onShowListener: DialogShowListener? = null
    protected var onHideListener: DialogHideListener? = null
    protected var onCancelListener: DialogCancelListener? = null
    protected var animation: DialogAnimationTypes? = null
    protected var dialogType: DialogTypes? = null
    protected var dialogCallbacks: DialogCallbacks? = null
    protected var cancelableOnClickOutside = true

    constructor(manager: FragmentManager?, dialogTag: String?) : this(null, manager, dialogTag)

    open fun withTitle(title: String?): BaseDialogBuilder<DialogType> {
        this.title = title
        return this
    }

    open fun withMessage(message: String?): BaseDialogBuilder<DialogType> {
        this.message = message
        return this
    }

    open fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): BaseDialogBuilder<DialogType> {
        this.positiveButtonConfiguration = configuration
        onPositiveClickListener = onClickListener
        return this
    }

    open fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): BaseDialogBuilder<DialogType> {
        this.negativeButtonConfiguration = configuration
        onNegativeClickListener = onClickListener
        return this
    }

    open fun withOnCancelListener(listener: DialogCancelListener?): BaseDialogBuilder<DialogType> {
        onCancelListener = listener
        return this
    }

    open fun withOnShowListener(listener: DialogShowListener?): BaseDialogBuilder<DialogType> {
        onShowListener = listener
        return this
    }

    open fun withOnHideListener(listener: DialogHideListener?): BaseDialogBuilder<DialogType> {
        onHideListener = listener
        return this
    }

    open fun withCancelOnClickOutsude(closeOnClickOutside: Boolean): BaseDialogBuilder<DialogType> {
        cancelableOnClickOutside = closeOnClickOutside
        return this
    }

    open fun withDialogType(dialogType: DialogTypes?): BaseDialogBuilder<DialogType> {
        this.dialogType = dialogType
        return this
    }

    open fun withAnimations(animation: DialogAnimationTypes?): BaseDialogBuilder<DialogType> {
        this.animation = animation
        return this
    }

    open fun withDialogCallbacks(callbacks: DialogCallbacks?): BaseDialogBuilder<DialogType> {
        dialogCallbacks = callbacks
        return this
    }

    abstract fun buildDialog(): DialogType
}