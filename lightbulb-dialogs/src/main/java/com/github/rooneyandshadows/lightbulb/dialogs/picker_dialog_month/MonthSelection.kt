package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.data.MonthEntry
import java.util.*

internal class MonthSelection(current: MonthEntry?, draft: MonthEntry?) : BaseDialogSelection<MonthEntry?>(current, draft) {
    @Override
    override fun compareValues(v1: MonthEntry?, v2: MonthEntry?): Boolean {
        if(v1==null && v2 == null) return true
        if(v1==null || v2==null) return false
        return (v1.year == v2.year && v1.month == v2.month)
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