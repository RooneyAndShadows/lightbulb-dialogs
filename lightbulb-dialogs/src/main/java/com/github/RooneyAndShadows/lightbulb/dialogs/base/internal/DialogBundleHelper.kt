package com.github.rooneyandshadows.lightbulb.dialogs.base.internal

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils

class DialogBundleHelper(val bundle: Bundle = Bundle()) {

    companion object {
        private const val DIALOG_TITLE_TEXT_TAG = "DIALOG_TITLE_TEXT_TAG"
        private const val DIALOG_MESSAGE_TEXT_TAG = "DIALOG_MESSAGE_TEXT_TAG"
        private const val DIALOG_POSITIVE_BUTTON_CONFIG_TAG = "DIALOG_POSITIVE_BUTTON_CONFIG_TAG"
        private const val DIALOG_NEGATIVE_BUTTON_CONFIG_TAG = "DIALOG_NEGATIVE_BUTTON_CONFIG_TAG"
        private const val DIALOG_CANCELABLE_TAG = "DIALOG_CANCELABLE_TAG"
        private const val DIALOG_SHOWING_TAG = "DIALOG_SHOWING_TAG"
        private const val DIALOG_TYPE_TAG = "DIALOG_TYPE_TAG"
        private const val DIALOG_FULLSCREEN_TAG = "DIALOG_FULLSCREEN_TAG"
        private const val DIALOG_ANIMATION_TAG = "DIALOG_ANIMATION_TAG"
    }

    fun getTitle(): String? {
        return bundle.getString(DIALOG_TITLE_TEXT_TAG)
    }

    fun getMessage(): String? {
        return bundle.getString(DIALOG_MESSAGE_TEXT_TAG)
    }

    fun getPositiveButtonConfig(): DialogButtonConfiguration? {
        return BundleUtils.getParcelable(
            DIALOG_POSITIVE_BUTTON_CONFIG_TAG,
            bundle,
            DialogButtonConfiguration::class.java
        )
    }

    fun getNegativeButtonConfig(): DialogButtonConfiguration? {
        return BundleUtils.getParcelable(
            DIALOG_NEGATIVE_BUTTON_CONFIG_TAG,
            bundle,
            DialogButtonConfiguration::class.java
        )
    }

    val cancelable: Boolean
        get() = bundle.getBoolean(DIALOG_CANCELABLE_TAG)
    val showing: Boolean
        get() = bundle.getBoolean(DIALOG_CANCELABLE_TAG)

    fun getDialogType(): DialogTypes {
        return DialogTypes.valueOf(bundle.getInt(DIALOG_TYPE_TAG))
    }

    fun getFullscreen(): Boolean {
        return bundle.getBoolean(DIALOG_FULLSCREEN_TAG)
    }

    fun getAnimationType(): DialogAnimationTypes {
        return DialogAnimationTypes.valueOf(bundle.getInt(DIALOG_ANIMATION_TAG))
    }

    fun withTitle(dialogTitle: String?): DialogBundleHelper {
        bundle.putString(DIALOG_TITLE_TEXT_TAG, dialogTitle)
        return this
    }

    fun withMessage(dialogMessage: String?): DialogBundleHelper {
        bundle.putString(DIALOG_MESSAGE_TEXT_TAG, dialogMessage)
        return this
    }

    fun withPositiveButtonConfig(positiveButtonConfig: DialogButtonConfiguration?): DialogBundleHelper {
        bundle.putParcelable(DIALOG_POSITIVE_BUTTON_CONFIG_TAG, positiveButtonConfig)
        return this
    }

    fun withNegativeButtonConfig(negativeButtonConfig: DialogButtonConfiguration?): DialogBundleHelper {
        bundle.putParcelable(DIALOG_NEGATIVE_BUTTON_CONFIG_TAG, negativeButtonConfig)
        return this
    }

    fun withShowing(showing: Boolean): DialogBundleHelper {
        bundle.putBoolean(DIALOG_SHOWING_TAG, showing)
        return this
    }

    fun withCancelable(cancelable: Boolean): DialogBundleHelper {
        bundle.putBoolean(DIALOG_CANCELABLE_TAG, cancelable)
        return this
    }

    fun withDialogType(dialogType: DialogTypes): DialogBundleHelper {
        bundle.putInt(DIALOG_TYPE_TAG, dialogType.value)
        return this
    }

    fun withFullScreen(fullScreen: Boolean): DialogBundleHelper {
        bundle.putBoolean(DIALOG_FULLSCREEN_TAG, fullScreen)
        return this
    }

    fun withAnimation(animationType: DialogAnimationTypes): DialogBundleHelper {
        bundle.putInt(DIALOG_ANIMATION_TAG, animationType.value)
        return this
    }
}