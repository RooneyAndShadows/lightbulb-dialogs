package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel

@Suppress("UNCHECKED_CAST", "unused")
class AdapterPickerDialogBuilder<ModelType : EasyAdapterDataModel> @JvmOverloads constructor(
    lifecycleOwner: LifecycleOwner? = null,
    manager: FragmentManager,
    dialogTag: String,
    private val adapter: EasyRecyclerAdapter<ModelType>
) :
    BaseDialogBuilder<AdapterPickerDialog<ModelType>>(lifecycleOwner, manager, dialogTag) {
    private var changedCallback: SelectionChangedListener<IntArray?>? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private var selection: IntArray? = null

    @Override
    override fun withTitle(title: String): AdapterPickerDialogBuilder<ModelType> {
        return super.withTitle(title) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withMessage(message: String): AdapterPickerDialogBuilder<ModelType> {
        return super.withMessage(message) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withPositiveButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): AdapterPickerDialogBuilder<ModelType> {
        return super.withPositiveButton(
            configuration,
            onClickListener
        ) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withNegativeButton(
        configuration: DialogButtonConfiguration,
        onClickListener: DialogButtonClickListener
    ): AdapterPickerDialogBuilder<ModelType> {
        return super.withNegativeButton(
            configuration,
            onClickListener
        ) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withOnCancelListener(listener: DialogCancelListener): AdapterPickerDialogBuilder<ModelType> {
        return super.withOnCancelListener(listener) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withOnShowListener(listener: DialogShowListener): AdapterPickerDialogBuilder<ModelType> {
        return super.withOnShowListener(listener) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withOnHideListener(listener: DialogHideListener): AdapterPickerDialogBuilder<ModelType> {
        return super.withOnHideListener(listener) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): AdapterPickerDialogBuilder<ModelType> {
        return super.withCancelOnClickOutside(closeOnClickOutside) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withDialogType(dialogType: DialogTypes): AdapterPickerDialogBuilder<ModelType> {
        return super.withDialogType(dialogType) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withAnimations(animation: DialogAnimationTypes): AdapterPickerDialogBuilder<ModelType> {
        return super.withAnimations(animation) as AdapterPickerDialogBuilder<ModelType>
    }

    @Override
    override fun withDialogListeners(listeners: DialogListeners): AdapterPickerDialogBuilder<ModelType> {
        return super.withDialogListeners(listeners) as AdapterPickerDialogBuilder<ModelType>
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray?>): AdapterPickerDialogBuilder<ModelType> {
        changedCallback = listener
        return this
    }

    fun withSelection(selection: IntArray): AdapterPickerDialogBuilder<ModelType> {
        this.selection = selection
        return this
    }

    fun withItemDecoration(decoration: RecyclerView.ItemDecoration?): AdapterPickerDialogBuilder<ModelType> {
        itemDecoration = decoration
        return this
    }

    override fun buildDialog(): AdapterPickerDialog<ModelType> {
        val dialogFragment = dialogParentFragmentManager.findFragmentByTag(dialogTag) as AdapterPickerDialog<ModelType>?
        return dialogFragment ?: AdapterPickerDialog.newInstance<ModelType>(
            title,
            message,
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            cancelableOnClickOutside,
            dialogType,
            animation
        ).apply {
            setLifecycleOwner(dialogLifecycleOwner)
            setDialogCallbacks(dialogListeners)
            setParentFragManager(dialogParentFragmentManager)
            setDialogTag(dialogTag)
            onNegativeClickListener?.apply { addOnNegativeClickListeners(this) }
            onPositiveClickListener?.apply { addOnPositiveClickListener(this) }
            onShowListener?.apply { addOnShowListener(this) }
            onHideListener?.apply { addOnHideListener(this) }
            onCancelListener?.apply { addOnCancelListener(this) }
            setAdapter(adapter)
            changedCallback?.apply { setOnSelectionChangedListener(this) }
            setSelection(selection)
            setItemDecoration(itemDecoration)
        }
    }
}