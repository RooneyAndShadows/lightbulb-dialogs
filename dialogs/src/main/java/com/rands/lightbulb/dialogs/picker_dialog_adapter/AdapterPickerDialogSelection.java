package com.rands.lightbulb.dialogs.picker_dialog_adapter;

import com.rands.lightbulb.dialogs.base.BaseDialogSelection;

import java.util.Arrays;

class AdapterPickerDialogSelection extends BaseDialogSelection<int[]> {
    public AdapterPickerDialogSelection(int[] current, int[] draft) {
        super(current, draft);
    }

    @Override
    public boolean compareValues(int[] v1, int[] v2) {
        return Arrays.equals(v1, v2);
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