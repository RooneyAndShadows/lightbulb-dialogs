package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterConfiguration
import java.lang.Exception
import java.util.ArrayList

class ColorPickerAdapter(private val context: Context, selectableMode: EasyAdapterSelectableModes?) :
    EasyRecyclerAdapter<ColorPickerAdapter.ColorModel?>(EasyAdapterConfiguration<ColorModel>().withSelectMode(selectableMode)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: AppCompatImageButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_color_item, parent, false) as AppCompatImageButton
        return ColorVH(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as ColorVH
        val item: ColorModel = items.get(position)
        vh.setItem(item, position, this)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as ColorVH
        vh.release()
    }

    val itemCount: Int
        get() = items.size

    override fun getItemName(item: ColorModel): String? {
        return item.itemName
    }

    fun setCollection(collection: List<ColorModel>) {
        super.setCollection(validatePendingCollection(collection))
    }

    fun appendCollection(collection: List<ColorModel>?) {
        super.appendCollection(validatePendingCollection(collection))
    }

    fun getColorDrawable(colorModel: ColorModel?): Drawable? {
        if (colorModel == null) return null
        val colorDrawable: Drawable = ResourceUtils.getDrawable(context, R.drawable.color_icon).mutate()
        colorDrawable.setTint(Color.parseColor(colorModel.colorHex))
        return colorDrawable
    }

    private fun validatePendingCollection(inputCollection: List<ColorModel>?): List<ColorModel> {
        val outputCollection: MutableList<ColorModel> = ArrayList()
        for (colorInput in inputCollection!!) {
            try {
                Color.parseColor(colorInput.colorHex)
                outputCollection.add(colorInput)
            } catch (e: Exception) {
                //ignore
            }
        }
        return outputCollection
    }

    class ColorVH internal constructor(view: AppCompatImageButton?) : RecyclerView.ViewHolder(view) {
        protected var colorView: AppCompatImageButton
        protected var item: ColorModel? = null
        fun setItem(item: ColorModel, position: Int, adapter: ColorPickerAdapter) {
            this.item = item
            val ctx: Context = colorView.getContext()
            val isItemSelected: Boolean = adapter.isItemSelected(position)
            val drawableId = if (isItemSelected) R.drawable.color_selected_icon else R.drawable.color_icon
            val iconDrawable: Drawable = ResourceUtils.getDrawable(ctx, drawableId).mutate()
            iconDrawable.setTint(Color.parseColor(item.colorHex))
            //colorView.setColorFilter(Color.parseColor(item.colorHex));
            colorView.setImageDrawable(iconDrawable)
            colorView.setOnClickListener(View.OnClickListener { v: View? ->
                val prevState: Boolean = adapter.isItemSelected(position)
                adapter.selectItemAt(position, !prevState)
            })
        }

        fun release() {
            colorView.setImageDrawable(null)
        }

        init {
            colorView = itemView as AppCompatImageButton
        }
    }

    class ColorModel : EasyAdapterDataModel {
        val colorHex: String?
        val itemName: String?

        constructor(iconName: String?, externalName: String?) : super(false) {
            itemName = externalName
            colorHex = iconName
        }

        // Parcelling part
        constructor(`in`: Parcel) : super(`in`) {
            colorHex = `in`.readString()
            itemName = `in`.readString()
        }

        override fun writeToParcel(dest: Parcel, i: Int) {
            dest.writeString(colorHex)
            dest.writeString(itemName)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            val CREATOR: Parcelable.Creator<ColorModel> = object : Parcelable.Creator<ColorModel?> {
                override fun createFromParcel(`in`: Parcel): ColorModel? {
                    return ColorModel(`in`)
                }

                override fun newArray(size: Int): Array<ColorModel?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}