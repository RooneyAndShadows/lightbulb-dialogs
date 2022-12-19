package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import java.util.*

internal class TimeSelection(current: IntArray?, draft: IntArray?) : BaseDialogSelection<IntArray?>(current, draft) {
    override fun compareValues(v1: IntArray?, v2: IntArray?): Boolean {
        return Arrays.equals(v1, v2)
    }

    override fun hasCurrentSelection(): Boolean {
        return currentSelection != null
    }

    override fun hasDraftSelection(): Boolean {
        return draftSelection != null
    }
}