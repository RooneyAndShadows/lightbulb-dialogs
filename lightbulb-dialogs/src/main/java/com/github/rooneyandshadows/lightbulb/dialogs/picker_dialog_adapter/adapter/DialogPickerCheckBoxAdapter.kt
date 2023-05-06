package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.R
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.SELECT_MULTIPLE
import com.github.rooneyandshadows.lightbulb.selectableview.CheckBoxView
import com.github.rooneyandshadows.lightbulb.selectableview.CheckBoxView.OnCheckedChangeListener
import com.github.rooneyandshadows.lightbulb.selectableview.RadioButtonView

@Suppress("UNUSED_PARAMETER", "unused", "MemberVisibilityCanBePrivate")
open class DialogPickerCheckBoxAdapter<ItemType : EasyAdapterDataModel> : DialogPickerAdapter<ItemType>() {

    @Override
    override fun createCollection(): ExtendedCollection<ItemType> {
        return ExtendedCollection(this, SELECT_MULTIPLE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val checkBoxView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_checkbox_button,
            parent, false
        ) as CheckBoxView
        val padding = getItemPadding(context)
        if (padding != null && padding.size == 4) checkBoxView.setPadding(
            padding[0],
            padding[1],
            padding[2],
            padding[3]
        )
        return CheckBoxViewHolder(checkBoxView)
    }

    @Suppress("UNCHECKED_CAST")
    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vHolder: CheckBoxViewHolder = holder as DialogPickerCheckBoxAdapter<ItemType>.CheckBoxViewHolder
        vHolder.bindItem()
    }

    @Suppress("UNCHECKED_CAST")
    @Override
    override fun onViewRecycled(holder: ViewHolder) {
        val vh: CheckBoxViewHolder = holder as DialogPickerCheckBoxAdapter<ItemType>.CheckBoxViewHolder
        vh.recycle()
    }

    @Override
    override fun getItemCount(): Int {
        return collection.size()
    }

    protected open fun getItemIcon(context: Context, item: ItemType): Drawable? {
        return null
    }

    protected open fun getItemIconBackground(context: Context, item: ItemType): Drawable? {
        return null
    }

    protected open fun getItemPadding(context: Context): IntArray? {
        return null
    }

    inner class CheckBoxViewHolder internal constructor(checkBoxView: CheckBoxView) : ViewHolder(checkBoxView) {
        private var selectableView: CheckBoxView = itemView as CheckBoxView
        private val onCheckedListener = OnCheckedChangeListener { cbv, isChecked ->
            cbv?.apply {
                post {
                    val position = bindingAdapterPosition - headersCount
                    collection.selectItemAt(position, isChecked)
                }
            }
        }

        fun bindItem() {
            selectableView.apply {
                val item = collection.getItem(bindingAdapterPosition) ?: return
                val isSelectedInAdapter = collection.isItemSelected(item)
                val itemText = collection.getItemName(item)
                val itemIcon = getItemIcon(context, item)
                val itemIconBackground = getItemIconBackground(context, item)
                if (isChecked != isSelectedInAdapter) isChecked = isSelectedInAdapter
                text = itemText
                setIcon(itemIcon, itemIconBackground)
                setOnCheckedListener(onCheckedListener)
            }
        }

        fun recycle() {
            selectableView.apply {
                setIcon(null, null)
                setOnCheckedListener(null)
            }
        }
    }
}