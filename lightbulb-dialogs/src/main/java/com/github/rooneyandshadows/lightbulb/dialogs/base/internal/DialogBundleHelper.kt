package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils

class DialogBundleHelper(val bundle: Bundle = defaultBundle()) {
    val title: String?
        get() = bundle.getString(dialogTitleTextTag)
    val message: String?
        get() = bundle.getString(dialogMessageTextTag)
    val cancelable: Boolean
        get() = bundle.getBoolean(dialogCancelableTag)
    val showing: Boolean
        get() = bundle.getBoolean(dialogShowingTag)
    val type: DialogTypes
        get() = DialogTypes.valueOf(bundle.getInt(dialogTypeTag))
    val animationType: DialogAnimationTypes
        get() = DialogAnimationTypes.valueOf(bundle.getInt(dialogAnimationTag))
    val buttonConfigurations: List<DialogButton>?
        get() = BundleUtils.getParcelableArrayList(dialogButtonsConfigTag, bundle, DialogButton::class.java)

    companion object {
        private const val dialogTitleTextTag = "DIALOG_TITLE_TEXT_TAG"
        private const val dialogMessageTextTag = "DIALOG_MESSAGE_TEXT_TAG"
        private const val dialogCancelableTag = "DIALOG_CANCELABLE_TAG"
        private const val dialogShowingTag = "DIALOG_SHOWING_TAG"
        private const val dialogTypeTag = "DIALOG_TYPE_TAG"
        private const val dialogAnimationTag = "DIALOG_ANIMATION_TAG"
        private const val dialogButtonsConfigTag = "DIALOG_POSITIVE_BUTTON_CONFIG_TAG"

        private fun defaultBundle(): Bundle {
            return Bundle().apply {
                putInt(dialogTypeTag, DialogTypes.NORMAL.value)
                putInt(dialogAnimationTag, DialogAnimationTypes.NO_ANIMATION.value)
            }
        }
    }

    fun withTitle(dialogTitle: String?): DialogBundleHelper {
        bundle.putString(dialogTitleTextTag, dialogTitle)
        return this
    }

    fun withMessage(dialogMessage: String?): DialogBundleHelper {
        bundle.putString(dialogMessageTextTag, dialogMessage)
        return this
    }

    fun withShowing(showing: Boolean): DialogBundleHelper {
        bundle.putBoolean(dialogShowingTag, showing)
        return this
    }

    fun withCancelable(cancelable: Boolean): DialogBundleHelper {
        bundle.putBoolean(dialogCancelableTag, cancelable)
        return this
    }

    fun withDialogType(dialogType: DialogTypes): DialogBundleHelper {
        bundle.putInt(dialogTypeTag, dialogType.value)
        return this
    }

    fun withAnimation(animationType: DialogAnimationTypes): DialogBundleHelper {
        bundle.putInt(dialogAnimationTag, animationType.value)
        return this
    }

    fun withButtonConfigurations(buttonConfigurations: List<DialogButton>): DialogBundleHelper {
        bundle.putParcelableArrayList(dialogButtonsConfigTag, ArrayList(buttonConfigurations))
        return this
    }
}