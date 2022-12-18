package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon

import android.content.Context
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
import java.util.HashMap

class IconPickerAdapter(private val context: Context, selectableMode: EasyAdapterSelectableModes?) :
    EasyRecyclerAdapter<IconModel?>(EasyAdapterConfiguration<IconModel>().withSelectMode(selectableMode)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: AppCompatImageButton = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_icon_item, parent, false) as AppCompatImageButton
        return IconVH(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: IconModel = items.get(position)
        val vh = holder as IconVH
        vh.setItem(item, position, this)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as IconVH
        vh.release()
    }

    val itemCount: Int
        get() = items.size

    override fun getItemName(item: IconModel): String? {
        return item.itemName
    }

    fun setCollection(collection: List<IconModel>) {
        super.setCollection(validatePendingCollection(collection))
    }

    fun appendCollection(collection: List<IconModel>?) {
        super.appendCollection(validatePendingCollection(collection))
    }

    fun getDrawable(iconModel: IconModel, size: Int): Drawable? {
        var size = size
        if (size < 0) size = 0
        val icon: IIcon = getIconValue(iconModel) ?: return null
        return when (iconModel.iconSet) {
            IconSet.FONTAWESOME -> IconUtils.getIconWithAttributeColor(
                context,
                icon,
                R.attr.colorOnSurface,
                size
            )
            else -> null
        }
    }

    private fun getIconValue(iconModel: IconModel): IIcon? {
        return when (iconModel.iconSet) {
            IconSet.FONTAWESOME -> {
                val iconInputName = iconModel.iconName
                    .replace("-", "_")
                    .replace("fa_", "faw_") //To match enum name
                return try {
                    valueOf.valueOf(iconInputName)
                } catch (e: Exception) {
                    null
                }
                null
            }
            else -> null
        }
    }

    private fun validatePendingCollection(inputCollection: List<IconModel>?): List<IconModel> {
        val outputCollection: MutableList<IconModel> = ArrayList()
        for (inputIcon in inputCollection!!) {
            val icon: IIcon? = getIconValue(inputIcon)
            if (icon != null) outputCollection.add(inputIcon)
        }
        return outputCollection
    }

    inner class IconVH internal constructor(view: AppCompatImageButton?) : RecyclerView.ViewHolder(view) {
        protected var iconView: AppCompatImageButton
        protected var item: IconModel? = null
        fun setItem(item: IconModel, position: Int, adapter: IconPickerAdapter) {
            this.item = item
            val isItemSelected: Boolean = adapter.isItemSelected(position)
            val colorAttribute = if (isItemSelected) R.attr.colorAccent else R.attr.colorOnSurface
            val alpha = if (isItemSelected) 255 else (255 * 0.3).toInt()
            val iconSize: Int = ResourceUtils.getDimenPxById(context, R.dimen.dialog_icon_picker_icon_size)
            val iconDrawable: Drawable =
                IconUtils.getIconWithAttributeColor(iconView.getContext(), getIconValue(item), colorAttribute, iconSize)
            iconDrawable.alpha = alpha
            iconView.setImageDrawable(iconDrawable)
            iconView.setOnClickListener(View.OnClickListener { v: View? ->
                val prevState: Boolean = adapter.isItemSelected(position)
                adapter.selectItemAt(position, !prevState)
            })
        }

        fun release() {
            iconView.setImageDrawable(null)
        }

        init {
            iconView = itemView as AppCompatImageButton
        }
    }

    class IconModel : EasyAdapterDataModel {
        val iconName: String?
        val iconSet: IconSet?
        val itemName: String?

        constructor(iconName: String?, externalName: String?, iconSet: IconSet?) : super(false) {
            itemName = externalName
            this.iconName = iconName
            this.iconSet = iconSet
        }

        // Parcelling part
        constructor(`in`: Parcel) : super(`in`) {
            iconName = `in`.readString()
            itemName = `in`.readString()
            iconSet = IconSet.valueOf(`in`.readInt())
        }

        override fun writeToParcel(dest: Parcel, i: Int) {
            dest.writeString(iconName)
            dest.writeString(itemName)
            dest.writeInt(iconSet!!.value)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            val CREATOR: Parcelable.Creator<IconModel> = object : Parcelable.Creator<IconModel?> {
                override fun createFromParcel(`in`: Parcel): IconModel? {
                    return IconModel(`in`)
                }

                override fun newArray(size: Int): Array<IconModel?> {
                    return arrayOfNulls(size)
                }
            }

            fun getAllForSet(set: IconSet?): List<IconModel>? {
                return when (set) {
                    IconSet.FONTAWESOME -> {
                        val icons: MutableList<IconModel> = ArrayList()
                        for (icon in values.values()) icons.add(IconModel(icon.name, icon.name, IconSet.FONTAWESOME))
                        icons
                    }
                    else -> null
                }
            }
        }
    }

    enum class IconSet(val value: Int, override val name: String) {
        FONTAWESOME(1, "FontAwesome");

        companion object {
            private val mapValues: MutableMap<Int, IconSet> = HashMap()
            private val mapNames: MutableMap<String, IconSet> = HashMap()
            fun valueOf(type: Int): IconSet? {
                return mapValues[type]
            }

            fun byName(type: String): IconSet? {
                return mapNames[type]
            }

            fun has(type: String): Boolean {
                return mapNames.containsKey(type)
            }

            init {
                for (type in values()) {
                    mapValues[com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.type.value] =
                        com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.type
                    mapNames[com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.type.name] =
                        com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.type
                }
            }
        }
    }
}