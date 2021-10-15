package com.rands.lightbulb.dialogs.dialog_loading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.rands.lightbulb.dialogs.R;
import com.rands.lightbulb.dialogs.base.BaseDialogFragment;

public class LoadingDialog extends BaseDialogFragment {

    public static LoadingDialog newInstance(String title, String message, DialogTypes dialogType, DialogAnimationTypes animationType) {
        LoadingDialog dialogFragment = new LoadingDialog();
        dialogFragment.setArguments(new DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withCancelable(false)
                .withShowing(false)
                .withDialogType(dialogType == null ? DialogTypes.NORMAL : dialogType)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType)
                .getBundle()
        );
        return dialogFragment;
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        return View.inflate(getContext(), R.layout.dialog_loading_normal, null);
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
    }
}