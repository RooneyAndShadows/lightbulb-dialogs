package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*

@Suppress("unused")
class ColorPickerDialogBuilder @JvmOverloads constructor(
    dialogTag: String,
    dialogParentFragmentManager: FragmentManager,
    dialogLifecycleOwner: LifecycleOwner? = null,
    initialDialogState: Bundle? = null
) : BaseDialogBuilder<ColorPickerDialog>(dialogTag, dialogParentFragmentManager, dialogLifecycleOwner, initialDialogState) {
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var selection: IntArray? = null

    @JvmOverloads
    constructor(dialogTag: String, fragment: Fragment, initialDialogState: Bundle? = null) : this(
        dialogTag,
        fragment.childFragmentManager,
        fragment,
        initialDialogState
    )

    @JvmOverloads
    constructor(dialogTag: String, activity: FragmentActivity, initialDialogState: Bundle? = null) : this(
        dialogTag,
        activity.supportFragmentManager,
        activity,
        initialDialogState
    )

    @Override
    override fun setupNonRetainableSettings(dialog: ColorPickerDialog) {
        dialog.apply {
            changedCallback?.apply { addOnSelectionChangedListener(this) }
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: ColorPickerDialog) {
        dialog.apply {
            setSelection(selection)
        }
    }

    @Override
    override fun initializeNewDialog(): ColorPickerDialog {
        return ColorPickerDialog.newInstance()
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
    override fun withButton(configuration: DialogButton): ColorPickerDialogBuilder {
        return super.withButton(configuration) as ColorPickerDialogBuilder
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
}