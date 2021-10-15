package com.rands.lightbulb.dialogs.base.constraints.regular;

import android.content.res.Configuration;

import com.rands.lightbulb.dialogs.base.BaseDialogFragment;
import com.rands.lightbulb.dialogs.base.constraints.base.BaseDialogConstraintBuilder;

@SuppressWarnings("FieldCanBeLocal")
public class RegularDialogConstraintsBuilder extends BaseDialogConstraintBuilder {
    private final int DEF_MAX_WIDTH_PERCENTS_PORTRAIT = 85;
    private final int DEF_MAX_WIDTH_PERCENTS_LANDSCAPE = 65;
    private final int DEF_MAX_HEIGHT_PERCENTS = 85;
    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;

    public RegularDialogConstraintsBuilder(BaseDialogFragment dialogFragment) {
        super(dialogFragment);
    }

    public RegularDialogConstraintsBuilder Default() {
        int orientation = dialogFragment.getResources().getConfiguration().orientation;
        int maxWidth = getPercentOfWindowWidth(
                orientation == Configuration.ORIENTATION_PORTRAIT ?
                        DEF_MAX_WIDTH_PERCENTS_PORTRAIT : DEF_MAX_WIDTH_PERCENTS_LANDSCAPE
        );
        int maxHeight = getPercentOfWindowHeight(DEF_MAX_HEIGHT_PERCENTS);
        this.minWidth = 0;
        this.maxWidth = maxWidth;
        this.minHeight = 0;
        this.maxHeight = maxHeight;
        return this;
    }

    public RegularDialogConstraintsBuilder withMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    public RegularDialogConstraintsBuilder withMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public RegularDialogConstraintsBuilder withMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    public RegularDialogConstraintsBuilder withMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public RegularDialogConstraintsBuilder withMinWidthInPercents(int minWidthPercent) {
        minWidthPercent = validatePercentNumber(minWidthPercent);
        minWidth = getPercentOfWindowWidth(minWidthPercent);
        return this;
    }

    public RegularDialogConstraintsBuilder withMaxWidthInPercents(int maxWidthPercent) {
        maxWidthPercent = validatePercentNumber(maxWidthPercent);
        maxWidth = getPercentOfWindowWidth(maxWidthPercent);
        return this;
    }

    public RegularDialogConstraintsBuilder withMinHeightInPercents(int minHeightPercent) {
        minHeightPercent = validatePercentNumber(minHeightPercent);
        minHeight = getPercentOfWindowHeight(minHeightPercent);
        return this;
    }

    public RegularDialogConstraintsBuilder withMaxHeightInPercents(int maxHeightPercent) {
        maxHeightPercent = validatePercentNumber(maxHeightPercent);
        maxHeight = getPercentOfWindowHeight(maxHeightPercent);
        return this;
    }

    public RegularDialogConstraints build() {
        return new RegularDialogConstraints(minWidth, maxWidth, minHeight, maxHeight);
    }
}
