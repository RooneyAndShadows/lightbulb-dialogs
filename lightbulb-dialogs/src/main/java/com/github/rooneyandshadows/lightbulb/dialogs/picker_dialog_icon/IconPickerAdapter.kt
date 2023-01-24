package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.IconUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterSelectableModes
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

@Suppress("unused")
class IconPickerAdapter(selectableMode: EasyAdapterSelectableModes) :
    EasyRecyclerAdapter<IconModel>(selectableMode) {

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: AppCompatImageButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_icon_item, parent, false) as AppCompatImageButton
        return IconVH(v)
    }

    @Override
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: IconModel = getItem(position)!!
        val vh = holder as IconVH
        vh.setItem(item, position, this)
    }

    @Override
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as IconVH
        vh.release()
    }

    @Override
    override fun getItemName(item: IconModel): String {
        return item.itemName
    }

    @Override
    override fun setCollection(collection: List<IconModel>) {
        super.setCollection(validatePendingCollection(collection))
    }

    @Override
    override fun appendCollection(collection: List<IconModel>) {
        super.appendCollection(validatePendingCollection(collection))
    }

    @Override
    override fun addItem(item: IconModel) {
        validatePendingItem(item)?.apply {
            super.addItem(this)
        }
    }

    fun getDrawable(context: Context, iconModel: IconModel, drawableSize: Int): Drawable? {
        var size = drawableSize
        if (size < 0)
            size = 0
        val icon: IIcon = getIconValue(iconModel) ?: return null
        return when (iconModel.iconSet) {
            IconSet.FONTAWESOME -> IconUtils.getIconWithAttributeColor(
                context,
                icon,
                R.attr.colorOnSurface,
                size
            )
        }
    }

    private fun validatePendingCollection(inputCollection: List<IconModel>?): List<IconModel> {
        val outputCollection: MutableList<IconModel> = ArrayList()
        for (inputIcon in inputCollection!!) {
            validatePendingItem(inputIcon)?.apply {
                outputCollection.add(this)
            }
        }
        return outputCollection
    }

    private fun validatePendingItem(iconModel: IconModel): IconModel? {
        val isValid = getIconValue(iconModel) != null
        return if (isValid) iconModel else null
    }

    private fun getIconValue(iconModel: IconModel): IIcon? {
        return when (iconModel.iconSet) {
            IconSet.FONTAWESOME -> {
                val iconInputName = iconModel.iconName
                    .replace("-", "_")
                    .replace("fa_", "faw_") //To match enum name
                try {
                    FontAwesome.Icon.valueOf(iconInputName)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    inner class IconVH internal constructor(view: AppCompatImageButton) : RecyclerView.ViewHolder(view) {
        private var iconView: AppCompatImageButton = itemView as AppCompatImageButton
        private var item: IconModel? = null

        fun setItem(item: IconModel, position: Int, adapter: IconPickerAdapter) {
            val context = iconView.context
            this.item = item
            val isItemSelected: Boolean = adapter.isItemSelected(position)
            val colorAttribute = if (isItemSelected) R.attr.colorAccent else R.attr.colorOnSurface
            val alpha = if (isItemSelected) 255 else (255 * 0.3).toInt()
            val iconSize: Int = ResourceUtils.getDimenPxById(context, R.dimen.dialog_icon_picker_icon_size)
            val iconDrawable: Drawable = IconUtils.getIconWithAttributeColor(
                context,
                getIconValue(item)!!,
                colorAttribute,
                iconSize
            )
            iconDrawable.alpha = alpha
            iconView.setImageDrawable(iconDrawable)
            iconView.setOnClickListener {
                val prevState: Boolean = adapter.isItemSelected(position)
                adapter.selectItemAt(position, !prevState)
            }
        }

        fun release() {
            iconView.setImageDrawable(null)
        }
    }

    class IconModel : EasyAdapterDataModel {
        val iconName: String
        val iconSet: IconSet

        constructor(iconName: String, iconSet: IconSet) : super() {
            this.iconName = iconName
            this.iconSet = iconSet
        }

        // Parcelling part
        constructor(parcel: Parcel) : super() {
            iconName = parcel.readString()!!
            iconSet = IconSet.valueOf(parcel.readInt())!!
        }

        @Override
        override fun writeToParcel(dest: Parcel, i: Int) {
            dest.writeString(iconName)
            dest.writeInt(iconSet.value)
        }

        override val itemName: String
            get() = iconName

        @Override
        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<IconModel> {
            @Override
            override fun createFromParcel(parcel: Parcel): IconModel {
                return IconModel(parcel)
            }

            @Override
            override fun newArray(size: Int): Array<IconModel?> {
                return arrayOfNulls(size)
            }

            @JvmStatic
            fun getAllForSet(set: IconSet): List<IconModel> {
                return when (set) {
                    IconSet.FONTAWESOME -> {
                        val icons: MutableList<IconModel> = ArrayList()
                        for (icon in FontAwesome.Icon.values())
                            icons.add(IconModel(icon.name, IconSet.FONTAWESOME))
                        icons
                    }
                }
            }
        }
    }

    enum class IconSet(val value: Int, val setName: String) {
        FONTAWESOME(1, "FontAwesome");

        companion object {
            private val mapValues: MutableMap<Int, IconSet> = HashMap()
            private val mapNames: MutableMap<String, IconSet> = HashMap()

            init {
                for (iconSet in values()) {
                    mapValues[iconSet.value] = iconSet
                    mapNames[iconSet.setName] = iconSet
                }
            }

            fun valueOf(value: Int): IconSet? {
                return mapValues[value]
            }

            fun byName(setName: String): IconSet? {
                return mapNames[setName]
            }

            fun has(setName: String): Boolean {
                return mapNames.containsKey(setName)
            }
        }
    }
}