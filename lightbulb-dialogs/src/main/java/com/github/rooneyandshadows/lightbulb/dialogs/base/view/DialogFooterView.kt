package com.github.rooneyandshadows.lightbulb.dialogs.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration

class DialogFooterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {
    val positiveButton: Button by lazy {
        return@lazy findViewById<Button>(R.id.dialog_positive_button)!!
    }
    val negativeButton: Button by lazy {
        return@lazy findViewById<Button>(R.id.dialog_negative_button)!!
    }
    val buttonsContainer: LinearLayoutCompat by lazy {
        return@lazy findViewById<LinearLayoutCompat>(R.id.dialog_buttons_container)!!
    }
    private var dialog: BaseDialogFragment? = null

    init {
        inflate(context, R.layout.layout_dialog_header_view, this)
    }

    fun initialize(dialog: BaseDialogFragment) {
        this.dialog = dialog
        dialog.dialogButtons.forEach {
            syncButton(positiveButton, dialog.dialogNegativeButtonConfiguration)
        }

        syncButton(negativeButton, dialog.dialogPositiveButtonConfiguration)
    }

    private fun syncButton(target: Button, config: DialogButtonConfiguration?) {
        target.apply {
            if (config == null || config.buttonTitle.isBlank()) {
                visibility = GONE
                return@apply
            }
            val alpha = if (config.buttonEnabled) 255 else 140
            val textColor = target.textColors.withAlpha(alpha)
            setTextColor(textColor)
            isEnabled = config.buttonEnabled
            text = config.buttonTitle
            setOnClickListener { view: View? ->
                dialog?.apply {
                    for (listener in config.onClickListeners) listener.doOnClick(view, this)
                    if (config.closeDialogOnClick) dismiss()
                }
            }
        }
    }

    private fun syncVisibility() {
        visibility = if (dialog!!.dialogPositiveButtonConfiguration == null && dialog!!.dialogNegativeButtonConfiguration == null) GONE
        else VISIBLE
    }
}