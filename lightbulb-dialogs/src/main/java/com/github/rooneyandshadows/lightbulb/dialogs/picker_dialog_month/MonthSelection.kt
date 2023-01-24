package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialog.*

internal class MonthSelection(current: Month?, draft: Month?) : BaseDialogSelection<Month>(current, draft) {
    @Override
    override fun compareValues(v1: Month?, v2: Month?): Boolean {
        if (v1 == null && v2 == null) return true
        if (v1 == null || v2 == null) return false
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