package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.rooneyandshadows.lightbulb.dialogs.R;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AlertDialog extends BaseDialogFragment {
    public static AlertDialog newInstance(
            String title, String message, DialogButtonConfiguration positive, DialogButtonConfiguration negative,
            boolean cancelable, DialogTypes dialogType, DialogAnimationTypes animationType) {
        AlertDialog dialogFragment = new AlertDialog();
        dialogFragment.setArguments(new DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(dialogType == null ? DialogTypes.NORMAL : dialogType)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType)
                .getBundle());
        return dialogFragment;
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        return View.inflate(getContext(), R.layout.dialog_alert, null);
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
    }

    @Override
    protected void setupFullScreenDialog(Window dialogWindow, View dialogLayout) {
        super.setupFullScreenDialog(dialogWindow, dialogLayout);
        if (titleAndMessageContainer != null)
            titleAndMessageContainer.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
