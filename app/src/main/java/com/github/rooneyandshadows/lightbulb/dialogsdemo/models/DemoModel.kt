package com.github.rooneyandshadows.lightbulb.dialogsdemo.models

import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils.Companion.writeString
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.icons.DemoIcons
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.color.colors.DemoColors
import android.os.Parcel
import android.os.Parcelable.Creator
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils
import java.util.*

class DemoModel : EasyAdapterDataModel {
    val id: UUID
    override val itemName: String
    val icon: DemoIcons
    val iconBackgroundColor: DemoColors

    constructor(id: UUID, title: String, icon: DemoIcons, iconBackgroundColor: DemoColors) {
        this.id = id
        itemName = title
        this.icon = icon
        this.iconBackgroundColor = iconBackgroundColor
    }

    // Parcelling part
    constructor(parcel: Parcel) {
        id = ParcelUtils.readUUID(parcel)!!
        itemName = ParcelUtils.readString(parcel)!!
        icon = DemoIcons.valueOf(ParcelUtils.readString(parcel)!!)
        iconBackgroundColor = DemoColors.valueOf(ParcelUtils.readString(parcel)!!)
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        writeString(parcel, iconBackgroundColor.colorName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<DemoModel> {
        override fun createFromParcel(parcel: Parcel): DemoModel {
            return DemoModel(parcel)
        }

        override fun newArray(size: Int): Array<DemoModel?> {
            return arrayOfNulls(size)
        }

        fun generateDemoCollection(): List<DemoModel> {
            val models: MutableList<DemoModel> = ArrayList()
            for (i in 0..24) {
                val title = "Demo Model #" + (i + 1).toString()
                models.add(DemoModel(UUID.randomUUID(), title, DemoIcons.random, DemoColors.random))
            }
            return models
        }
    }
}