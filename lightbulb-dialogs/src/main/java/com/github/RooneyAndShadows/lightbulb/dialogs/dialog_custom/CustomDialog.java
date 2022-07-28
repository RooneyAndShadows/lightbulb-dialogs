package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils;
import com.github.rooneyandshadows.lightbulb.dialogs.R;
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomDialog extends BaseDialogFragment {
    private static final String IS_LOADING_KEY = "IS_LOADING_KEY";
    private ProgressBar loadingIndicator;
    private AppCompatTextView titleView;
    private CustomDialogInflater dialogInflater;
    private boolean loading = false;

    public static CustomDialog newCustomDialogInstance(
            String title, String message, DialogButtonConfiguration positive, DialogButtonConfiguration negative,
            boolean cancelable, boolean loading, DialogTypes dialogType, DialogAnimationTypes animationType) {
        CustomDialog f = new CustomDialog();
        DialogBundleHelper helper = new DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(dialogType == null ? DialogTypes.NORMAL : dialogType)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType);
        helper.getBundle().putBoolean(IS_LOADING_KEY, loading);
        f.setArguments(helper.getBundle());
        return f;
    }

    @Override
    protected final View setDialogLayout(LayoutInflater inflater) {
        View view = View.inflate(getContext(), R.layout.dialog_custom, null);
        LinearLayoutCompat contentContainer = view.findViewById(R.id.customDialogContentContainer);
        contentContainer.removeAllViews();
        if (getDialogType() == DialogTypes.FULLSCREEN)
            contentContainer.getLayoutParams().height = 0;
        if (dialogInflater != null)
            contentContainer.addView(dialogInflater.inflateView(this, inflater));
        return view;
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        loading = savedInstanceState == null ?
                dialogArguments.getBoolean(IS_LOADING_KEY) :
                savedInstanceState.getBoolean(IS_LOADING_KEY);
    }

    @Override
    protected void configureContent(View view, Bundle savedInstanceState) {
        selectViews();
        setupLoadingView();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        outState.putBoolean(IS_LOADING_KEY, loading);
    }

    public final void setLoading(boolean isLoading) {
        this.loading = isLoading;
        setupLoadingView();
    }

    public final void setDialogInflater(CustomDialogInflater dialogInflater) {
        this.dialogInflater = dialogInflater;
    }

    public interface CustomDialogInflater {
        View inflateView(CustomDialog dialog, LayoutInflater layoutInflater);
    }

    private void selectViews() {
        if (getView() == null)
            return;
        loadingIndicator = getView().findViewById(R.id.loadingIndicator);
        titleView = getView().findViewById(R.id.title);
    }

    private void setupLoadingView() {
        if (loadingIndicator == null)
            return;
        loadingIndicator.setVisibility(loading ? View.VISIBLE : View.GONE);
        int endPadding = loading ? ResourceUtils.getDimenPxById(loadingIndicator.getContext(), R.dimen.dialog_spacing_size_small) : 0;
        titleView.setPadding(titleView.getPaddingLeft(), titleView.getPaddingTop(), endPadding, titleView.getPaddingBottom());
    }
}