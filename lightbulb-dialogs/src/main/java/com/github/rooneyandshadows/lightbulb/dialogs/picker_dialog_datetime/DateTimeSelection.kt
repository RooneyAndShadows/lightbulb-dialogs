package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import java.time.OffsetDateTime

internal class DateTimeSelection(current: OffsetDateTime?, draft: OffsetDateTime?) :
    BaseDialogSelection<OffsetDateTime>(current, draft) {
    @Override
    override fun compareValues(v1: OffsetDateTime?, v2: OffsetDateTime?): Boolean {
        return DateUtilsOffsetDate.isDateEqual(v1, v2, true)
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