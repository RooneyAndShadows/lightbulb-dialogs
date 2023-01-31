package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime

import android.os.Bundle
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

    @Override
    override fun saveState(): Bundle {
        return Bundle().apply {
            getCurrentSelection().apply {
                putString(CURRENT_SELECTION_STATE_KEY, getDateString(this))
            }
            getDraftSelection().apply {
                putString(DRAFT_SELECTION_STATE_KEY, getDateString(this))
            }
        }
    }

    @Override
    override fun restoreState(source: Bundle) {
        source.apply {
            getDateFromString(getString(CURRENT_SELECTION_STATE_KEY)).apply {
                setCurrentSelection(this, false)
            }
            getDateFromString(getString(DRAFT_SELECTION_STATE_KEY)).apply {
                setDraftSelection(this, false)
            }
        }
    }

    private fun getDateFromString(dateString: String?): OffsetDateTime? {
        return DateUtilsOffsetDate.getDateFromString(DateUtilsOffsetDate.defaultFormatWithTimeZone, dateString)
    }

    private fun getDateString(date: OffsetDateTime?): String? {
        return DateUtilsOffsetDate.getDateString(DateUtilsOffsetDate.defaultFormatWithTimeZone, date)
    }
}