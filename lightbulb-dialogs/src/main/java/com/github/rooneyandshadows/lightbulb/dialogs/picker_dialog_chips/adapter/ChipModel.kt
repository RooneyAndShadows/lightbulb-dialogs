package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter

import android.os.Parcel
import android.os.Parcelable
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel
import java.util.*

class ChipModel : EasyAdapterDataModel {
    val chipTitle: String
    var id: UUID
        private set

    init {
        id = UUID.randomUUID()
    }

    override val itemName: String
        get() = chipTitle

    constructor(title: String) : super() {
        this.chipTitle = title
    }

    // Parcelling part
    constructor(parcel: Parcel) : super() {
        chipTitle = parcel.readString()!!
        id = ParcelUtils.readUUID(parcel)!!
    }

    @Override
    override fun writeToParcel(dest: Parcel, i: Int) {
        dest.writeString(chipTitle)
        ParcelUtils.writeUUID(dest, id)
    }

    @Override
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChipModel> {
        override fun createFromParcel(parcel: Parcel): ChipModel {
            return ChipModel(parcel)
        }

        override fun newArray(size: Int): Array<ChipModel?> {
            return arrayOfNulls(size)
        }
    }
}