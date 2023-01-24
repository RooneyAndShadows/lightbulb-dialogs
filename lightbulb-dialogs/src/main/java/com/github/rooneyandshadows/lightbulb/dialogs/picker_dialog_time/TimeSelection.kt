package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialog.Time

internal class TimeSelection(current: Time?, draft: Time?) : BaseDialogSelection<Time>(current, draft) {
    @Override
    override fun compareValues(v1: Time?, v2: Time?): Boolean {
        if (v1 == null && v2 == null) return true
        if (v1 == null || v2 == null) return false
        return (v1.hour == v2.hour && v1.minute == v2.minute)
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