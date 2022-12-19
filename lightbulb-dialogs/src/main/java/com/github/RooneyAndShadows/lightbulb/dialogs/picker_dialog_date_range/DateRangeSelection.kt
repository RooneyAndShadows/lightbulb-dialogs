package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import java.time.OffsetDateTime
import java.util.*

internal class DateRangeSelection(current: Array<OffsetDateTime?>?, draft: Array<OffsetDateTime?>?) :
    BaseDialogSelection<Array<OffsetDateTime?>?>(current, draft) {
    override fun compareValues(v1: Array<OffsetDateTime?>?, v2: Array<OffsetDateTime?>?): Boolean {
        return Arrays.equals(v1, v2)
    }

    override fun setCurrentSelection(newValue: Array<OffsetDateTime?>?, notify: Boolean) {
        var newValue = newValue
        if (newValue == null) newValue = arrayOf(null, null)
        super.setCurrentSelection(newValue, notify)
    }

    override fun setDraftSelection(newValue: Array<OffsetDateTime?>?, notify: Boolean) {
        var newValue = newValue
        if (newValue == null) newValue = arrayOf(null, null)
        super.setDraftSelection(newValue, notify)
    }

    override fun hasCurrentSelection(): Boolean {
        val current = currentSelection
        return current != null && current[0] != null && current[1] != null
    }

    override fun hasDraftSelection(): Boolean {
        val draft = draftSelection
        return draft != null && draft[0] != null && draft[1] != null
    }

    init {
        if (current == null) setCurrentSelection(arrayOf(null, null), false)
        if (draft == null) setDraftSelection(arrayOf(null, null), false)
    }
}