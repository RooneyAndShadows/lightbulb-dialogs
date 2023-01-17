package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import java.time.OffsetDateTime
import java.util.*

internal class DateRangeSelection(current: Array<OffsetDateTime?>?, draft: Array<OffsetDateTime?>?) :
    BaseDialogSelection<Array<OffsetDateTime?>?>(current, draft) {

    init {
        if (current == null) setCurrentSelection(arrayOf(null, null), false)
        if (draft == null) setDraftSelection(arrayOf(null, null), false)
    }

    @Override
    override fun compareValues(v1: Array<OffsetDateTime?>?, v2: Array<OffsetDateTime?>?): Boolean {
        if ((v1 == null || v1.isEmpty()) && (v2 == null || v2.isEmpty())) return true
        if ((v1 == null || v1.isEmpty()) || (v2 == null || v2.isEmpty())) return false
        if (v1.size != v2.size) return false
        return compareDates(v1.first(), v2.first()) && compareDates(v1.last(), v2.last())
    }

    @Override
    override fun setCurrentSelection(newValue: Array<OffsetDateTime?>?, notify: Boolean) {
        var currentSelection = newValue
        if (currentSelection == null) currentSelection = arrayOf(null, null)
        super.setCurrentSelection(currentSelection, notify)
    }

    @Override
    override fun setDraftSelection(newValue: Array<OffsetDateTime?>?, notify: Boolean) {
        var draftSelection = newValue
        if (draftSelection == null) draftSelection = arrayOf(null, null)
        super.setDraftSelection(draftSelection, notify)
    }

    @Override
    override fun hasCurrentSelection(): Boolean {
        val current = getCurrentSelection()
        return current != null && current[0] != null && current[1] != null
    }

    @Override
    override fun hasDraftSelection(): Boolean {
        val draft = getDraftSelection()
        return draft != null && draft[0] != null && draft[1] != null
    }

    private fun compareDates(testDate: OffsetDateTime?, target: OffsetDateTime?): Boolean {
        return DateUtilsOffsetDate.isDateEqual(testDate, target, false)
    }
}