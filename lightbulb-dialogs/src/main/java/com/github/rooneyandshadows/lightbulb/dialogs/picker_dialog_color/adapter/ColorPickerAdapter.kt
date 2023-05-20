package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.*

@Suppress("unused")
class ColorPickerAdapter @JvmOverloads constructor(private val selectableMode: SelectableModes = SELECT_SINGLE) :
    DialogPickerAdapter<ColorModel>() {
    override val collection: ColorsCollection
        get() = super.collection as ColorsCollection

    @Override
    override fun createCollection(): ColorsCollection {
        return ColorsCollection(this, selectableMode)
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: AppCompatImageButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_color_item, parent, false) as AppCompatImageButton
        return ColorVH(v)
    }

    @Override
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as ColorVH
        val item: ColorModel = collection.getItem(position) ?: return
        vh.setItem(item, position, this)
    }

    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as ColorVH
        vh.release()
    }

    fun getColorDrawable(context: Context, colorModel: ColorModel?): Drawable? {
        if (colorModel == null) return null
        val colorDrawable: Drawable = ResourceUtils.getDrawable(context, R.drawable.dialogs_ic_color)!!.mutate()
        colorDrawable.setTint(Color.parseColor(colorModel.colorHex))
        return colorDrawable
    }

    inner class ColorVH internal constructor(view: AppCompatImageButton) : RecyclerView.ViewHolder(view) {
        private var colorView: AppCompatImageButton = itemView as AppCompatImageButton
        private var item: ColorModel? = null

        fun setItem(item: ColorModel, position: Int, adapter: ColorPickerAdapter) {
            this.item = item
            val ctx: Context = colorView.context
            val isItemSelected: Boolean = adapter.collection.isItemSelected(position)
            val drawableId = if (isItemSelected) R.drawable.dialogs_ic_color_selected else R.drawable.dialogs_ic_color
            val iconDrawable: Drawable = ResourceUtils.getDrawable(ctx, drawableId)!!.mutate()
            iconDrawable.setTint(Color.parseColor(item.colorHex))
            //colorView.setColorFilter(Color.parseColor(item.colorHex));
            colorView.setImageDrawable(iconDrawable)
            colorView.setOnClickListener {
                val prevState: Boolean = adapter.collection.isItemSelected(position)
                adapter.collection.selectItemAt(position, !prevState)
            }
        }

        fun release() {
            colorView.setImageDrawable(null)
        }
    }
}