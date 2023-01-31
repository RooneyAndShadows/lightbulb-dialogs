package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
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
        source.apply {
            val clazz = Time::class.java
            BundleUtils.getParcelable(CURRENT_SELECTION_STATE_KEY, this, clazz)?.apply {
                setCurrentSelection(this, false)
            }
            BundleUtils.getParcelable(DRAFT_SELECTION_STATE_KEY, this, clazz)?.apply {
                setDraftSelection(this, false)
            }
        }
    }
}