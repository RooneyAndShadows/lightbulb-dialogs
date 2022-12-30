package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import java.util.*

internal class MonthSelection(current: IntArray?, draft: IntArray?) : BaseDialogSelection<IntArray?>(current, draft) {
    @Override
    override fun compareValues(v1: IntArray?, v2: IntArray?): Boolean {
        return Arrays.equals(v1, v2)
    }

    @Override
    override fun hasCurrentSelection(): Boolean {
        return getCurrentSelection() != null
    }

    @Override
    override fun hasDraftSelection(): Boolean {
        return getDraftSelection() != null
    }
}