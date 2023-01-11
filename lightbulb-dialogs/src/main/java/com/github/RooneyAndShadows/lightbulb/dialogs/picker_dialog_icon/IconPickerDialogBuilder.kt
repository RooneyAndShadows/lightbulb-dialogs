package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon

import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime.DateTimePickerDialog

@Suppress("unused")
class IconPickerDialogBuilder @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
    private val adapter: IconPickerAdapter,
) : BaseDialogBuilder<IconPickerDialog>(lifecycleOwner, manager, dialogTag) {
    private var changedCallback: SelectionChangedListener<IntArray?>? = null
    private var selection: IntArray? = null

    @Override
    override fun withTitle(title: String): IconPickerDialogBuilder {
        return super.withTitle(title) as IconPickerDialogBuilder
    }

    @Override
    override fun withMessage(message: String): IconPickerDialogBuilder {
        return super.withMessage(message) as IconPickerDialogBuilder
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): IconPickerDialogBuilder {
        return super.withPositiveButton(configuration, onClickListener) as IconPickerDialogBuilder
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener,
    ): IconPickerDialogBuilder {
        return super.withNegativeButton(configuration, onClickListener) as IconPickerDialogBuilder
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

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray?>): IconPickerDialogBuilder {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): IconPickerDialogBuilder {
        this.selection = selection
        return this
    }

    @Override
    override fun buildDialog(): IconPickerDialog {
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
            setAdapter(adapter)
            setOnSelectionChangedListener(changedCallback)
            setSelection(selection)
        }
    }

    private fun getExistingDialogOrCreate(): IconPickerDialog {
        val dialog = dialogParentFragmentManager.findFragmentByTag(dialogTag) as IconPickerDialog?
        return dialog ?: IconPickerDialog.newInstance().apply {
            dialogTitle = title
            dialogMessage = message
            dialogPositiveButton = positiveButtonConfiguration
            dialogNegativeButton = negativeButtonConfiguration
            isCancelable = cancelableOnClickOutside
            dialogAnimationType = animation
        }
    }
}