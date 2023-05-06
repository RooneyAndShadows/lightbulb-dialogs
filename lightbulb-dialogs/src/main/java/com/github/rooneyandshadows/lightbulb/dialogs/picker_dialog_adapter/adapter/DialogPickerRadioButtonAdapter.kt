package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.recycleradapters.R
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.SELECT_SINGLE
import com.github.rooneyandshadows.lightbulb.selectableview.RadioButtonView
import com.github.rooneyandshadows.lightbulb.selectableview.RadioButtonView.OnCheckedChangeListener

@Suppress("UNUSED_PARAMETER", "unused", "MemberVisibilityCanBePrivate")
open class DialogPickerRadioButtonAdapter<ItemType : EasyAdapterDataModel> : DialogPickerAdapter<ItemType>() {

    @Override
    override fun createCollection(): ExtendedCollection<ItemType> {
        return ExtendedCollection(this, SELECT_SINGLE)
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val radioButtonView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_radio_button,
            parent,
            false
        ) as RadioButtonView
        val padding = getItemPadding(context)
        if (padding != null && padding.size == 4) radioButtonView.setPadding(
            padding[0],
            padding[1],
            padding[2],
            padding[3]
        )
        return RadioButtonViewHolder(radioButtonView)
    }

    @Suppress("UNCHECKED_CAST")
    @Override
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vHolder: RadioButtonViewHolder = holder as DialogPickerRadioButtonAdapter<ItemType>.RadioButtonViewHolder
        vHolder.bindItem()
    }

    @Suppress("UNCHECKED_CAST")
    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val vh: RadioButtonViewHolder = holder as DialogPickerRadioButtonAdapter<ItemType>.RadioButtonViewHolder
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

    inner class RadioButtonViewHolder(radioButtonView: RadioButtonView) : RecyclerView.ViewHolder(radioButtonView) {
        private var selectableView: RadioButtonView = itemView as RadioButtonView
        private val onCheckedListener = OnCheckedChangeListener { rbv, isChecked ->
            rbv?.apply {
                post {
                    val position = bindingAdapterPosition - headersCount
                    collection.selectItemAt(position, isChecked)
                }
            }
        }

        fun bindItem() {
            selectableView.apply {
                val item = collection.getItem(bindingAdapterPosition - headersCount) ?: return
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