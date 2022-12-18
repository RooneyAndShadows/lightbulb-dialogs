package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color

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
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager

class ColorPickerDialogBuilder : BaseDialogBuilder<ColorPickerDialog?> {
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var selection: IntArray
    private val adapter: ColorPickerAdapter

    constructor(manager: FragmentManager?, dialogTag: String?, adapter: ColorPickerAdapter) : super(manager, dialogTag) {
        this.adapter = adapter
    }

    constructor(
        lifecycleOwner: LifecycleOwner?,
        manager: FragmentManager?,
        dialogTag: String?,
        adapter: ColorPickerAdapter
    ) : super(lifecycleOwner, manager, dialogTag) {
        this.adapter = adapter
    }

    override fun withTitle(title: String?): ColorPickerDialogBuilder? {
        return super.withTitle(title) as ColorPickerDialogBuilder
    }

    override fun withMessage(message: String?): ColorPickerDialogBuilder? {
        return super.withMessage(message) as ColorPickerDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): ColorPickerDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as ColorPickerDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): ColorPickerDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as ColorPickerDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): ColorPickerDialogBuilder? {
        return super.withOnCancelListener(listener) as ColorPickerDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): ColorPickerDialogBuilder? {
        return super.withOnShowListener(listener) as ColorPickerDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): ColorPickerDialogBuilder? {
        return super.withOnHideListener(listener) as ColorPickerDialogBuilder
    }

    override fun withCancelOnClickOutsude(closeOnClickOutside: Boolean): ColorPickerDialogBuilder? {
        return super.withCancelOnClickOutsude(closeOnClickOutside) as ColorPickerDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): ColorPickerDialogBuilder? {
        return super.withDialogType(dialogType) as ColorPickerDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): ColorPickerDialogBuilder? {
        return super.withAnimations(animation) as ColorPickerDialogBuilder
    }

    override fun withDialogCallbacks(callbacks: DialogCallbacks?): ColorPickerDialogBuilder? {
        return super.withDialogCallbacks(callbacks) as ColorPickerDialogBuilder
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray>?): ColorPickerDialogBuilder {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): ColorPickerDialogBuilder {
        this.selection = selection
        return this
    }

    override fun buildDialog(): ColorPickerDialog? {
        var colorPickerDialog = fragmentManager!!.findFragmentByTag(dialogTag) as ColorPickerDialog?
        if (colorPickerDialog == null) colorPickerDialog = ColorPickerDialog.Companion.newInstance(
            title,
            message,
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            cancelableOnClickOutside,
            animation
        )
        colorPickerDialog.setLifecycleOwner(dialogLifecycleOwner)
        colorPickerDialog.setDialogCallbacks(dialogCallbacks)
        colorPickerDialog.setFragmentManager(fragmentManager)
        colorPickerDialog.setDialogTag(dialogTag)
        colorPickerDialog.addOnShowListener(onShowListener)
        colorPickerDialog.addOnHideListener(onHideListener)
        colorPickerDialog.addOnCancelListener(onCancelListener)
        colorPickerDialog.addOnNegativeClickListeners(onNegativeClickListener)
        colorPickerDialog.addOnPositiveClickListener(onPositiveClickListener)
        colorPickerDialog.setAdapter(adapter)
        colorPickerDialog.setOnSelectionChangedListener(changedCallback)
        colorPickerDialog.selection = selection
        return colorPickerDialog
    }
}