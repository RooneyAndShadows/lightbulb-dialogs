package com.github.rooneyandshadows.lightbulb.dialogs.base;

import androidx.fragment.app.FragmentManager;

public abstract class LightBulbDialogBuilder<DialogType extends LightBulbDialogFragment> {
    protected final String dialogTag;
    protected final FragmentManager fragmentManager;
    protected String title;
    protected String message;
    protected LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration;
    protected LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration;
    protected LightBulbDialogFragment.DialogButtonClickListener onPositiveClickListener;
    protected LightBulbDialogFragment.DialogButtonClickListener onNegativeClickListener;
    protected LightBulbDialogFragment.DialogShowListener onShowListener;
    protected LightBulbDialogFragment.DialogHideListener onHideListener;
    protected LightBulbDialogFragment.DialogCancelListener onCancelListener;
    protected LightBulbDialogFragment.DialogAnimationTypes animation;
    protected LightBulbDialogFragment.DialogTypes dialogType;
    protected boolean cancelableOnClickOutside = true;

    public LightBulbDialogBuilder(FragmentManager manager, String dialogTag) {
        this.dialogTag = dialogTag;
        this.fragmentManager = manager;
    }

    public LightBulbDialogBuilder<DialogType> withTitle(String title) {
        this.title = title;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withMessage(String message) {
        this.message = message;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        this.positiveButtonConfiguration = positiveButtonConfiguration;
        this.onPositiveClickListener = onClickListener;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        this.negativeButtonConfiguration = negativeButtonConfiguration;
        this.onNegativeClickListener = onClickListener;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        this.onCancelListener = listener;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        this.onShowListener = listener;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        this.onHideListener = listener;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        this.cancelableOnClickOutside = closeOnClickOutside;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        this.dialogType = dialogType;
        return this;
    }

    public LightBulbDialogBuilder<DialogType> withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        this.animation = animation;
        return this;
    }

    public abstract DialogType buildDialog();
}
