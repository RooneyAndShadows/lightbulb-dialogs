package com.github.rooneyandshadowss.lightbulb.dialogs.base.internal


enum class DialogAnimationTypes(val value: Int) {
    FADE(1),
    NO_ANIMATION(2),
    TRANSITION_FROM_LEFT_TO_RIGHT(3),
    TRANSITION_FROM_TOP_TO_BOTTOM(4),
    TRANSITION_FROM_BOTTOM_TO_BOTTOM(5);

    companion object {
        fun valueOf(value: Int) = values().first { it.value == value }
    }
}