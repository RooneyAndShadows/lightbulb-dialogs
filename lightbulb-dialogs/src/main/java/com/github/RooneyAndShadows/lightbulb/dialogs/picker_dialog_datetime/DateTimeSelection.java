package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime;

import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection;

import java.time.OffsetDateTime;

class DateTimeSelection extends BaseDialogSelection<OffsetDateTime> {
    public DateTimeSelection(OffsetDateTime current, OffsetDateTime draft) {
        super(current, draft);
    }

    @Override
    public boolean compareValues(OffsetDateTime v1, OffsetDateTime v2) {
        return DateUtilsOffsetDate.isDateEqual(v1, v2, true);
    }

    @Override
    public boolean hasCurrentSelection() {
        return getCurrentSelection() != null;
    }

    @Override
    public boolean hasDraftSelection() {
        return getDraftSelection() != null;
    }
}