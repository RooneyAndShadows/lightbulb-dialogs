package com.rands.lightbulb.dialogs.picker_dialog_datetime;

import com.rands.lightbulb.dialogs.base.BaseDialogSelection;
import com.rands.java.commons.date.DateUtils;

import java.util.Date;

class DateTimeSelection extends BaseDialogSelection<Date> {
    public DateTimeSelection(Date current, Date draft) {
        super(current, draft);
    }

    @Override
    public boolean compareValues(Date v1, Date v2) {
        return DateUtils.isDateEqual(v1, v2, true);
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