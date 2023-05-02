package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.adapter

import android.os.Parcel
import android.os.Parcelable
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel

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