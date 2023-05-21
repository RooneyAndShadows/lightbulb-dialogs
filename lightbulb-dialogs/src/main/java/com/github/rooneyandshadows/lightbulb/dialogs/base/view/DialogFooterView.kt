package com.github.rooneyandshadows.lightbulb.dialogs.base.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButton
import com.github.rooneyandshadows.lightbulb.dialogs.databinding.LayoutDialogButtonBinding

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
        inflate(context, R.layout.layout_dialog_footer_view, this)
    }


    fun initialize(dialog: BaseDialogFragment) {
        this.dialog = dialog
        inflateButtons(dialog.getButtons())
        visibility = if (buttonsContainer.childCount > 0) VISIBLE else GONE
    }

    private fun inflateButtons(dialogButtons: List<DialogButton>) {
        val layoutInflater = LayoutInflater.from(context)
        val buttonTextColor = ResourceUtils.getColorByAttribute(context, R.attr.colorPrimary)
        val textColors = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf()
            ), intArrayOf(
                ColorUtils.setAlphaComponent(buttonTextColor, 140),
                buttonTextColor
            )
        )
        buttonsContainer.apply {
            removeAllViews()
            dialogButtons.forEach { dialogButtonConfiguration ->
                DataBindingUtil.inflate<LayoutDialogButtonBinding>(
                    layoutInflater,
                    R.layout.layout_dialog_button,
                    buttonsContainer,
                    true
                ).apply {
                    buttonConfiguration = dialogButtonConfiguration
                    (root as Button).apply {
                        setTextColor(textColors)
                        setOnClickListener {
                            dialog?.apply {
                                for (listener in dialogButtonConfiguration.getOnClickListeners())
                                    listener.doOnClick(view, this)
                                if (dialogButtonConfiguration.closeDialogOnClick) dismiss()
                            }
                        }
                    }
                }
            }
        }
    }
}