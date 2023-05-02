package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter

import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.SELECT_MULTIPLE

abstract class DialogPickerAdapter<ItemType : EasyAdapterDataModel> : EasyRecyclerAdapter<ItemType>() {
    override val collection: ExtendedCollection<ItemType>
        get() = super.collection as ExtendedCollection<ItemType>

    override fun createCollection(): ExtendedCollection<ItemType> {
        return ExtendedCollection(this, SELECT_MULTIPLE)
    }
}