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
    val buttonsContainer: LinearLayoutCompat by lazy {
        return@lazy findViewById<LinearLayoutCompat>(R.id.dialog_buttons_container)!!
    }
    private var dialog: BaseDialogFragment? = null

    init {
        inflate(context, R.layout.layout_dialog_header_view, this)
    }

    fun initialize(dialog: BaseDialogFragment) {
        this.dialog = dialog
        buttonsContainer.apply {
            removeAllViews()
            dialog.dialogButtons.forEach {
                val button = inflateButton(it)
                addView(button)
            }
        }
        buttonsContainer.removeAllViews()
        visibility = if (buttonsContainer.childCount > 0) VISIBLE else GONE
    }

    private fun inflateButton(config: DialogButtonConfiguration): Button {
        return Button(context, null, 0, R.style.DialogButton).apply {
            val alpha = if (config.buttonEnabled) 255 else 140
            val textColor = textColors.withAlpha(alpha)
            setTextColor(textColor)
            isEnabled = config.buttonEnabled
            text = config.buttonTitle
            tag = config.buttonTag
            setOnClickListener { view: View? ->
                dialog?.apply {
                    for (listener in config.onClickListeners) listener.doOnClick(view, this)
                    if (config.closeDialogOnClick) dismiss()
                }
            }
        }
    }
}