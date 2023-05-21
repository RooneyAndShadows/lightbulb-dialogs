package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener

class DialogButton constructor(
    val buttonTag: String,
    buttonTitle: String = "Action",
    buttonEnabled: Boolean = true,
    var closeDialogOnClick: Boolean = true,
) : Parcelable, BaseObservable() {
    @get:Bindable
    var buttonTitle: String = buttonTitle
        set(value) {
            if (field == value) return
            field = value
            notifyPropertyChanged(com.github.rooneyandshadows.lightbulb.dialogs.BR.buttonTitle)
        }

    @get:Bindable
    var buttonEnabled: Boolean = buttonEnabled
        set(value) {
            if (field == value) return
            field = value
            notifyPropertyChanged(com.github.rooneyandshadows.lightbulb.dialogs.BR.buttonEnabled)
        }
    private val onClickListeners: List<DialogButtonClickListener> = mutableListOf()

    constructor(parcel: Parcel) : this(
        ParcelUtils.readString(parcel)!!,
        ParcelUtils.readString(parcel)!!,
        ParcelUtils.readBoolean(parcel)!!,
        ParcelUtils.readBoolean(parcel)!!
    )

    fun getOnClickListeners(): List<DialogButtonClickListener> {
        return onClickListeners.toList()
    }

    fun addOnClickListeners(listeners: List<DialogButtonClickListener>) {
        listeners.forEach {
            addOnClickListener(it)
        }
    }

    fun addOnClickListener(listener: DialogButtonClickListener) {
        if (!onClickListeners.contains(listener))
            (onClickListeners as MutableList<DialogButtonClickListener>).add(listener)
    }

    fun removeOnClickListener(listener: DialogButtonClickListener) {
        (onClickListeners as MutableList<DialogButtonClickListener>).remove(listener)
    }

    @Override
    override fun writeToParcel(dest: Parcel, flags: Int) {
        ParcelUtils.writeString(dest, buttonTag)
            .writeString(dest, buttonTitle)
            .writeBoolean(dest, buttonEnabled)
            .writeBoolean(dest, closeDialogOnClick)
    }

    @Override
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DialogButton> {
        @Override
        override fun createFromParcel(parcel: Parcel): DialogButton {
            return DialogButton(parcel)
        }

        @Override
        override fun newArray(size: Int): Array<DialogButton?> {
            return arrayOfNulls(size)
        }
    }
}