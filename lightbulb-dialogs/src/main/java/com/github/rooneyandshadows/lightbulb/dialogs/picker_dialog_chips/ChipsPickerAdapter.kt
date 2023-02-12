package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.ChipModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterSelectableModes.SELECT_MULTIPLE
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import java.util.*
import java.util.function.Predicate
import kotlin.streams.toList


@Suppress("unused", "UNCHECKED_CAST")
class ChipsPickerAdapter : EasyRecyclerAdapter<ChipModel>(SELECT_MULTIPLE) {

    companion object {
        private const val FILTERED_ITEMS_KEY = "FILTERED_ITEMS_KEY"
    }

    init {
        setHasStableIds(true)
    }

    @Override
    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
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
        vHolder.init()
    }


    @Override
    override fun addItem(item: ChipModel) {
        super.addItem(item)
        selectItem(item, true)
    }

    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vHolder = holder as ChipVH
        vHolder.recycle()
    }

    @Override
    override fun getItemName(item: ChipModel): String {
        return item.itemName
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
        return getItems().stream().filter(Predicate { item -> return@Predicate item.itemName == name })
            .toList()
            .isNotEmpty()
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
                    val item = getFilteredItems()[filteredPosition]
                    val newState = !this@ChipsPickerAdapter.isItemSelected(item)
                    selectItem(item, newState, false)
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
            val item = getFilteredItems()[filteredPosition]
            val originalPosition = getPosition(item)
            val selectedInAdapter = isItemSelected(originalPosition)
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
        var id: UUID
            private set

        init {
            id = UUID.randomUUID()
        }

        override val itemName: String
            get() = chipTitle

        constructor(title: String) : super() {
            this.chipTitle = title
        }

        // Parcelling part
        constructor(parcel: Parcel) : super() {
            chipTitle = parcel.readString()!!
            id = ParcelUtils.readUUID(parcel)!!
        }

        @Override
        override fun writeToParcel(dest: Parcel, i: Int) {
            dest.writeString(chipTitle)
            ParcelUtils.writeUUID(dest, id)
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