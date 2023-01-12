package com.github.rooneyandshadowss.lightbulb.dialogs.base.constraints.base

import android.app.Activity
import com.github.rooneyandshadows.lightbulb.commons.utils.WindowUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment

open class BaseDialogConstraintBuilder(protected val dialogFragment: BaseDialogFragment) {
    protected fun validatePercentNumber(percentVal: Int): Int {
        var actualPercents = percentVal
        if (actualPercents > 100) actualPercents = 100
        if (actualPercents < 0) actualPercents = 0
        return actualPercents
    }

    protected fun getPercentOfWindowHeight(heightInPercents: Int): Int {
        return windowHeight * validatePercentNumber(heightInPercents) / 100
    }

    protected fun getPercentOfWindowWidth(widthInPercents: Int): Int {
        return windowWidth * validatePercentNumber(widthInPercents) / 100
    }

    protected val windowHeight: Int
        get() {
            val activity = dialogFragment.context as Activity? ?: return -1
            return WindowUtils.getWindowHeight(activity)
        }
    protected val windowWidth: Int
        get() {
            val activity = dialogFragment.context as Activity? ?: return -1
            return WindowUtils.getWindowWidth(activity)
        }
}