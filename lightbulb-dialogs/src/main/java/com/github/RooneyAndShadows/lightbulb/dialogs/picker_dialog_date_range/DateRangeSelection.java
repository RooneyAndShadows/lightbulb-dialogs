package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection;

import java.time.OffsetDateTime;
import java.util.Arrays;

class DateRangeSelection extends BaseDialogSelection<OffsetDateTime[]> {
    public DateRangeSelection(OffsetDateTime[] current, OffsetDateTime[] draft) {
        super(current, draft);
        if (current == null)
            setCurrentSelection(new OffsetDateTime[]{null, null}, false);
        if (draft == null)
            setDraftSelection(new OffsetDateTime[]{null, null}, false);
    }

    @Override
    public boolean compareValues(OffsetDateTime[] v1, OffsetDateTime[] v2) {
        return Arrays.equals(v1, v2);
    }

    @Override
    public void setCurrentSelection(OffsetDateTime[] newValue, boolean notify) {
        if (newValue == null)
            newValue = new OffsetDateTime[]{null, null};
        super.setCurrentSelection(newValue, notify);
    }

    @Override
    public void setDraftSelection(OffsetDateTime[] newValue, boolean notify) {
        if (newValue == null)
            newValue = new OffsetDateTime[]{null, null};
        super.setDraftSelection(newValue, notify);
    }

    @Override
    public boolean hasCurrentSelection() {
        OffsetDateTime[] current = getCurrentSelection();
        return current != null && current[0] != null && current[1] != null;
    }

    @Override
    public boolean hasDraftSelection() {
        OffsetDateTime[] draft = getDraftSelection();
        return draft != null && draft[0] != null && draft[1] != null;
    }
}