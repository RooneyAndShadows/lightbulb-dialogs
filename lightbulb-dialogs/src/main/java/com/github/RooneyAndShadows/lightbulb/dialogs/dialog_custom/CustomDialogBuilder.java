package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom;

import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.LightBulbDialogFragment;

import androidx.fragment.app.FragmentManager;

import static com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.*;


public class CustomDialogBuilder<DialogType extends CustomDialog> extends LightBulbDialogBuilder<DialogType> {
    private final CustomDialogInitializer<DialogType> dialogInitializer;
    private final CustomDialogInflater dialogInflater;
    private boolean loading = false;
    private CustomDialogCallbacks dialogCallbacks;


    public CustomDialogBuilder(FragmentManager manager, String dialogTag, CustomDialogInitializer<DialogType> customDialogInitializer, CustomDialogInflater inflatedListener) {
        super(manager, dialogTag);
        this.dialogInflater = inflatedListener;
        this.dialogInitializer = customDialogInitializer;
    }

    @Override
    public CustomDialogBuilder<DialogType> withTitle(String title) {
        return (CustomDialogBuilder<DialogType>) super.withTitle(title);
    }

    @Override
    public CustomDialogBuilder<DialogType> withMessage(String message) {
        return (CustomDialogBuilder<DialogType>) super.withMessage(message);
    }

    @Override
    public CustomDialogBuilder<DialogType> withPositiveButton(DialogButtonConfiguration positiveButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (CustomDialogBuilder<DialogType>) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withNegativeButton(DialogButtonConfiguration negativeButtonConfiguration, LightBulbDialogFragment.DialogButtonClickListener onClickListener) {
        return (CustomDialogBuilder<DialogType>) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withOnCancelListener(LightBulbDialogFragment.DialogCancelListener listener) {
        return (CustomDialogBuilder<DialogType>) super.withOnCancelListener(listener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withOnShowListener(LightBulbDialogFragment.DialogShowListener listener) {
        return (CustomDialogBuilder<DialogType>) super.withOnShowListener(listener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withOnHideListener(LightBulbDialogFragment.DialogHideListener listener) {
        return (CustomDialogBuilder<DialogType>) super.withOnHideListener(listener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (CustomDialogBuilder<DialogType>) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public CustomDialogBuilder<DialogType> withDialogType(LightBulbDialogFragment.DialogTypes dialogType) {
        return (CustomDialogBuilder<DialogType>) super.withDialogType(dialogType);
    }

    @Override
    public CustomDialogBuilder<DialogType> withAnimations(LightBulbDialogFragment.DialogAnimationTypes animation) {
        return (CustomDialogBuilder<DialogType>) super.withAnimations(animation);
    }

    public CustomDialogBuilder<DialogType> withLoading(boolean isLoading) {
        this.loading = isLoading;
        return this;
    }

    public CustomDialogBuilder<DialogType> withDialogCallbacks(CustomDialogCallbacks dialogCallbacks) {
        this.dialogCallbacks = dialogCallbacks;
        return this;
    }

    public interface CustomDialogInitializer<DialogType extends CustomDialog> {
        DialogType initialize(
                String title, String message,
                DialogButtonConfiguration positiveButtonConfiguration, DialogButtonConfiguration negativeButtonConfiguration,
                boolean cancelable, boolean loading, LightBulbDialogFragment.DialogTypes dialogType, LightBulbDialogFragment.DialogAnimationTypes animationType
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public DialogType buildDialog() {
        DialogType dialogFragment = (DialogType) fragmentManager.findFragmentByTag(dialogTag);
        if (dialogFragment == null)
            dialogFragment = dialogInitializer.initialize(
                    title, message, positiveButtonConfiguration, negativeButtonConfiguration,
                    cancelableOnClickOutside, loading, dialogType, animation
            );
        dialogFragment.setDialogInflater(dialogInflater);
        dialogFragment.setDialogCallbacks(dialogCallbacks);
        dialogFragment.setFragmentManager(fragmentManager);
        dialogFragment.setDialogTag(dialogTag);
        dialogFragment.addOnShowListener(onShowListener);
        dialogFragment.addOnPositiveClickListener(onPositiveClickListener);
        dialogFragment.addOnNegativeClickListeners(onNegativeClickListener);
        dialogFragment.addOnHideListener(onHideListener);
        dialogFragment.addOnCancelListener(onCancelListener);
        return dialogFragment;
    }
}
