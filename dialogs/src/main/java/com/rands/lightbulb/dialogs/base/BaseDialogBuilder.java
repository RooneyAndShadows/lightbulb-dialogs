package com.rands.lightbulb.dialogs.base;

import androidx.fragment.app.FragmentManager;

import static com.rands.lightbulb.dialogs.base.BaseDialogFragment.*;

public abstract class BaseDialogBuilder<DialogType extends BaseDialogFragment> {
    protected final String dialogTag;
    protected final FragmentManager fragmentManager;
    protected String title;
    protected String message;
    protected DialogButtonConfiguration positiveButtonConfiguration;
    protected DialogButtonConfiguration negativeButtonConfiguration;
    protected DialogButtonClickListener onPositiveClickListener;
    protected DialogButtonClickListener onNegativeClickListener;
    protected DialogShowListener onShowListener;
    protected DialogHideListener onHideListener;
    protected DialogCancelListener onCancelListener;
    protected DialogAnimationTypes animation;
    protected DialogTypes dialogType;
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

    public BaseDialogBuilder<DialogType> withPositiveButton(DialogButtonConfiguration positiveButtonConfiguration, DialogButtonClickListener onClickListener) {
        this.positiveButtonConfiguration = positiveButtonConfiguration;
        this.onPositiveClickListener = onClickListener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withNegativeButton(DialogButtonConfiguration negativeButtonConfiguration, DialogButtonClickListener onClickListener) {
        this.negativeButtonConfiguration = negativeButtonConfiguration;
        this.onNegativeClickListener = onClickListener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withOnCancelListener(DialogCancelListener listener) {
        this.onCancelListener = listener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withOnShowListener(DialogShowListener listener) {
        this.onShowListener = listener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withOnHideListener(DialogHideListener listener) {
        this.onHideListener = listener;
        return this;
    }

    public BaseDialogBuilder<DialogType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        this.cancelableOnClickOutside = closeOnClickOutside;
        return this;
    }

    public BaseDialogBuilder<DialogType> withDialogType(DialogTypes dialogType) {
        this.dialogType = dialogType;
        return this;
    }

    public BaseDialogBuilder<DialogType> withAnimations(DialogAnimationTypes animation) {
        this.animation = animation;
        return this;
    }

    public abstract DialogType buildDialog();
}
