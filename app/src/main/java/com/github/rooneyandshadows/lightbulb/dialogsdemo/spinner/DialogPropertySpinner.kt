package com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import androidx.appcompat.widget.AppCompatSpinner
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.adapter.DialogPropertyAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.adapter.DialogPropertyItem

class DialogPropertySpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.spinnerStyle,
    mode: Int = MODE_DROPDOWN,
    popupTheme: Resources.Theme? = null,
) : AppCompatSpinner(context, attrs, defStyleAttr, mode, popupTheme) {
    private var adapter: DialogPropertyAdapter?
    private var selectedPosition: Int = -1

    init {
        isSaveEnabled = true
        adapter = DialogPropertyAdapter(context, arrayOf())
    }

    @Override
    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchFreezeSelfOnly(container)
    }

    @Override
    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchThawSelfOnly(container)
    }

    @Override
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val myState = SavedState(superState)
        requireAdapter { adapter ->
            myState.adapterState = adapter.saveInstanceState()
        }
        myState.selectedPosition = selectedPosition
        return myState
    }

    @Override
    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        requireAdapter { adapter ->
            adapter.restoreInstanceState(savedState.adapterState!!)
        }
        selectedPosition = savedState.selectedPosition
        setSelection(selectedPosition)
    }

    fun setProperties(options: Array<DialogPropertyItem>) {
        requireAdapter { adapter ->
            adapter.setProperties(options)
        }
    }

    private fun requireAdapter(run: ((adapter: DialogPropertyAdapter) -> Unit)? = null): DialogPropertyAdapter {
        if (adapter == null) throw Exception("Dialog picker adapter must not be null.")
        run?.invoke(adapter!!)
        return adapter!!
    }

    private class SavedState : BaseSavedState {
        var adapterState: Bundle? = null
        var selectedPosition: Int = -1

        constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            adapterState = parcel.readBundle(DialogPropertySpinner::class.java.classLoader)
            selectedPosition = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeBundle(adapterState)
            out.writeInt(selectedPosition)
        }
    }
}