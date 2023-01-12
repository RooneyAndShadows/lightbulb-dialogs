package com.github.rooneyandshadows.lightbulb.dialogs.base

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

abstract class BaseDialogBuilder<DialogType : com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment?> @JvmOverloads constructor(
    protected val dialogLifecycleOwner: LifecycleOwner? = null,
    protected val dialogParentFragmentManager: FragmentManager,
    protected val dialogTag: String
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
    protected var animation: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION
    protected var type: DialogTypes = DialogTypes.NORMAL
    protected var dialogListeners: DialogListeners? = null
    protected var cancelableOnClickOutside = true

    open fun withTitle(title: String): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        this.title = title
        return this
    }

    open fun withMessage(message: String): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        this.message = message
        return this
    }

    open fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        this.positiveButtonConfiguration = configuration
        onPositiveClickListener = onClickListener
        return this
    }

    open fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        this.negativeButtonConfiguration = configuration
        onNegativeClickListener = onClickListener
        return this
    }

    open fun withOnCancelListener(listener: DialogCancelListener): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        onCancelListener = listener
        return this
    }

    open fun withOnShowListener(listener: DialogShowListener): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        onShowListener = listener
        return this
    }

    open fun withOnHideListener(listener: DialogHideListener): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        onHideListener = listener
        return this
    }

    open fun withCancelOnClickOutside(closeOnClickOutside: Boolean): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        cancelableOnClickOutside = closeOnClickOutside
        return this
    }

    open fun withDialogType(dialogType: DialogTypes): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        this.type = dialogType
        return this
    }

    open fun withAnimations(animation: DialogAnimationTypes): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        this.animation = animation
        return this
    }

    open fun withDialogListeners(listeners: DialogListeners): com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder<DialogType> {
        dialogListeners = listeners
        return this
    }

    abstract fun buildDialog(): DialogType
}