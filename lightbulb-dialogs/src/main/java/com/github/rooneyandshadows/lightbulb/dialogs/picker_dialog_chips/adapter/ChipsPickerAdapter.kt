package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection

@Suppress("unused", "UNCHECKED_CAST")
class ChipsPickerAdapter : DialogPickerAdapter<ChipModel>() {
    override val collection: ChipsCollection
        get() = super.collection as ChipsCollection

    init {
        setHasStableIds(true)
    }

    @Override
    override fun createCollection(): ExtendedCollection<ChipModel> {
        return ChipsCollection(this)
    }

    @Override
    override fun getItemId(position: Int): Long {
        return collection.getItem(position).hashCode().toLong()
    }

    @Override
    override fun getItemCount(): Int {
        return collection.filteredItems.size
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ctx = parent.context
        val layoutId = R.layout.layout_chip_item
        val layoutInflater = LayoutInflater.from(ctx)
        val layout = layoutInflater.inflate(layoutId, parent, false) as LinearLayoutCompat
        return ChipVH(layout)
    }

    @Override
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vHolder: ChipVH = holder as ChipVH
        vHolder.init()
    }

    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vHolder = holder as ChipVH
        vHolder.recycle()
    }

    inner class ChipVH internal constructor(private val chipView: LinearLayoutCompat) : RecyclerView.ViewHolder(chipView) {
        private var initialized: Boolean = false
        private val context = itemView.context
        private val textView = itemView.findViewById<TextView>(R.id.chip_text_view)
        private val iconView = itemView.findViewById<AppCompatImageView>(R.id.chip_selected_indicator)
        private val horPadding = ResourceUtils.getDimenById(context, R.dimen.spacing_size_small).toInt()
        private val filteredPosition
            get() = absoluteAdapterPosition - headersCount

        init {
            chipView.apply {
                setOnClickListener {
                    val item = collection.getFilteredItem(filteredPosition) ?: return@setOnClickListener
                    val newState = !collection.isItemSelected(item)
                    collection.selectItem(item, newState, false)
                    notifyItemChanged(filteredPosition, false)
                    //update item decoration before/after element
                    if (newState) {
                        val isLast = filteredPosition == itemCount - 1
                        if (!isLast) notifyItemChanged(filteredPosition + 1, false)
                    } else {
                        val isFirst = filteredPosition == 0
                        if (!isFirst) notifyItemChanged(filteredPosition - 1, false)
                    }
                }
            }
        }

        fun init() {
            val item = collection.getFilteredItem(filteredPosition) ?: return
            val selectedInAdapter = collection.isItemSelected(item)
            chipView.apply {
                isSelected = selectedInAdapter
                textView.apply {
                    val color = if (selectedInAdapter) R.attr.colorOnPrimary else R.attr.colorPrimary
                    setTextColor(ResourceUtils.getColorByAttribute(context, color))
                    if (selectedInAdapter) setPadding(horPadding / 2, paddingTop, paddingRight, paddingBottom)
                    else setPadding(horPadding, paddingTop, paddingRight, paddingBottom)
                    text = item.itemName
                }
                iconView.apply icon@{
                    visibility = if (selectedInAdapter) View.VISIBLE else View.GONE
                    val color = if (selectedInAdapter) R.attr.colorOnPrimary else R.attr.colorPrimary
                    if (!selectedInAdapter) return@icon
                    setColorFilter(ResourceUtils.getColorByAttribute(context, color))
                    setImageDrawable(ResourceUtils.getDrawable(context, R.drawable.chip_selected_icon))
                    (layoutParams as MarginLayoutParams).leftMargin = horPadding
                }
                initialized = true
            }
        }

        fun recycle() {
            iconView.setImageDrawable(null)
        }
    }
}