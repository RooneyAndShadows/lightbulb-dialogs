package com.github.RooneyAndShadows.lightbulb.dialogs.base.constraints.bottomsheet;

public class BottomSheetDialogConstraints {
    private final int minHeight;
    private final int maxHeight;

    public BottomSheetDialogConstraints(int minHeight, int maxHeight) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public int resolveHeight(int desiredHeight) {
        if (desiredHeight > getMaxHeight())
            desiredHeight = getMaxHeight();
        if (desiredHeight < getMinHeight())
            desiredHeight = getMinHeight();
        return (desiredHeight);
    }

    public int getMinHeight() {
        return Math.min(minHeight, maxHeight);
    }

    public int getMaxHeight() {
        return Math.max(maxHeight, minHeight);
    }
}