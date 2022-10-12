package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogBuilder;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

import static com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog.*;


public class CustomDialogBuilder<DialogType extends CustomDialog> extends BaseDialogBuilder<DialogType> {
    private final CustomDialogInitializer<DialogType> dialogInitializer;
    private final CustomDialogInflater dialogInflater;
    private boolean loading = false;

    public CustomDialogBuilder(LifecycleOwner lifecycleOwner, FragmentManager manager, String dialogTag, CustomDialogInitializer<DialogType> dialogInitializer, CustomDialogInflater dialogInflater) {
        super(lifecycleOwner, manager, dialogTag);
        this.dialogInitializer = dialogInitializer;
        this.dialogInflater = dialogInflater;
    }

    public CustomDialogBuilder(FragmentManager manager, String dialogTag, CustomDialogInitializer<DialogType> dialogInitializer, CustomDialogInflater dialogInflater) {
        super(manager, dialogTag);
        this.dialogInitializer = dialogInitializer;
        this.dialogInflater = dialogInflater;
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
    public CustomDialogBuilder<DialogType> withPositiveButton(DialogButtonConfiguration positiveButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (CustomDialogBuilder<DialogType>) super.withPositiveButton(positiveButtonConfiguration, onClickListener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withNegativeButton(DialogButtonConfiguration negativeButtonConfiguration, BaseDialogFragment.DialogButtonClickListener onClickListener) {
        return (CustomDialogBuilder<DialogType>) super.withNegativeButton(negativeButtonConfiguration, onClickListener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withOnCancelListener(BaseDialogFragment.DialogCancelListener listener) {
        return (CustomDialogBuilder<DialogType>) super.withOnCancelListener(listener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withOnShowListener(BaseDialogFragment.DialogShowListener listener) {
        return (CustomDialogBuilder<DialogType>) super.withOnShowListener(listener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withOnHideListener(BaseDialogFragment.DialogHideListener listener) {
        return (CustomDialogBuilder<DialogType>) super.withOnHideListener(listener);
    }

    @Override
    public CustomDialogBuilder<DialogType> withCancelOnClickOutsude(boolean closeOnClickOutside) {
        return (CustomDialogBuilder<DialogType>) super.withCancelOnClickOutsude(closeOnClickOutside);
    }

    @Override
    public CustomDialogBuilder<DialogType> withDialogType(DialogTypes dialogType) {
        return (CustomDialogBuilder<DialogType>) super.withDialogType(dialogType);
    }

    @Override
    public CustomDialogBuilder<DialogType> withAnimations(DialogAnimationTypes animation) {
        return (CustomDialogBuilder<DialogType>) super.withAnimations(animation);
    }

    @Override
    public CustomDialogBuilder<DialogType> withDialogCallbacks(DialogCallbacks callbacks) {
        return (CustomDialogBuilder<DialogType>) super.withDialogCallbacks(callbacks);
    }

    public CustomDialogBuilder<DialogType> withLoading(boolean isLoading) {
        this.loading = isLoading;
        return this;
    }

    public interface CustomDialogInitializer<DialogType extends CustomDialog> {
        DialogType initialize(
                String title,
                String message,
                DialogButtonConfiguration positiveButtonConfiguration,
                DialogButtonConfiguration negativeButtonConfiguration,
                boolean cancelable,
                boolean loading,
                DialogTypes dialogType,
                DialogAnimationTypes animationType
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
        dialogFragment.setLifecycleOwner(dialogLifecycleOwner);
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
