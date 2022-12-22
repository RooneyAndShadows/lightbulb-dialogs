package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils

class DialogBundleHelper(val bundle: Bundle = Bundle()) {
    private val dialogTitleTextTag = "DIALOG_TITLE_TEXT_TAG"
    private val dialogMessageTextTag = "DIALOG_MESSAGE_TEXT_TAG"
    private val dialogCancelableTag = "DIALOG_CANCELABLE_TAG"
    private val dialogShowingTag = "DIALOG_SHOWING_TAG"
    private val dialogTypeTag = "DIALOG_TYPE_TAG"
    private val dialogAnimationTag = "DIALOG_ANIMATION_TAG"
    private val dialogFullscreenTag = "DIALOG_FULLSCREEN_TAG"
    private val dialogPositiveButtonConfigTag = "DIALOG_POSITIVE_BUTTON_CONFIG_TAG"
    private val dialogNegativeButtonConfigTag = "DIALOG_NEGATIVE_BUTTON_CONFIG_TAG"
    val title: String?
        get() = bundle.getString(dialogTitleTextTag)
    val message: String?
        get() = bundle.getString(dialogMessageTextTag)
    val cancelable: Boolean
        get() = bundle.getBoolean(dialogCancelableTag)
    val showing: Boolean
        get() = bundle.getBoolean(dialogCancelableTag)
    val dialogType: DialogTypes
        get() = DialogTypes.valueOf(bundle.getInt(dialogTypeTag))
    val animationType: DialogAnimationTypes
        get() = DialogAnimationTypes.valueOf(bundle.getInt(dialogAnimationTag))
    val fullScreen: Boolean
        get() = bundle.getBoolean(dialogFullscreenTag)
    val positiveButtonConfig: DialogButtonConfiguration?
        get() = BundleUtils.getParcelable(
            dialogPositiveButtonConfigTag,
            bundle,
            DialogButtonConfiguration::class.java
        )
    val negativeButtonConfig: DialogButtonConfiguration?
        get() = BundleUtils.getParcelable(
            dialogNegativeButtonConfigTag,
            bundle,
            DialogButtonConfiguration::class.java
        )

    init {
        bundle.putInt(dialogTypeTag, DialogTypes.NORMAL.value)
        bundle.putInt(dialogAnimationTag, DialogAnimationTypes.NO_ANIMATION.value)
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

    fun withFullScreen(fullScreen: Boolean): DialogBundleHelper {
        bundle.putBoolean(dialogFullscreenTag, fullScreen)
        return this
    }

    fun withPositiveButtonConfig(positiveButtonConfig: DialogButtonConfiguration?): DialogBundleHelper {
        bundle.putParcelable(dialogPositiveButtonConfigTag, positiveButtonConfig)
        return this
    }

    fun withNegativeButtonConfig(negativeButtonConfig: DialogButtonConfiguration?): DialogBundleHelper {
        bundle.putParcelable(dialogNegativeButtonConfigTag, negativeButtonConfig)
        return this
    }
}