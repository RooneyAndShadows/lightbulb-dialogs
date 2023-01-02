package com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.ListPopupWindow
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyItem

abstract class DialogPropertySpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.spinnerStyle,
    mode: Int = MODE_DROPDOWN,
    popupTheme: Resources.Theme? = null,
) : AppCompatSpinner(context, attrs, defStyleAttr, mode, popupTheme), DefaultLifecycleObserver {
    private var localAdapter: DialogPropertyAdapter?
    private var selectedPosition: Int = -1
    private var lifecycleOwner: LifecycleOwner? = null
    var dialog: BaseDialogFragment? = null

    init {
        isSaveEnabled = true
        localAdapter = DialogPropertyAdapter(context, arrayOf())
    }

    @Override
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        adapter = localAdapter
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

    @Override
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        dismiss()
    }

    private fun dismiss() {
        getPopupWindow().apply {
            enterTransition = null
            exitTransition = null
            dismiss()
        }
    }

    private fun getPopupWindow(): PopupWindow {
        val listPopupWindowField = AppCompatSpinner::class.java.getDeclaredField("mPopup")
        listPopupWindowField.isAccessible = true
        val listPopupWindow = listPopupWindowField.get(this) as ListPopupWindow
        val popupWindowField = ListPopupWindow::class.java.getDeclaredField("mPopup")
        popupWindowField.isAccessible = true
        return popupWindowField.get(listPopupWindow) as PopupWindow
    }

    fun setLifecycleOwner(owner: LifecycleOwner?) {
        lifecycleOwner = owner
        if (lifecycleOwner == null) return
        lifecycleOwner!!.lifecycle.removeObserver(this)
        lifecycleOwner!!.lifecycle.addObserver(this)
    }

    fun setProperties(options: Array<DialogPropertyItem>) {
        requireAdapter { adapter ->
            adapter.setProperties(options)
        }
    }

    private fun requireAdapter(run: ((adapter: DialogPropertyAdapter) -> Unit)? = null): DialogPropertyAdapter {
        if (localAdapter == null) throw Exception("Dialog picker adapter must not be null.")
        run?.invoke(localAdapter!!)
        return localAdapter!!
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