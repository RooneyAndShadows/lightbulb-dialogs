package com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.base.BaseDialogConstraintBuilder

class BottomSheetDialogConstraintsBuilder(dialogFragment: BaseDialogFragment) : BaseDialogConstraintBuilder(dialogFragment) {
    private val defaultMaxHeightPercents = 65
    private val defaultMinHeightPercents = 10
    private var minHeight = 0
    private var maxHeight = 0

    fun default(): BottomSheetDialogConstraintsBuilder {
        val orientation = dialogFragment.resources.configuration.orientation
        minHeight = getPercentOfWindowHeight(defaultMinHeightPercents)
        maxHeight = getPercentOfWindowHeight(defaultMaxHeightPercents)
        return this
    }

    fun withMinHeight(minHeight: Int): BottomSheetDialogConstraintsBuilder {
        this.minHeight = minHeight
        return this
    }

    fun withMaxHeight(maxHeight: Int): BottomSheetDialogConstraintsBuilder {
        this.maxHeight = maxHeight
        return this
    }

    fun withMinHeightInPercents(minHeightPercent: Int): BottomSheetDialogConstraintsBuilder {
        minHeight = getPercentOfWindowHeight(validatePercentNumber(minHeightPercent))
        return this
    }

    fun withMaxHeightInPercents(maxHeightPercent: Int): BottomSheetDialogConstraintsBuilder {
        maxHeight = getPercentOfWindowHeight(validatePercentNumber(maxHeightPercent))
        return this
    }

    fun build(): BottomSheetDialogConstraints {
        return BottomSheetDialogConstraints(minHeight, maxHeight)
    }
}