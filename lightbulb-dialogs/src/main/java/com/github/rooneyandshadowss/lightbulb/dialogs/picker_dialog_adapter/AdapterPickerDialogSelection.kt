package com.github.rooneyandshadowss.lightbulb.dialogs.picker_dialog_adapter

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import java.util.*

internal class AdapterPickerDialogSelection(current: IntArray?, draft: IntArray?) :
    BaseDialogSelection<IntArray?>(current, draft) {
    override fun compareValues(v1: IntArray?, v2: IntArray?): Boolean {
        return Arrays.equals(v1, v2)
    }

    override fun hasCurrentSelection(): Boolean {
        val currentSelection = getCurrentSelection()
        return currentSelection != null && currentSelection.isNotEmpty()
    }

    override fun hasDraftSelection(): Boolean {
        val draftSelection = getDraftSelection()
        return draftSelection != null && draftSelection.isNotEmpty()
    }
}