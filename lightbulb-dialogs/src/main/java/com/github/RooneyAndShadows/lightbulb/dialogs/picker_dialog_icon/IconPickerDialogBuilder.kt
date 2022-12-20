package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon

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

class IconPickerDialogBuilder : BaseDialogBuilder<IconPickerDialog?> {
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var selection: IntArray
    private val adapter: IconPickerAdapter

    constructor(manager: FragmentManager?, dialogTag: String?, adapter: IconPickerAdapter) : super(manager, dialogTag) {
        this.adapter = adapter
    }

    constructor(
        lifecycleOwner: LifecycleOwner?,
        manager: FragmentManager?,
        dialogTag: String?,
        adapter: IconPickerAdapter
    ) : super(lifecycleOwner, manager, dialogTag) {
        this.adapter = adapter
    }

    override fun withTitle(title: String?): IconPickerDialogBuilder? {
        return super.withTitle(title) as IconPickerDialogBuilder
    }

    override fun withMessage(message: String?): IconPickerDialogBuilder? {
        return super.withMessage(message) as IconPickerDialogBuilder
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): IconPickerDialogBuilder? {
        return super.withPositiveButton(configuration, onClickListener) as IconPickerDialogBuilder
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): IconPickerDialogBuilder? {
        return super.withNegativeButton(configuration, onClickListener) as IconPickerDialogBuilder
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): IconPickerDialogBuilder? {
        return super.withOnCancelListener(listener) as IconPickerDialogBuilder
    }

    override fun withOnShowListener(listener: DialogShowListener?): IconPickerDialogBuilder? {
        return super.withOnShowListener(listener) as IconPickerDialogBuilder
    }

    override fun withOnHideListener(listener: DialogHideListener?): IconPickerDialogBuilder? {
        return super.withOnHideListener(listener) as IconPickerDialogBuilder
    }

    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): IconPickerDialogBuilder? {
        return super.withCancelOnClickOutside(closeOnClickOutside) as IconPickerDialogBuilder
    }

    override fun withDialogType(dialogType: DialogTypes?): IconPickerDialogBuilder? {
        return super.withDialogType(dialogType) as IconPickerDialogBuilder
    }

    override fun withAnimations(animation: DialogAnimationTypes?): IconPickerDialogBuilder? {
        return super.withAnimations(animation) as IconPickerDialogBuilder
    }

    override fun withDialogListeners(callbacks: DialogCallbacks?): IconPickerDialogBuilder? {
        return super.withDialogListeners(callbacks) as IconPickerDialogBuilder
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray>?): IconPickerDialogBuilder {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): IconPickerDialogBuilder {
        this.selection = selection
        return this
    }

    override fun buildDialog(): IconPickerDialog? {
        var iconPickerDialog = dialogParentFragmentManager!!.findFragmentByTag(dialogParentFragmentManager) as IconPickerDialog?
        if (iconPickerDialog == null) iconPickerDialog = IconPickerDialog.Companion.newInstance(
            title,
            message,
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            cancelableOnClickOutside,
            animation
        )
        iconPickerDialog.setLifecycleOwner(dialogLifecycleOwner)
        iconPickerDialog.setDialogCallbacks(dialogListeners)
        iconPickerDialog.setParentFragManager(dialogParentFragmentManager)
        iconPickerDialog.setDialogTag(dialogParentFragmentManager)
        iconPickerDialog.addOnShowListener(onShowListener)
        iconPickerDialog.addOnHideListener(onHideListener)
        iconPickerDialog.addOnCancelListener(onCancelListener)
        iconPickerDialog.addOnNegativeClickListeners(onNegativeClickListener)
        iconPickerDialog.addOnPositiveClickListener(onPositiveClickListener)
        iconPickerDialog.setAdapter(adapter)
        iconPickerDialog.setOnSelectionChangedListener(changedCallback)
        iconPickerDialog.selection = selection
        return iconPickerDialog
    }
}