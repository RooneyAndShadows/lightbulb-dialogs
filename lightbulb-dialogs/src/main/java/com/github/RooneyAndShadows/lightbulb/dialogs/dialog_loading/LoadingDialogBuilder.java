package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;

import androidx.fragment.app.FragmentManager;

public class LoadingDialogBuilder extends BaseDialogBuilder<LoadingDialog> {

    public LoadingDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    @Override
    public LoadingDialogBuilder withTitle(String title) {
        return (LoadingDialogBuilder) super.withTitle(title);
    }

    @Override
    public LoadingDialogBuilder withMessage(String message) {
        return (LoadingDialogBuilder) super.withMessage(message);
    }

    @Override
    public LoadingDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (LoadingDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public LoadingDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (LoadingDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public LoadingDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (LoadingDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public LoadingDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (LoadingDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public LoadingDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (LoadingDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public LoadingDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (LoadingDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public LoadingDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (LoadingDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public LoadingDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (LoadingDialogBuilder) super.withAnimations(animation);
    }

    @Override
    public LoadingDialog buildDialog() {
        LoadingDialog dialogFragment = (LoadingDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = LoadingDialog.newInstance(title, message, dialogType, animation);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        return dialogFragment;
    }
}
