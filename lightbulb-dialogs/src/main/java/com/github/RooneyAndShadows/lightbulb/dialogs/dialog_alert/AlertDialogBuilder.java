package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;

import androidx.fragment.app.FragmentManager;

public class AlertDialogBuilder extends LightBulbDialogBuilder<AlertDialog> {

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
    public AlertDialogBuilder withPositiveButton(LightBulbDialogFragment.DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (AlertDialogBuilder) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public AlertDialogBuilder withNegativeButton(LightBulbDialogFragment.DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (AlertDialogBuilder) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public AlertDialogBuilder withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (AlertDialogBuilder) super.withOnCancelListener(listener);
    }

    @Override
    public AlertDialogBuilder withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (AlertDialogBuilder) super.withOnShowListener(listener);
    }

    @Override
    public AlertDialogBuilder withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (AlertDialogBuilder) super.withOnHideListener(listener);
    }

    @Override
    public AlertDialogBuilder withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (AlertDialogBuilder) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public AlertDialogBuilder withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (AlertDialogBuilder) super.withDialogType(dialogType);
    }

    @Override
    public AlertDialogBuilder withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
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
