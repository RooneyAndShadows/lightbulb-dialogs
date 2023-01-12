package com.github.rooneyandshadowss.lightbulb.dialogs.base.constraints.regular

import android.content.res.Configuration
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.base.BaseDialogConstraintBuilder

class RegularDialogConstraintsBuilder(dialogFragment: BaseDialogFragment) : BaseDialogConstraintBuilder(dialogFragment) {
    private val defaultMaxWidthPercentsPortrait = 85
    private val defaultMaxWidthPercentsLandscape = 65
    private val defaultMaxHeightPercents = 85
    private var minWidth = 0
    private var maxWidth = 0
    private var minHeight = 0
    private var maxHeight = 0

    fun default(): RegularDialogConstraintsBuilder {
        val orientation = dialogFragment.resources.configuration.orientation
        val maxWidth = getPercentOfWindowWidth(
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                defaultMaxWidthPercentsPortrait
            else
                defaultMaxWidthPercentsLandscape
        )
        val maxHeight = getPercentOfWindowHeight(defaultMaxHeightPercents)
        minWidth = 0
        this.maxWidth = maxWidth
        minHeight = 0
        this.maxHeight = maxHeight
        return this
    }

    fun withMinWidth(minWidth: Int): RegularDialogConstraintsBuilder {
        this.minWidth = minWidth
        return this
    }

    fun withMaxWidth(maxWidth: Int): RegularDialogConstraintsBuilder {
        this.maxWidth = maxWidth
        return this
    }

    fun withMinHeight(minHeight: Int): RegularDialogConstraintsBuilder {
        this.minHeight = minHeight
        return this
    }

    fun withMaxHeight(maxHeight: Int): RegularDialogConstraintsBuilder {
        this.maxHeight = maxHeight
        return this
    }

    fun withMinWidthInPercents(minWidthPercent: Int): RegularDialogConstraintsBuilder {
        minWidth = getPercentOfWindowWidth(validatePercentNumber(minWidthPercent))
        return this
    }

    fun withMaxWidthInPercents(maxWidthPercent: Int): RegularDialogConstraintsBuilder {
        maxWidth = getPercentOfWindowWidth(validatePercentNumber(maxWidthPercent))
        return this
    }

    fun withMinHeightInPercents(minHeightPercent: Int): RegularDialogConstraintsBuilder {
        minHeight = getPercentOfWindowHeight(validatePercentNumber(minHeightPercent))
        return this
    }

    fun withMaxHeightInPercents(maxHeightPercent: Int): RegularDialogConstraintsBuilder {
        maxHeight = getPercentOfWindowHeight(validatePercentNumber(maxHeightPercent))
        return this
    }

    fun build(): RegularDialogConstraints {
        return RegularDialogConstraints(minWidth, maxWidth, minHeight, maxHeight)
    }
}