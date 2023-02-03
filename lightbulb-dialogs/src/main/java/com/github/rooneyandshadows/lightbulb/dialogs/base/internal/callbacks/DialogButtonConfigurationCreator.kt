package com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks

import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration


interface DialogButtonConfigurationCreator {
    fun create(currentConfiguration: DialogButtonConfiguration?): DialogButtonConfiguration?
}