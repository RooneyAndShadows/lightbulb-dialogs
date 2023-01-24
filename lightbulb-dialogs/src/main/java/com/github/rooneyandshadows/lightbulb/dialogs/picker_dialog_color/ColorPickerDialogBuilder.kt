package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

@Suppress("unused")
class ColorPickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<ColorPickerDialog>(lifecycleOwner, manager, dialogTag) {
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var selection: IntArray? = null

    @Override
    override fun withSavedState(savedState: Bundle?): ColorPickerDialogBuilder {
        return super.withSavedState(savedState) as ColorPickerDialogBuilder
    }

    @Override
    override fun withTitle(title: String): ColorPickerDialogBuilder {
        return super.withTitle(title) as ColorPickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): ColorPickerDialogBuilder {
        return super.withMessage(message) as ColorPickerDialogBuilder
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): ColorPickerDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as ColorPickerDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): ColorPickerDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as ColorPickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): ColorPickerDialogBuilder {
        return super.withOnCancelListener(listener) as ColorPickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): ColorPickerDialogBuilder {
        return super.withOnShowListener(listener) as ColorPickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): ColorPickerDialogBuilder {
        return super.withOnHideListener(listener) as ColorPickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): ColorPickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as ColorPickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): ColorPickerDialogBuilder {
        return super.withDialogType(dialogType) as ColorPickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): ColorPickerDialogBuilder {
        return super.withAnimations(animation) as ColorPickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): ColorPickerDialogBuilder {
        return super.withDialogListeners(listeners) as ColorPickerDialogBuilder
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray>): ColorPickerDialogBuilder {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): ColorPickerDialogBuilder {
        this.selection = selection
        return this
    }

    override fun buildDialog(): ColorPickerDialog {
        return getExistingDialogOrCreate().apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
            changedCallback?.apply { addOnSelectionChangedListener(this) }
        }
    }

    private fun getExistingDialogOrCreate(): ColorPickerDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as ColorPickerDialog?
        return dialog ?: ColorPickerDialog.newInstance().apply {
            if (savedState != null) {
                restoreDialogState(savedState)
                return@apply
            }
            dialogTitle = title
            dialogMessage = message
            dialogAnimationType = animation
            isCancelable = cancelableOnClickOutside
            dialogNegativeButton = negativeButtonConfiguration
            dialogPositiveButton = positiveButtonConfiguration
            setSelection(selection)
        }
    }
}