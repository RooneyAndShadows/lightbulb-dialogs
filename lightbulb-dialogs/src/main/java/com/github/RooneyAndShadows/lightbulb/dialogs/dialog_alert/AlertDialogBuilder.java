package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder;

import androidx.fragment.app.FragmentManager;

public class AlertDialogBuilder extends BaseDialogBuilder<AlertDialog> {

    public AlertDialogBuilder(FragmentManager manager, String dialogTag) {
        super(manager, dialogTag);
    }

    @Override
    public AlertDialogBuilder withTitle(String title) {
        return (AlertDialogBuilder) super.withTitle(title);
    }

    @Override
    public AlertDialogBuilder withMessage(String message) {
        return (AlertDialogBuilder) super.withMessage(message);
    }


    @Override
    public AlertDialogBuilder withPositiveButton(BaseDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (AlertDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public AlertDialogBuilder withNegativeButton(BaseDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (AlertDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public AlertDialogBuilder withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (AlertDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public AlertDialogBuilder withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (AlertDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public AlertDialogBuilder withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (AlertDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public AlertDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (AlertDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public AlertDialogBuilder withDialogType(BaseDialogFragment.DialogTypes dialogType) {
        return (AlertDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public AlertDialogBuilder withAnimations(BaseDialogFragment.DialogAnimationTypes animation) {
        return (AlertDialogBuilder) super.withAnimations(animation);
    }

    @Override
    public AlertDialog buildDialog() {
        AlertDialog alertDialog = (AlertDialog) fragmentManager.findFragmentByTag(dialogTag);
        if (alertDialog == null)
            alertDialog = AlertDialog.newInstance(title, message, positiveButtonConfiguration, negativeButtonConfiguration, cancelableOnClickOutside, dialogType, animation);
        alertDialog.setFragmentManager(fragmentManager);
        alertDialog.setDialogTag(dialogTag);
        alertDialog.addOnShowListener(onShowListener);
        alertDialog.addOnHideListener(onHideListener);
        alertDialog.addOnCancelListener(onCancelListener);
        alertDialog.addOnNegativeClickListeners(onNegativeClickListener);
        alertDialog.addOnPositiveClickListener(onPositiveClickListener);
        return alertDialog;
    }
}
