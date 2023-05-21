package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
@JvmSuppressWildcards
abstract class BaseDialogBuilder<DialogType : BaseDialogFragment> @JvmOverloads constructor(
    protected val dialogLifecycleOwner: LifecycleOwner? = null,
    protected val dialogParentFragmentManager: FragmentManager,
    protected val dialogTag: String,
) {
    protected var initialDialogState: Bundle? = null
    protected var title: String? = null
    protected var message: String? = null
    protected var buttonConfigurations: List<DialogButton> = mutableListOf()
    protected var onShowListener: DialogShowListener? = null
    protected var onHideListener: DialogHideListener? = null
    protected var onCancelListener: DialogCancelListener? = null
    protected var animation: DialogAnimationTypes? = null
    protected var type: DialogTypes? = null
    protected var dialogListeners: DialogListeners? = null
    protected var cancelableOnClickOutside = true
    protected abstract fun setupNonRetainableSettings(dialog: DialogType)
    protected abstract fun setupRetainableSettings(dialog: DialogType)
    protected abstract fun initializeNewDialog(): DialogType

    open fun withInitialDialogState(savedState: Bundle?): BaseDialogBuilder<DialogType> {
        this.initialDialogState = savedState
        return this
    }

    open fun withTitle(title: String): BaseDialogBuilder<DialogType> {
        this.title = title
        return this
    }

    open fun withMessage(message: String): BaseDialogBuilder<DialogType> {
        this.message = message
        return this
    }

    open fun withButton(configuration: DialogButton): BaseDialogBuilder<DialogType> {
        val buttons = buttonConfigurations as MutableList<DialogButton>
        buttons.add(configuration)
        return this
    }

    open fun withOnCancelListener(listener: DialogCancelListener): BaseDialogBuilder<DialogType> {
        onCancelListener = listener
        return this
    }

    open fun withOnShowListener(listener: DialogShowListener): BaseDialogBuilder<DialogType> {
        onShowListener = listener
        return this
    }

    open fun withOnHideListener(listener: DialogHideListener): BaseDialogBuilder<DialogType> {
        onHideListener = listener
        return this
    }

    open fun withCancelOnClickOutside(closeOnClickOutside: Boolean): BaseDialogBuilder<DialogType> {
        cancelableOnClickOutside = closeOnClickOutside
        return this
    }

    open fun withDialogType(dialogType: DialogTypes): BaseDialogBuilder<DialogType> {
        this.type = dialogType
        return this
    }

    open fun withAnimations(animation: DialogAnimationTypes): BaseDialogBuilder<DialogType> {
        this.animation = animation
        return this
    }

    open fun withDialogListeners(listeners: DialogListeners): BaseDialogBuilder<DialogType> {
        dialogListeners = listeners
        return this
    }

    fun buildDialog(): DialogType {
        return getExistingDialogOrCreate().apply dialog@{
            setDialogTag(dialogTag)
            setLifecycleOwner(dialogLifecycleOwner)
            setParentFragManager(dialogParentFragmentManager)
            setDialogCallbacks(dialogListeners)
            buttonConfigurations.forEach { addDialogButton(it) }
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
            setupNonRetainableSettings(this)
        }
    }

    private fun getExistingDialogOrCreate(): DialogType {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as DialogType?
        return dialog ?: initializeNewDialog().apply dialogInstance@{
            initialDialogState?.apply {
                val ignoreManuallySavedState = getBoolean("IGNORE_MANUALLY_SAVED_STATE", false)
                if (!ignoreManuallySavedState) this@dialogInstance.restoreDialogState(this)
                return@dialogInstance
            }
            setDialogTitle(title)
            setDialogMessage(message)
            type?.apply { dialogType = this }
            animation?.apply { dialogAnimationType = this }
            isCancelable = cancelableOnClickOutside
            setupRetainableSettings(this)
        }
    }
}