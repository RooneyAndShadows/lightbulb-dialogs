package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.view.View
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat

@Suppress("unused")
class DialogLayoutHierarchyFooter(
    val buttonsContainer: LinearLayoutCompat?,
    val buttonPositive: Button?,
    val buttonNegative: Button?
) {
    fun isVisible(): Boolean {
        val visibility = buttonsContainer?.visibility ?: false
        return visibility == View.VISIBLE
    }
}