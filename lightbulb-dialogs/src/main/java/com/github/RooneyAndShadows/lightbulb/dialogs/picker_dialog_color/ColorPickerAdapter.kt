package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter.ColorModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterSelectableModes
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import java.lang.Exception
import java.util.ArrayList

@Suppress("unused")
class ColorPickerAdapter(
    private val context: Context,
    selectableMode: EasyAdapterSelectableModes = EasyAdapterSelectableModes.SELECT_SINGLE
) : EasyRecyclerAdapter<ColorModel>(selectableMode) {
    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: AppCompatImageButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_color_item, parent, false) as AppCompatImageButton
        return ColorVH(v)
    }

    @Override
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as ColorVH
        val item: ColorModel = getItem(position)!!
        vh.setItem(item, position, this)
    }

    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as ColorVH
        vh.release()
    }

    @Override
    override fun getItemName(item: ColorModel): String {
        return item.externalName
    }

    @Override
    override fun setCollection(collection: List<ColorModel>) {
        super.setCollection(validatePendingItems(collection))
    }

    @Override
    override fun appendCollection(collection: List<ColorModel>) {
        super.appendCollection(validatePendingItems(collection))
    }

    @Override
    override fun addItem(item: ColorModel) {
        validatePendingItem(item)?.apply {
            super.addItem(this)
        }
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

    fun getColorDrawable(colorModel: ColorModel?): Drawable? {
        if (colorModel == null) return null
        val colorDrawable: Drawable = ResourceUtils.getDrawable(context, R.drawable.color_icon)!!.mutate()
        colorDrawable.setTint(Color.parseColor(colorModel.colorHex))
        return colorDrawable
    }

    class ColorVH internal constructor(view: AppCompatImageButton) : RecyclerView.ViewHolder(view) {
        private var colorView: AppCompatImageButton = itemView as AppCompatImageButton
        private var item: ColorModel? = null

        fun setItem(item: ColorModel, position: Int, adapter: ColorPickerAdapter) {
            this.item = item
            val ctx: Context = colorView.context
            val isItemSelected: Boolean = adapter.isItemSelected(position)
            val drawableId = if (isItemSelected) R.drawable.color_selected_icon else R.drawable.color_icon
            val iconDrawable: Drawable = ResourceUtils.getDrawable(ctx, drawableId)!!.mutate()
            iconDrawable.setTint(Color.parseColor(item.colorHex))
            //colorView.setColorFilter(Color.parseColor(item.colorHex));
            colorView.setImageDrawable(iconDrawable)
            colorView.setOnClickListener {
                val prevState: Boolean = adapter.isItemSelected(position)
                adapter.selectItemAt(position, !prevState)
            }
        }

        fun release() {
            colorView.setImageDrawable(null)
        }
    }

    @Suppress("unused")
    class ColorModel : EasyAdapterDataModel {
        val colorHex: String
        val externalName: String

        constructor(colorHex: String, externalName: String) : super() {
            this.colorHex = colorHex
            this.externalName = externalName
        }

        // Parcelling part
        constructor(parcel: Parcel) : super() {
            colorHex = parcel.readString()!!
            externalName = parcel.readString()!!
        }

        override fun writeToParcel(dest: Parcel, i: Int) {
            dest.writeString(colorHex)
            dest.writeString(externalName)
        }

        override val itemName: String
            get() = externalName

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ColorModel> {
            override fun createFromParcel(parcel: Parcel): ColorModel {
                return ColorModel(parcel)
            }

            override fun newArray(size: Int): Array<ColorModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}