package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogSelection;

import java.util.Arrays;

class TimeSelection extends BaseDialogSelection<int[]> {
        public TimeSelection(int[] current, int[] draft) {
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