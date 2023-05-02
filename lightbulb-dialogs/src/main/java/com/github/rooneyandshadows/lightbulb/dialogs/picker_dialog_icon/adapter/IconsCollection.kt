package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter

import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection
import java.util.ArrayList

class IconsCollection(adapter: IconPickerAdapter, selectableMode: SelectableModes) :
    ExtendedCollection<IconModel>(adapter, selectableMode) {

    @Override
    override fun setInternally(collection: List<IconModel>): Boolean {
        validatePendingCollection(collection).apply {
            return super.setInternally(this)
        }
    }

    @Override
    override fun addAllInternally(collection: List<IconModel>): Boolean {
        validatePendingCollection(collection).apply {
            return super.addAllInternally(this)
        }
    }

    @Override
    override fun addInternally(item: IconModel): Boolean {
        validatePendingItem(item)?.apply {
            return super.addInternally(this)
        }
        return false
    }

    private fun validatePendingCollection(inputCollection: List<IconModel>?): List<IconModel> {
        val outputCollection: MutableList<IconModel> = ArrayList()
        for (inputIcon in inputCollection!!) {
            validatePendingItem(inputIcon)?.apply {
                outputCollection.add(this)
            }
        }
        return outputCollection
    }

    private fun validatePendingItem(iconModel: IconModel): IconModel? {
        val isValid = IconModel.getIconValue(iconModel) != null
        return if (isValid) iconModel else null
    }
}