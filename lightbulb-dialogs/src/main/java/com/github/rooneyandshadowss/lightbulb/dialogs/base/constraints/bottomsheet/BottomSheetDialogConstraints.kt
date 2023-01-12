package com.github.rooneyandshadowss.lightbulb.dialogs.base.constraints.bottomsheet

import kotlin.math.max
import kotlin.math.min

class BottomSheetDialogConstraints(private val minHeight: Int, private val maxHeight: Int) {
    fun resolveHeight(desiredHeight: Int): Int {
        var desired = desiredHeight
        if (desired > getMaxHeight())
            desired = getMaxHeight()
        if (desired < getMinHeight())
            desired = getMinHeight()
        return desired
    }

    fun getMinHeight(): Int {
        return min(minHeight, maxHeight)
    }

    fun getMaxHeight(): Int {
        return max(maxHeight, minHeight)
    }
}