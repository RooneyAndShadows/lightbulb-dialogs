package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog.DateRange

internal class DateRangeSelection(current: DateRange?, draft: DateRange?) :
    BaseDialogSelection<DateRange>(current, draft) {

    init {
        if (current == null) setCurrentSelection(null, false)
        if (draft == null) setDraftSelection(null, false)
    }

    @Override
    override fun compareValues(v1: DateRange?, v2: DateRange?): Boolean {
        if ((v1 == null) && (v2 == null)) return true
        if ((v1 == null) || (v2 == null)) return false
        return v1.compare(v2)
    }

    @Override
    override fun hasCurrentSelection(): Boolean {
        val current = getCurrentSelection()
        return current != null
    }

    @Override
    override fun hasDraftSelection(): Boolean {
        val draft = getDraftSelection()
        return draft != null
    }
}