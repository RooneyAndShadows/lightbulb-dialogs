package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.IconUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerAdapter
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter.ChipsPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconSet.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.*
import com.mikepenz.iconics.typeface.IIcon

@Suppress("unused")
class IconPickerAdapter @JvmOverloads constructor(private val selectableMode: SelectableModes = SELECT_SINGLE) :
    DialogPickerAdapter<IconModel>() {
    override val collection: IconsCollection
        get() = super.collection as IconsCollection

    @Override
    override fun createCollection(): IconsCollection {
        return IconsCollection(this, selectableMode)
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: AppCompatImageButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_icon_item, parent, false) as AppCompatImageButton
        return IconVH(v)
    }

    @Override
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: IconModel = collection.getItem(position) ?: return
        val vh = holder as IconVH
        vh.setItem(item, position, this)
    }

    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as IconVH
        vh.release()
    }

    fun getDrawable(context: Context, iconModel: IconModel, drawableSize: Int): Drawable? {
        var size = drawableSize
        if (size < 0)
            size = 0
        val icon: IIcon = IconModel.getIconValue(iconModel) ?: return null
        return when (iconModel.iconSet) {
            FONTAWESOME -> IconUtils.getIconWithAttributeColor(
                context,
                icon,
                R.attr.colorOnSurface,
                size
            )
        }
    }

    inner class IconVH internal constructor(view: AppCompatImageButton) : RecyclerView.ViewHolder(view) {
        private var iconView: AppCompatImageButton = itemView as AppCompatImageButton
        private var item: IconModel? = null

        fun setItem(item: IconModel, position: Int, adapter: IconPickerAdapter) {
            val context = iconView.context
            this.item = item
            val isItemSelected: Boolean = adapter.collection.isItemSelected(position)
            val colorAttribute = if (isItemSelected) R.attr.colorAccent else R.attr.colorOnSurface
            val alpha = if (isItemSelected) 255 else (255 * 0.3).toInt()
            val iconSize: Int = ResourceUtils.getDimenPxById(context, R.dimen.dialog_icon_picker_icon_size)
            val iconDrawable: Drawable = IconUtils.getIconWithAttributeColor(
                context,
                IconModel.getIconValue(item)!!,
                colorAttribute,
                iconSize
            )
            iconDrawable.alpha = alpha
            iconView.setImageDrawable(iconDrawable)
            iconView.setOnClickListener {
                val prevState: Boolean = adapter.collection.isItemSelected(position)
                adapter.collection.selectItemAt(position, !prevState)
            }
        }

        fun release() {
            iconView.setImageDrawable(null)
        }
    }
}