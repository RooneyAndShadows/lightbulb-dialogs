package com.github.RooneyAndShadows.lightbulb.dialogs.base.constraints.regular;

public class RegularDialogConstraints {
    private final int minWidth;
    private final int maxWidth;
    private final int minHeight;
    private final int maxHeight;

    public RegularDialogConstraints(int minWidth, int maxWidth, int minHeight, int maxHeight) {
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public int resolveWidth(int desiredWidth) {
        if (desiredWidth > getMaxWidth())
            desiredWidth = getMaxWidth();
        if (desiredWidth < getMinWidth())
            desiredWidth = getMinWidth();
        return (desiredWidth);
    }

    public int resolveHeight(int desiredHeight) {
        if (desiredHeight > getMaxHeight())
            desiredHeight = getMaxHeight();
        if (desiredHeight < getMinHeight())
            desiredHeight = getMinHeight();
        return (desiredHeight);
    }

    public int getMinWidth() {
        return Math.min(minWidth, maxWidth);
    }

    public int getMaxWidth() {
        return Math.max(maxWidth, minWidth);
    }

    public int getMinHeight() {
        return Math.min(minHeight, maxHeight);
    }

    public int getMaxHeight() {
        return Math.max(maxHeight, minHeight);
    }
}