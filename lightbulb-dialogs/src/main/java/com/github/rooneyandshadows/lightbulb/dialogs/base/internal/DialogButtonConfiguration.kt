package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.os.Parcel
import android.os.Parcelable
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils

class DialogButtonConfiguration @JvmOverloads constructor(
    val buttonTitle: String = "Action",
    val buttonEnabled: Boolean = true,
    val closeDialogOnClick: Boolean = true
) : Parcelable {

    constructor(parcel: Parcel) : this(
        ParcelUtils.readString(parcel)!!,
        ParcelUtils.readBoolean(parcel)!!,
        ParcelUtils.readBoolean(parcel)!!
    )

    @Override
    override fun writeToParcel(dest: Parcel, flags: Int) {
        ParcelUtils.writeString(dest, buttonTitle)
            .writeBoolean(dest, buttonEnabled)
            .writeBoolean(dest, closeDialogOnClick)
    }

    @Override
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DialogButtonConfiguration> {
        @Override
        override fun createFromParcel(parcel: Parcel): DialogButtonConfiguration {
            return DialogButtonConfiguration(parcel)
        }

        @Override
        override fun newArray(size: Int): Array<DialogButtonConfiguration?> {
            return arrayOfNulls(size)
        }
    }
}