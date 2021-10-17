package com.github.RooneyAndShadows.lightbulb.dialogs.picker_dialog_datetime;

import com.github.RooneyAndShadows.commons.date.DateUtils;
import com.github.RooneyAndShadows.lightbulb.dialogs.base.BaseDialogSelection;

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