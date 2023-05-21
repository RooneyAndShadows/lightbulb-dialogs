package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

@Suppress("unused")
class IconPickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
) : BaseDialogBuilder<IconPickerDialog>(lifecycleOwner, manager, dialogTag) {
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var selection: IntArray? = null

    @Override
    override fun setupNonRetainableSettings(dialog: IconPickerDialog) {
        dialog.apply {
            changedCallback?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: IconPickerDialog) {
        dialog.apply {
            setSelection(selection)
        }
    }

    @Override
    override fun initializeNewDialog(): IconPickerDialog {
        return IconPickerDialog.newInstance()
    }

    @Override
    override fun withInitialDialogState(savedState: Bundle?): IconPickerDialogBuilder {
        return super.withInitialDialogState(savedState) as IconPickerDialogBuilder
    }

    @Override
    override fun withTitle(title: String): IconPickerDialogBuilder {
        return super.withTitle(title) as IconPickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): IconPickerDialogBuilder {
        return super.withMessage(message) as IconPickerDialogBuilder
    }

    @Override
    override fun withButton(configuration: DialogButton): IconPickerDialogBuilder {
        return super.withButton(configuration) as IconPickerDialogBuilder
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): IconPickerDialogBuilder {
        return super.withOnCancelListener(listener) as IconPickerDialogBuilder
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): IconPickerDialogBuilder {
        return super.withOnShowListener(listener) as IconPickerDialogBuilder
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): IconPickerDialogBuilder {
        return super.withOnHideListener(listener) as IconPickerDialogBuilder
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): IconPickerDialogBuilder {
        return super.withCancelOnClickOutside(closeOnClickOutside) as IconPickerDialogBuilder
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): IconPickerDialogBuilder {
        return super.withDialogType(dialogType) as IconPickerDialogBuilder
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): IconPickerDialogBuilder {
        return super.withAnimations(animation) as IconPickerDialogBuilder
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): IconPickerDialogBuilder {
        return super.withDialogListeners(listeners) as IconPickerDialogBuilder
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray>): IconPickerDialogBuilder {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): IconPickerDialogBuilder {
        this.selection = selection
        return this
    }
}