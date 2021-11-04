package com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;

import androidx.fragment.app.FragmentManager;

public class LoadingDialogBuilder extends LightBulbDialogBuilder<LoadingDialog> {

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
    public LoadingDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (LoadingDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public LoadingDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (LoadingDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public LoadingDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (LoadingDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public LoadingDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (LoadingDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public LoadingDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (LoadingDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public LoadingDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (LoadingDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public LoadingDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (LoadingDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public LoadingDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
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
