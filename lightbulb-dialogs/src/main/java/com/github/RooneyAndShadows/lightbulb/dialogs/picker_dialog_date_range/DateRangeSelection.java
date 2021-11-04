package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection;

import java.util.Arrays;
import java.util.Date;

class DateRangeSelection extends BaseDialogSelection<Date[]> {
    public DateRangeSelection(Date[] current, Date[] draft) {
        super(current, draft);
        if (current == null)
            setCurrentSelection(new Date[]{null, null}, false);
        if (draft == null)
            setDraftSelection(new Date[]{null, null}, false);
    }

    @Override
    public boolean compareValues(Date[] v1, Date[] v2) {
        return Arrays.equals(v1, v2);
    }

    @Override
    public void setCurrentSelection(Date[] newValue, boolean notify) {
        if (newValue == null)
            newValue = new Date[]{null, null};
        super.setCurrentSelection(newValue, notify);
    }

    @Override
    public void setDraftSelection(Date[] newValue, boolean notify) {
        if (newValue == null)
            newValue = new Date[]{null, null};
        super.setDraftSelection(newValue, notify);
    }

    @Override
    public boolean hasCurrentSelection() {
        Date[] current = getCurrentSelection();
        return current != null && current[0] != null && current[1] != null;
    }

    @Override
    public boolean hasDraftSelection() {
        Date[] draft = getDraftSelection();
        return draft != null && draft[0] != null && draft[1] != null;
    }
}