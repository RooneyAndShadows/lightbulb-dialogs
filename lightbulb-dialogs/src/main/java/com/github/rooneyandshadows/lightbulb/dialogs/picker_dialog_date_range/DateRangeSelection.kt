package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
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

    @Override
    override fun saveState(): Bundle {
        return Bundle().apply bundle@{
            getCurrentSelection()?.apply {
                BundleUtils.putParcelable(CURRENT_SELECTION_STATE_KEY, this@bundle, this)
            }
            getDraftSelection()?.apply {
                BundleUtils.putParcelable(DRAFT_SELECTION_STATE_KEY, this@bundle, this)
            }
        }
    }

    @Override
    override fun restoreState(source: Bundle) {
        val clazz = DateRange::class.java
        source.apply {
            BundleUtils.getParcelable(CURRENT_SELECTION_STATE_KEY, this, clazz)?.apply {
                setCurrentSelection(this, false)
            }
            BundleUtils.getParcelable(DRAFT_SELECTION_STATE_KEY, this, clazz)?.apply {
                setDraftSelection(this, false)
            }
        }
    }
}