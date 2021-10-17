package com.github.RooneyAndShadows.lightbulb.dialogs.base.constraints.bottomsheet;

import com.github.RooneyAndShadows.lightbulb.dialogs.base.BaseDialogFragment;
import com.github.RooneyAndShadows.lightbulb.dialogs.base.constraints.base.BaseDialogConstraintBuilder;

@SuppressWarnings("FieldCanBeLocal")
public class BottomSheetDialogConstraintsBuilder extends BaseDialogConstraintBuilder {
    private final int DEF_MAX_HEIGHT_PERCENTS = 65;
    private final int DEF_MIN_HEIGHT_PERCENTS = 10;
    private int minHeight;
    private int maxHeight;

    public BottomSheetDialogConstraintsBuilder(BaseDialogFragment dialogFragment) {
        super(dialogFragment);
    }

    public BottomSheetDialogConstraintsBuilder Default() {
        int orientation = dialogFragment.getResources().getConfiguration().orientation;
        this.minHeight = getPercentOfWindowHeight(DEF_MIN_HEIGHT_PERCENTS);
        this.maxHeight = getPercentOfWindowHeight(DEF_MAX_HEIGHT_PERCENTS);
        return this;
    }

    public BottomSheetDialogConstraintsBuilder withMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    public BottomSheetDialogConstraintsBuilder withMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public BottomSheetDialogConstraintsBuilder withMinHeightInPercents(int minHeightPercent) {
        minHeightPercent = validatePercentNumber(minHeightPercent);
        minHeight = getPercentOfWindowHeight(minHeightPercent);
        return this;
    }

    public BottomSheetDialogConstraintsBuilder withMaxHeightInPercents(int maxHeightPercent) {
        maxHeightPercent = validatePercentNumber(maxHeightPercent);
        maxHeight = getPercentOfWindowHeight(maxHeightPercent);
        return this;
    }

    public BottomSheetDialogConstraints build() {
        return new BottomSheetDialogConstraints(minHeight, maxHeight);
    }
}
