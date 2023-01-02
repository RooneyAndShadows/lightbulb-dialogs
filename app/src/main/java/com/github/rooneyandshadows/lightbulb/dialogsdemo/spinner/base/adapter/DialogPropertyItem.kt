package com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter

import android.os.Parcel
import android.os.Parcelable

class DialogPropertyItem : Parcelable {
    val name: String
    val value: Int

    constructor(parcel: Parcel) {
        name = parcel.readString()!!
        value = parcel.readInt()
    }

    constructor(name: String, value: Int) {
        this.name = name
        this.value = value
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DialogPropertyItem> {
        override fun createFromParcel(parcel: Parcel): DialogPropertyItem {
            return DialogPropertyItem(parcel)
        }

        override fun newArray(size: Int): Array<DialogPropertyItem?> {
            return arrayOfNulls(size)
        }
    }
}