package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection
import java.util.*

internal class AdapterPickerDialogSelection(current: IntArray?, draft: IntArray?) :
    BaseDialogSelection<IntArray>(current, draft) {
    override fun compareValues(v1: IntArray?, v2: IntArray?): Boolean {
        if ((v1 == null) && (v2 == null)) return true
        val first = v1 ?: intArrayOf()
        val second = v2 ?: intArrayOf()
        return first.contentEquals(second)
    }

    override fun hasCurrentSelection(): Boolean {
        val currentSelection = getCurrentSelection()
        return currentSelection != null && currentSelection.isNotEmpty()
    }

    override fun hasDraftSelection(): Boolean {
        val draftSelection = getDraftSelection()
        return draftSelection != null && draftSelection.isNotEmpty()
    }

    @Override
    override fun saveState(): Bundle {
        return Bundle().apply {
            getCurrentSelection()?.apply {
                putIntArray(CURRENT_SELECTION_STATE_KEY, this)
            }
            getDraftSelection()?.apply {
                putIntArray(DRAFT_SELECTION_STATE_KEY, this)
            }
        }
    }

    @Override
    override fun restoreState(source: Bundle) {
        source.apply {
            getIntArray(CURRENT_SELECTION_STATE_KEY).apply {
                setCurrentSelection(this, false)
            }
            getIntArray(DRAFT_SELECTION_STATE_KEY).apply {
                setDraftSelection(this, false)
            }
        }
    }
}