package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterSelectableModes.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import java.util.*
import java.util.function.Predicate


@Suppress("unused", "UNCHECKED_CAST")
class ChipsPickerAdapter : EasyRecyclerAdapter<ChipModel>(SELECT_MULTIPLE), Filterable {
    private var allItems: MutableList<ChipModel> = mutableListOf()

    companion object {
        private const val FILTERED_ITEMS_KEY = "FILTERED_ITEMS_KEY"
    }

    @Override
    override fun onSaveInstanceState(outState: Bundle): Bundle {
        val state: Bundle = super.onSaveInstanceState(outState)
        outState.apply {
            BundleUtils.putParcelableArrayList(FILTERED_ITEMS_KEY, this, allItems as ArrayList<ChipModel>)
        }
        return state
    }

    @Override
    override fun onRestoreInstanceState(savedState: Bundle) {
        savedState.apply {
            BundleUtils.apply {
                allItems = getParcelableArrayList(
                    FILTERED_ITEMS_KEY,
                    savedState,
                    ChipModel::class.java
                ) as MutableList<ChipModel>
            }
        }
        super.onRestoreInstanceState(savedState)
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
        val vHolder: ChipVH = holder as ChipsPickerAdapter.ChipVH
        vHolder.setItem()
    }

    @Override
    override fun getItemCount(): Int {
        return allItems.size
    }

    @Override
    override fun setCollection(collection: List<ChipModel>) {
        allItems = ArrayList(collection)
        super.setCollection(collection)
    }

    @Override
    override fun addItem(item: ChipModel) {
        if (hasItemWithName(item.itemName)) return
        allItems.add(item)
        super.addItem(item)
        selectItem(item, true)
    }

    fun getAllItems():List<ChipModel>{
        return allItems.toList()
    }

    fun hasItemWithName(name: String): Boolean {
        if (name.isBlank()) return false
        return getItems(Predicate { item -> return@Predicate item.itemName == name })
            .isNotEmpty()
    }

    @Override
    override fun getItemName(item: ChipModel): String {
        return item.itemName
    }

    @Override
    override fun getFilter(): Filter {
        return object : Filter() {
            @Override
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val locale = Locale.getDefault()
                val filterString = charSequence.toString().lowercase(locale)
                val itemsInAdapter = getItems()
                val result: MutableList<ChipModel> = mutableListOf()
                if (filterString.isBlank()) result.addAll(itemsInAdapter)
                else {
                    itemsInAdapter.forEach { item ->
                        val itemName = item.itemName.lowercase(locale)
                        if (itemName.contains(filterString)) result.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = result
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                allItems = filterResults.values as MutableList<ChipModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class ChipVH internal constructor(private val chipView: LinearLayoutCompat) : RecyclerView.ViewHolder(chipView) {
        private var initialized: Boolean = false
        fun setItem() {
            chipView.apply {
                val horPadding = ResourceUtils.getDimenById(context, R.dimen.spacing_size_small).toInt()
                val position = absoluteAdapterPosition - headersCount
                val item = getItem(position)!!
                val selectedInAdapter = this@ChipsPickerAdapter.isItemSelected(position)
                isSelected = selectedInAdapter
                findViewById<TextView>(R.id.chip_text_view)?.apply {
                    val color = if (selectedInAdapter) R.attr.colorOnPrimary else R.attr.colorPrimary
                    setTextColor(ResourceUtils.getColorByAttribute(context, color))
                    if (selectedInAdapter) setPadding(horPadding / 2, paddingTop, paddingRight, paddingBottom)
                    else setPadding(horPadding, paddingTop, paddingRight, paddingBottom)
                    text = item.itemName
                }
                findViewById<AppCompatImageView>(R.id.chip_selected_indicator).apply icon@{
                    visibility = if (selectedInAdapter) View.VISIBLE else View.GONE
                    val color = if (selectedInAdapter) R.attr.colorOnPrimary else R.attr.colorPrimary
                    if (!selectedInAdapter) return@icon
                    setColorFilter(ResourceUtils.getColorByAttribute(context, color))
                    setImageDrawable(ResourceUtils.getDrawable(context, R.drawable.chip_selected_icon))
                    (layoutParams as MarginLayoutParams).leftMargin = horPadding
                }
                setOnClickListener {
                    val pos = absoluteAdapterPosition - headersCount
                    val selected = this@ChipsPickerAdapter.isItemSelected(pos)
                    selectItem(item, !selected)
                }
            }
        }
    }

    /*private fun animateToWidth(view: View,show:Boolean) {
        val from = if(show)0

        val anim = ValueAnimator.ofInt(view.measuredHeight, -100)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
            layoutParams.height = `val`
            view.layoutParams = layoutParams
        }
        anim.duration = 1000
        anim.start()
    }*/

    class ChipModel : EasyAdapterDataModel {
        val chipTitle: String

        override val itemName: String
            get() = chipTitle

        constructor(title: String) : super() {
            this.chipTitle = title
        }

        // Parcelling part
        constructor(parcel: Parcel) : super() {
            chipTitle = parcel.readString()!!
        }

        @Override
        override fun writeToParcel(dest: Parcel, i: Int) {
            dest.writeString(chipTitle)
        }

        @Override
        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ChipModel> {
            override fun createFromParcel(parcel: Parcel): ChipModel {
                return ChipModel(parcel)
            }

            override fun newArray(size: Int): Array<ChipModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}