package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.adapter

import android.graphics.Color
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection
import java.lang.Exception
import java.util.ArrayList

class ColorsCollection(adapter: ColorPickerAdapter, selectableMode: SelectableModes) :
    ExtendedCollection<ColorModel>(adapter, selectableMode) {

    @Override
    override fun setInternally(collection: List<ColorModel>): Boolean {
        validatePendingItems(collection).apply {
            return super.setInternally(this)
        }
    }

    @Override
    override fun addAllInternally(collection: List<ColorModel>): Boolean {
        validatePendingItems(collection).apply {
            return super.addAllInternally(this)
        }
    }

    @Override
    override fun addInternally(item: ColorModel): Boolean {
        validatePendingItem(item)?.apply {
            return super.addInternally(this)
        }
        return false
    }

    private fun validatePendingItems(collection: List<ColorModel>): List<ColorModel> {
        val outputCollection: MutableList<ColorModel> = ArrayList()
        for (colorInput in collection)
            validatePendingItem(colorInput)?.apply {
                outputCollection.add(this)
            }
        return outputCollection
    }

    private fun validatePendingItem(item: ColorModel): ColorModel? {
        return try {
            Color.parseColor(item.colorHex)
            item
        } catch (e: Exception) {
            null
        }
    }
}