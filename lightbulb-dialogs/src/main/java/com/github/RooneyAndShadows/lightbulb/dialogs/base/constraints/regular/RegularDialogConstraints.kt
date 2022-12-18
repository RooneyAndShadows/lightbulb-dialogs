package com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular

import kotlin.math.max
import kotlin.math.min

class RegularDialogConstraints(
    private val minWidth: Int,
    private val maxWidth: Int,
    private val minHeight: Int,
    private val maxHeight: Int
) {
    fun resolveWidth(desiredWidth: Int): Int {
        var result = desiredWidth
        if (result > getMaxWidth()) result = getMaxWidth()
        if (result < getMinWidth()) result = getMinWidth()
        return result
    }

    fun resolveHeight(desiredHeight: Int): Int {
        var result = desiredHeight
        if (result > getMaxHeight()) result = getMaxHeight()
        if (result < getMinHeight()) result = getMinHeight()
        return result
    }

    fun getMinWidth(): Int {
        return min(minWidth, maxWidth)
    }

    fun getMaxWidth(): Int {
        return max(maxWidth, minWidth)
    }

    fun getMinHeight(): Int {
        return min(minHeight, maxHeight)
    }

    fun getMaxHeight(): Int {
        return max(maxHeight, minHeight)
    }
}