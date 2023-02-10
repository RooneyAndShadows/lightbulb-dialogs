package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.view.View.*
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat

@Suppress("unused")
class DialogLayoutHierarchyHeading(
    val titleAndMessageContainer: LinearLayoutCompat?,
    val titleTextView: TextView?,
    val messageTextView: TextView?
) {
    fun isVisible(): Boolean {
        val visibility = titleAndMessageContainer?.visibility ?: false
        return visibility == VISIBLE
    }
}