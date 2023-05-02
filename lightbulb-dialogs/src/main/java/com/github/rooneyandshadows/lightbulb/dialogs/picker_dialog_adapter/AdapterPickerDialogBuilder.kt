package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel

@Suppress("UNCHECKED_CAST", "unused")
class AdapterPickerDialogBuilder<DialogType : AdapterPickerDialog<out EasyAdapterDataModel>> @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
    private val dialogInitializer: AdapterPickerDialogInitializer<DialogType>,
) : BaseDialogBuilder<DialogType>(lifecycleOwner, manager, dialogTag) {
    private var changedCallback: SelectionChangedListener<IntArray>? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private var selection: IntArray? = null

    @Override
    override fun setupNonRetainableSettings(dialog: DialogType) {
        dialog.apply {
            changedCallback?.apply { addOnSelectionChangedListener(this) }
            setItemDecoration(itemDecoration)
        }
    }

    @Override
    override fun setupRetainableSettings(dialog: DialogType) {
        dialog.apply {
            setSelection(selection)
        }
    }

    @Override
    override fun initializeNewDialog(): DialogType {
        return dialogInitializer.initialize()
    }

    @Override
    override fun withInitialDialogState(savedState: Bundle?): AdapterPickerDialogBuilder<DialogType> {
        return super.withInitialDialogState(savedState) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withTitle(title: String): AdapterPickerDialogBuilder<DialogType> {
        return super.withTitle(title) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withMessage(message: String): AdapterPickerDialogBuilder<DialogType> {
        return super.withMessage(message) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener?,
    ): AdapterPickerDialogBuilder<DialogType> {
        return super.withPositiveButton(
            configuration,
            onClickListener
        ) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener?,
    ): AdapterPickerDialogBuilder<DialogType> {
        return super.withNegativeButton(
            configuration,
            onClickListener
        ) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): AdapterPickerDialogBuilder<DialogType> {
        return super.withOnCancelListener(listener) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): AdapterPickerDialogBuilder<DialogType> {
        return super.withOnShowListener(listener) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): AdapterPickerDialogBuilder<DialogType> {
        return super.withOnHideListener(listener) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): AdapterPickerDialogBuilder<DialogType> {
        return super.withCancelOnClickOutside(closeOnClickOutside) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): AdapterPickerDialogBuilder<DialogType> {
        return super.withDialogType(dialogType) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): AdapterPickerDialogBuilder<DialogType> {
        return super.withAnimations(animation) as AdapterPickerDialogBuilder<DialogType>
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): AdapterPickerDialogBuilder<DialogType> {
        return super.withDialogListeners(listeners) as AdapterPickerDialogBuilder<DialogType>
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray>): AdapterPickerDialogBuilder<DialogType> {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): AdapterPickerDialogBuilder<DialogType> {
        this.selection = selection
        return this
    }

    fun withItemDecoration(decoration: RecyclerView.ItemDecoration?): AdapterPickerDialogBuilder<DialogType> {
        itemDecoration = decoration
        return this
    }

    interface AdapterPickerDialogInitializer<DialogType : AdapterPickerDialog<out EasyAdapterDataModel>> {
        fun initialize(): DialogType
    }
}