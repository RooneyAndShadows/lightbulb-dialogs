package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter

import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.SELECT_MULTIPLE
import java.util.*

class ChipsCollection(adapter: ChipsPickerAdapter) : ExtendedCollection<ChipModel>(adapter, SELECT_MULTIPLE) {
    @Override
    override fun addInternally(item: ChipModel): Boolean {
        val added = super.addInternally(item)
        if (added) selectItem(item, true)
        return added
    }

    @Override
    override fun filterItem(item: ChipModel, filterQuery: String): Boolean {
        val locale = Locale.getDefault()
        val filterString = filterQuery.lowercase(locale)
        val itemName = item.itemName.lowercase(locale)
        return itemName.contains(filterString)
    }

    fun hasItemWithName(name: String): Boolean {
        if (name.isBlank()) return false
        return getItems().any { it.itemName == name }
    }
}