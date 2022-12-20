package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

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
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel

class AdapterPickerDialogBuilder<ModelType : EasyAdapterDataModel?> : BaseDialogBuilder<AdapterPickerDialog<ModelType?>?> {
    private val adapter: EasyRecyclerAdapter<ModelType?>
    private var changedCallback: SelectionChangedListener<IntArray?>? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private var selection: IntArray

    constructor(manager: FragmentManager?, dialogTag: String?, adapter: EasyRecyclerAdapter<ModelType>) : super(
        manager,
        dialogTag
    ) {
        this.adapter = adapter
    }

    constructor(
        lifecycleOwner: LifecycleOwner?,
        manager: FragmentManager?,
        dialogTag: String?,
        adapter: EasyRecyclerAdapter<ModelType>
    ) : super(lifecycleOwner, manager, dialogTag) {
        this.adapter = adapter
    }

    override fun withTitle(title: String?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withTitle(title) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withMessage(message: String?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withMessage(message) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withPositiveButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withPositiveButton(
            configuration,
            onClickListener
        ) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withNegativeButton(
        configuration: DialogButtonConfiguration?,
        onClickListener: DialogButtonClickListener?
    ): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withNegativeButton(
            configuration,
            onClickListener
        ) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withOnCancelListener(listener: DialogCancelListener?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withOnCancelListener(listener) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withOnShowListener(listener: DialogShowListener?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withOnShowListener(listener) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withOnHideListener(listener: DialogHideListener?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withOnHideListener(listener) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withCancelOnClickOutside(closeOnClickOutside: Boolean): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withCancelOnClickOutside(closeOnClickOutside) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withDialogType(dialogType: DialogTypes?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withDialogType(dialogType) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withAnimations(animation: DialogAnimationTypes?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withAnimations(animation) as AdapterPickerDialogBuilder<ModelType?>
    }

    override fun withDialogListeners(callbacks: DialogCallbacks?): AdapterPickerDialogBuilder<ModelType?>? {
        return super.withDialogListeners(callbacks) as AdapterPickerDialogBuilder<ModelType?>
    }

    fun withSelectionCallback(listener: SelectionChangedListener<IntArray?>?): AdapterPickerDialogBuilder<ModelType> {
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

    override fun buildDialog(): AdapterPickerDialog<ModelType?>? {
        var dialogFragment = dialogParentFragmentManager!!.findFragmentByTag(dialogParentFragmentManager) as AdapterPickerDialog<ModelType?>?
        if (dialogFragment == null) dialogFragment = AdapterPickerDialog.Companion.newInstance<ModelType?>(
            title,
            message,
            positiveButtonConfiguration,
            negativeButtonConfiguration,
            cancelableOnClickOutside,
            dialogType,
            animation
        )
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner)
        dialogFragment.setDialogCallbacks(dialogListeners)
        dialogFragment.setParentFragManager(dialogParentFragmentManager)
        dialogFragment.setDialogTag(dialogParentFragmentManager)
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener)
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener)
        dialogFragment.addOnShowListener(onShowListener)
        dialogFragment.addOnHideListener(onHideListener)
        dialogFragment.addOnCancelListener(onCancelListener)
        dialogFragment.setAdapter(adapter)
        dialogFragment.setOnSelectionChangedListener(changedCallback)
        dialogFragment.selection = selection
        dialogFragment.setItemDecoration(itemDecoration)
        return dialogFragment
    }
}