package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

enum class DialogTypes(val value: Int) {
    NORMAL(1),
    FULLSCREEN(2),
    BOTTOM_SHEET(3);

    companion object {
        fun valueOf(value: Int) = values().first { it.value == value }
    }
}