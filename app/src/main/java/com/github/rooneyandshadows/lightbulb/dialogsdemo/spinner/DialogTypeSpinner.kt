package com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.DefaultLifecycleObserver
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getAllAsDialogPropertyItems
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.DialogPropertySpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyAdapter

class DialogTypeSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.spinnerStyle,
    mode: Int = MODE_DROPDOWN,
    popupTheme: Resources.Theme? = null,
) : DialogPropertySpinner(context, attrs, defStyleAttr, mode, popupTheme), DefaultLifecycleObserver {
    init {
        setProperties(DialogTypes.getAllAsDialogPropertyItems())
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val adapter = adapter as DialogPropertyAdapter
                val dialogType = DialogTypes.valueOf(adapter.getItem(position).value)
                dialog?.setDialogType(dialogType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}