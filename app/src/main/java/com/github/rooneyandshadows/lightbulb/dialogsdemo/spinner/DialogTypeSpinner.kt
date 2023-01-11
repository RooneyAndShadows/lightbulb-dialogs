package com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.DefaultLifecycleObserver
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes.TRANSITION_FROM_BOTTOM_TO_BOTTOM
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getAllAsDialogPropertyItems
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.DialogPropertySpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.base.adapter.DialogPropertyItem

@Suppress("SameParameterValue")
class DialogTypeSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.spinnerStyle,
    mode: Int = MODE_DROPDOWN,
    popupTheme: Resources.Theme? = null,
) : DialogPropertySpinner(context, attrs, defStyleAttr, mode, popupTheme), DefaultLifecycleObserver {
    var animationTypeSpinner: DialogAnimationTypeSpinner? = null

    init {
        setProperties(DialogTypes.getAllAsDialogPropertyItems())
        setupSelectionChangedListener()
    }

    private fun setupSelectionChangedListener() {
        onItemSelectedListener = object : OnItemSelectedListener {
            @Override
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val adapter = adapter as DialogPropertyAdapter
                val dialogType = DialogTypes.valueOf(adapter.getItem(position).value)
                dialog?.dialogType = dialogType
                (animationTypeSpinner?.selectedItem as DialogPropertyItem?)?.apply {
                    val currentAnimation = DialogAnimationTypes.valueOf(value)
                    if (dialogType == DialogTypes.BOTTOM_SHEET && currentAnimation != TRANSITION_FROM_BOTTOM_TO_BOTTOM) {
                        val positionToSelect = findPositionOfAnimationType(TRANSITION_FROM_BOTTOM_TO_BOTTOM)
                        animationTypeSpinner!!.setSelection(positionToSelect)
                        val message = ResourceUtils.getPhrase(
                            context,
                            R.string.demo_dialog_animation_must_be_bottomsheet_text
                        )
                        InteractionUtils.showMessage(context, message)
                    }
                }
            }

            @Override
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            private fun findPositionOfAnimationType(animationType: DialogAnimationTypes): Int {
                val position = -1
                val adapter = animationTypeSpinner?.adapter ?: return position
                for (i in 0 until adapter.count) {
                    val currentItem = adapter.getItem(i) as DialogPropertyItem
                    val currentAnimationType = DialogAnimationTypes.valueOf(currentItem.value)
                    if (currentAnimationType == animationType) return i
                }
                return position
            }
        }
    }
}