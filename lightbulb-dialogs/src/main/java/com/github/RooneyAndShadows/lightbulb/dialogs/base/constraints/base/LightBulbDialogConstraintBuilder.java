package com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.base;

import android.app.Activity;

import com.github.rooneyandshadows.lightbulb.commons.utils.WindowUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;

public class BaseDialogConstraintBuilder {
    protected final LightBulbDialogFragment dialogFragment;

    public BaseDialogConstraintBuilder(LightBulbDialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    protected int validatePercentNumber(int percentVal) {
        if (percentVal > 100)
            percentVal = 100;
        if (percentVal < 0)
            percentVal = 0;
        return percentVal;
    }

    protected final int getPercentOfWindowHeight(int heightInPercents) {
        if (heightInPercents < 0)
            heightInPercents = 0;
        if (heightInPercents > 100)
            heightInPercents = 100;
        return getWindowHeight() * heightInPercents / 100;
    }

    protected final int getPercentOfWindowWidth(int widthInPercents) {
        if (widthInPercents < 0)
            widthInPercents = 0;
        if (widthInPercents > 100)
            widthInPercents = 100;
        return getWindowWidth() * widthInPercents / 100;
    }

    protected final int getWindowHeight() {
        Activity activity = (Activity) dialogFragment.getContext();
        if (activity == null)
            return -1;
        return WindowUtils.getWindowHeight(activity);
    }

    protected final int getWindowWidth() {
        Activity activity = (Activity) dialogFragment.getContext();
        if (activity == null)
            return -1;
        return WindowUtils.getWindowWidth(activity);
    }
}
