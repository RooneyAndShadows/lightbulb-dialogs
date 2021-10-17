package com.github.RooneyAndShadows.lightbulb.dialogs.base;

import androidx.fragment.app.FragmentManager;

public abstract class BaseDialogBuilder<DialogType extends BaseDialogFragment> {
    protected final String dialogTag;
    protected final FragmentManager fragmentManager;
    protected String title;
    protected String message;
    protected BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration;
    protected BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration;
    protected BaseDialogFragment.DialogButtonClickListener onPositiveClickListener;
    protected BaseDialogFragment.DialogButtonClickListener onNegativeClickListener;
    protected BaseDialogFragment.DialogShowListener onShowListener;
    protected BaseDialogFragment.DialogHideListener onHideListener;
    protected BaseDialogFragment.DialogCancelListener onCancelListener;
    protected BaseDialogFragment.DialogAnimationTypes animation;
    protected BaseDialogFragment.DialogTypes dialogType;
    protected boolean cancelableOnClickOutside = true;

    public BaseDialogBuilder(FragmentManager manager, String dialogTag) {
        this.dialogTag = dialogTag;
        this.fragmentManager = manager;
    }

    public BaseDialogBuilder<DialogType> withTitle(String title) {
        this.title = title;
        return this;
    }

    public BaseDialogBuilder<DialogType> withMessage(String message) {
        this.message = message;
        return this;
    }

    public BaseDialogBuilder<DialogType> withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        this.positiveButtonConfiguration = positiveButtonConfiguration;
        this.onPositiveClickListener = onClickListener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        this.negativeButtonConfiguration = negativeButtonConfiguration;
        this.onNegativeClickListener = onClickListener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        this.onCancelListener = listener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        this.onShowListener = listener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        this.onHideListener = listener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        this.cancelableOnClickOutside = closeOnClickOutside;
        return this;
    }

    public BaseDialogBuilder<DialogType> withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        this.dialogType = dialogType;
        return this;
    }

    public BaseDialogBuilder<DialogType> withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        this.animation = animation;
        return this;
    }

    public abstract DialogType buildDialog();
}
