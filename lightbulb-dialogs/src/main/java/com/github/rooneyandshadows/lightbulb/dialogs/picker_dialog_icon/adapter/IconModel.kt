package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter

import android.os.Parcel
import android.os.Parcelable
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconSet.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import java.lang.Exception

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
                FONTAWESOME -> {
                    val icons: MutableList<IconModel> = mutableListOf()
                    for (icon in FontAwesome.Icon.values()) icons.add(IconModel(icon.name, FONTAWESOME))
                    icons
                }
            }
        }

        @JvmStatic
        internal fun getIconValue(iconModel: IconModel): IIcon? {
            return when (iconModel.iconSet) {
                FONTAWESOME -> {
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
    }
}