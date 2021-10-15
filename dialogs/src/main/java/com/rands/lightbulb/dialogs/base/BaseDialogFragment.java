package com.rands.lightbulb.dialogs.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rands.lightbulb.commons.utils.ParcelableUtils;
import com.rands.lightbulb.commons.utils.WindowUtils;
import com.rands.lightbulb.dialogs.R;
import com.rands.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints;
import com.rands.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraintsBuilder;
import com.rands.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints;
import com.rands.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder;
import com.rands.java.commons.string.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

@SuppressWarnings("unused")
public abstract class BaseDialogFragment extends androidx.fragment.app.DialogFragment {
    private View rootView;
    protected String title;
    protected String message;
    private String dialogTag;
    private boolean cancelableOnClickOutside = true;
    private DialogButtonConfiguration positiveButtonConfig;
    private DialogButtonConfiguration negativeButtonConfig;
    private boolean shown = false;
    protected boolean fullscreen = false;
    private DialogTypes dialogType;
    private DialogAnimationTypes animationType;
    private FragmentManager fragmentManager;
    protected LinearLayoutCompat titleAndMessageContainer;
    protected LinearLayoutCompat buttonsContainer;
    protected Button buttonPositive;
    protected Button buttonNegative;
    protected RegularDialogConstraints regularDialogConstraints;
    protected BottomSheetDialogConstraints bottomSheetConstraints;
    private final ArrayList<DialogButtonClickListener> onPositiveClickListeners = new ArrayList<>();
    private final ArrayList<DialogButtonClickListener> onNegativeClickListeners = new ArrayList<>();
    private final ArrayList<DialogShowListener> onShowListeners = new ArrayList<>();
    private final ArrayList<DialogHideListener> onHideListeners = new ArrayList<>();
    private final ArrayList<DialogCancelListener> onCancelListeners = new ArrayList<>();


    protected abstract View setDialogLayout(LayoutInflater inflater);

    protected abstract void configureContent(View view, Bundle savedInstanceState);

    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
    }

    protected void viewCreated(View view, Bundle savedInstanceState) {
    }

    protected void viewRestored(Bundle savedInstanceState) {
    }

    protected void saveInstanceState(Bundle outState) {
    }

    protected void onDismiss() {
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogBundleHelper helper = new DialogBundleHelper(savedInstanceState == null ? getArguments() : savedInstanceState);
        if (savedInstanceState == null) {
            if (helper.getBundle() == null) {
                throw new IllegalArgumentException("Bundle args required");
            }
            dialogType = helper.getDialogType();
            switch (dialogType) {
                case NORMAL:
                    fullscreen = false;
                    animationType = helper.getAnimationType();
                    break;
                case FULLSCREEN:
                    fullscreen = true;
                    animationType = helper.getAnimationType();
                    break;
                case BOTTOM_SHEET:
                    fullscreen = false;
                    animationType = DialogAnimationTypes.TRANSITION_FROM_BOTTOM_TO_BOTTOM;
                    break;
            }

            if (title == null)//if not set outside of builder | otherwise ignore
                title = helper.getTitle();
            if (message == null)//if not set outside of builder | otherwise ignore
                message = helper.getMessage();
            if (positiveButtonConfig == null)//if not set outside of builder | otherwise ignore
                positiveButtonConfig = helper.getPositiveButtonConfig();
            if (negativeButtonConfig == null)//if not set outside of builder | otherwise ignore
                negativeButtonConfig = helper.getNegativeButtonConfig();
            cancelableOnClickOutside = helper.getCancelable();
        } else {
            title = helper.getTitle();
            message = helper.getMessage();
            positiveButtonConfig = helper.getPositiveButtonConfig();
            negativeButtonConfig = helper.getNegativeButtonConfig();
            cancelableOnClickOutside = helper.getCancelable();
            shown = helper.getShowing();
            fullscreen = helper.getFullscreen();
            dialogType = helper.getDialogType();
            animationType = helper.getAnimationType();
        }
        setCancelable(cancelableOnClickOutside);
        create(getArguments(), savedInstanceState);
    }

    @Override
    public final void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        DialogBundleHelper helper = new DialogBundleHelper(outState)
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positiveButtonConfig)
                .withNegativeButtonConfig(negativeButtonConfig)
                .withCancelable(cancelableOnClickOutside)
                .withShowing(shown)
                .withFullScreen(fullscreen)
                .withDialogType(dialogType)
                .withAnimation(animationType);
        saveInstanceState(helper.getBundle());
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        regularDialogConstraints = getRegularConstraints();
        bottomSheetConstraints = getBottomSheetConstraints();
        if (regularDialogConstraints == null)
            regularDialogConstraints = new RegularDialogConstraintsBuilder(this)
                    .Default()
                    .build();
        if (bottomSheetConstraints == null)
            this.bottomSheetConstraints = new BottomSheetDialogConstraintsBuilder(this)
                    .Default()
                    .build();
        switch (dialogType) {
            case NORMAL:
            case FULLSCREEN:
                this.rootView = setDialogLayout(LayoutInflater.from(getContext()));
                break;
            case BOTTOM_SHEET:
                this.rootView = setBottomSheetDialogLayout();
                break;
        }
        return rootView;
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureHeading();
        configureButtons();
        configureContent(rootView, savedInstanceState);
        measureDialogLayout();
        viewCreated(view, savedInstanceState);
    }

    @Override
    public final void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        viewRestored(savedInstanceState);
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        shown = true;
        dialog.setOnShowListener(dialogInterface -> {
            if (savedInstanceState == null) { // executes animation and onShowEvent only if dialog is shown for the first time in it's lifecycle
                for (DialogShowListener onShowListener : onShowListeners)
                    onShowListener.onShow(this);
            }
            //dialog.setContentView(rootView);
        });
        return dialog;
    }

    @Override
    public final void onStart() {
        super.onStart();
        if (getDialog() == null || getDialog().getWindow() == null)
            return;
        Window dialogWindow = getDialog().getWindow();
        switch (animationType) {
            case NO_ANIMATION:
                dialogWindow.setWindowAnimations(R.style.NoAnimation);
                break;
            case FADE:
                dialogWindow.setWindowAnimations(R.style.Animation_Fade);
                break;
            case TRANSITION_FROM_BOTTOM_TO_BOTTOM:
                dialogWindow.setWindowAnimations(R.style.Animation_FromBottomToBottom);
                break;
            case TRANSITION_FROM_LEFT_TO_RIGHT:
                dialogWindow.setWindowAnimations(R.style.Animation_FromLeftToRight);
                break;
            case TRANSITION_FROM_TOP_TO_BOTTOM:
                dialogWindow.setWindowAnimations(R.style.Animation_FromTopToBottom);
                break;
        }
    }

    @Override
    public final void dismiss() {
        if (!shown)
            return;
        super.dismiss();
        shown = false;
    }

    @Override
    public final void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        for (DialogHideListener onHideListener : onHideListeners)
            onHideListener.onHide(this);
        onDismiss();
    }

    @Override
    public final void onCancel(@NonNull DialogInterface dialog) {
        for (DialogCancelListener onCancelListener : onCancelListeners)
            onCancelListener.onCancel(this);
        dismiss();
    }

    public boolean isDialogShown() {
        return shown;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setDialogTag(String dialogTag) {
        this.dialogTag = dialogTag;
    }

    public void setTitle(String title) {
        this.title = title;
        configureHeading();
        measureDialogLayout();
    }

    public void setMessage(String message) {
        this.message = message;
        configureHeading();
        measureDialogLayout();
    }

    public void setTitleAndMessage(String title, String message) {
        this.title = title;
        this.message = message;
        configureHeading();
        measureDialogLayout();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void configurePositiveButton(DialogButtonConfigurationCreator configurationCreator) {
        this.positiveButtonConfig = configurationCreator == null ? null : configurationCreator.create(positiveButtonConfig);
        configureButtons();
    }

    public void configureNegativeButton(DialogButtonConfigurationCreator configurationCreator) {
        this.negativeButtonConfig = configurationCreator == null ? null : configurationCreator.create(negativeButtonConfig);
        configureButtons();
    }

    public void addOnPositiveClickListener(DialogButtonClickListener onPositiveClickListener) {
        if (onPositiveClickListener != null && !onPositiveClickListeners.contains(onPositiveClickListener))
            onPositiveClickListeners.add(onPositiveClickListener);
    }

    public void addOnNegativeClickListeners(DialogButtonClickListener onNegativeClickListener) {
        if (onNegativeClickListener != null && !onNegativeClickListeners.contains(onNegativeClickListener))
            onNegativeClickListeners.add(onNegativeClickListener);
    }

    public void addOnCancelListener(DialogCancelListener onCancelListener) {
        if (onCancelListener != null && !onCancelListeners.contains(onCancelListener))
            onCancelListeners.add(onCancelListener);
    }

    public void addOnShowListener(DialogShowListener onShowListener) {
        if (onShowListener != null && !onShowListeners.contains(onShowListener))
            onShowListeners.add(onShowListener);
    }

    public void addOnHideListener(DialogHideListener hideListener) {
        if (hideListener != null && !onHideListeners.contains(hideListener))
            onHideListeners.add(hideListener);
    }

    public void removeOnPositiveClickListener(DialogButtonClickListener onPositiveClickListener) {
        onPositiveClickListeners.remove(onPositiveClickListener);
    }

    public void removeOnNegativeClickListeners(DialogButtonClickListener onNegativeClickListener) {
        onNegativeClickListeners.remove(onNegativeClickListener);
    }

    public void removeOnCancelListener(DialogCancelListener onCancelListener) {
        onCancelListeners.remove(onCancelListener);
    }

    public void removeOnShowListener(DialogShowListener onShowListener) {
        onShowListeners.remove(onShowListener);
    }

    public void removeOnHideListener(DialogHideListener hideListener) {
        onHideListeners.remove(hideListener);
    }

    public void show() {
        if (shown)
            return;
        Fragment prev = fragmentManager.findFragmentByTag(dialogTag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (prev != null)
            transaction.remove(prev);
        show(transaction, dialogTag);
    }

    protected RegularDialogConstraints getRegularConstraints() {
        return null;
    }

    protected BottomSheetDialogConstraints getBottomSheetConstraints() {
        return null;
    }

    protected final int getPercentOfWindowHeight(int heightInPercents) {
        if (heightInPercents < 0)
            heightInPercents = 0;
        if (heightInPercents > 100)
            heightInPercents = 100;
        return getWindowHeight() * heightInPercents / 100;
    }

    protected final int getPercentOfWindowWidth(int widthInPercents) {
        if (widthInPercents < 0)
            widthInPercents = 0;
        if (widthInPercents > 100)
            widthInPercents = 100;
        return getWindowWidth() * widthInPercents / 100;
    }

    protected final int getWindowHeight() {
        Activity activity = (Activity) getContext();
        if (activity == null)
            return -1;
        return WindowUtils.getWindowHeight(activity);
    }

    protected final int getWindowWidth() {
        Activity activity = (Activity) getContext();
        if (activity == null)
            return -1;
        return WindowUtils.getWindowWidth(activity);
    }

    protected final void measureDialogLayout() {
        Dialog dialog = getDialog();
        if (dialog == null || dialog.getWindow() == null)
            return;
        Window window = dialog.getWindow();
        Rect fgPadding = new Rect();
        window.getDecorView().getBackground().getPadding(fgPadding);
        switch (dialogType) {
            case FULLSCREEN:
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // overrides background to remove insets
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                setupFullScreenDialog(window, rootView);
                break;
            case NORMAL:
                setupRegularDialog(regularDialogConstraints, window, rootView, fgPadding);
                break;
            case BOTTOM_SHEET:
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // overrides background to remove insets
                setupBottomSheetDialog(bottomSheetConstraints, window, rootView);
                break;
        }
    }

    protected void setupRegularDialog(RegularDialogConstraints constraints, Window dialogWindow, View dialogLayout, Rect fgPadding) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec);
        int horPadding = fgPadding.left + fgPadding.right;
        int verPadding = fgPadding.top + fgPadding.bottom;
        int desiredWidth = dialogLayout.getMeasuredWidth();
        int desiredHeight = dialogLayout.getMeasuredHeight();
        desiredWidth = constraints.resolveWidth(desiredWidth);
        desiredHeight = constraints.resolveHeight(desiredHeight);
        dialogWindow.setLayout(desiredWidth + horPadding, desiredHeight + verPadding);
    }

    protected void setupFullScreenDialog(Window dialogWindow, View dialogLayout) {
    }

    protected void setupBottomSheetDialog(BottomSheetDialogConstraints constraints, Window dialogWindow, View dialogLayout) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWindowWidth(), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec);
        int desiredHeight = dialogLayout.getMeasuredHeight();
        desiredHeight = constraints.resolveHeight(desiredHeight);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight);
    }

    private void handleDismiss() {
        if (!shown)
            return;
        dismiss();
    }

    private void configureHeading() {
        View dialogView = rootView;
        if (dialogView == null)
            return;
        titleAndMessageContainer = dialogView.findViewById(R.id.titleAndMessageContainer);
        TextView titleTextView = dialogView.findViewById(R.id.title);
        TextView messageTextView = dialogView.findViewById(R.id.message);
        if (titleAndMessageContainer == null)
            return;
        if (titleTextView == null && messageTextView == null || (StringUtils.isNullOrEmptyString(title) && StringUtils.isNullOrEmptyString(message)))
            titleAndMessageContainer.setVisibility(View.GONE);
        else
            titleAndMessageContainer.setVisibility(View.VISIBLE);
        if (titleTextView != null) {
            if (StringUtils.isNullOrEmptyString(title))
                titleTextView.setVisibility(View.GONE);
            else {
                titleTextView.setVisibility(View.VISIBLE);
                titleTextView.setText(title);
            }
        }
        if (messageTextView != null) {
            if (StringUtils.isNullOrEmptyString(message))
                messageTextView.setVisibility(View.GONE);
            else {
                messageTextView.setVisibility(View.VISIBLE);
                messageTextView.setText(message);
            }
        }
    }

    private void configureButtons() {
        View dialogView = rootView;
        if (dialogView == null)
            return;
        buttonsContainer = dialogView.findViewById(R.id.buttonsContainer);
        buttonPositive = dialogView.findViewById(R.id.pos_button);
        buttonNegative = dialogView.findViewById(R.id.neg_button);
        if (buttonsContainer == null)
            return;
        if (buttonPositive == null && buttonNegative == null || (positiveButtonConfig == null && negativeButtonConfig == null))
            buttonsContainer.setVisibility(View.GONE);
        if (buttonPositive != null && positiveButtonConfig != null) {
            buttonPositive.setEnabled(positiveButtonConfig.getButtonEnabled());
            buttonPositive.setTextColor(buttonPositive.getTextColors().withAlpha(positiveButtonConfig.getButtonEnabled() ? 255 : 140));
            if (StringUtils.isNullOrEmptyString(positiveButtonConfig.getButtonTitle()))
                buttonPositive.setVisibility(View.GONE);
            else {
                buttonPositive.setText(positiveButtonConfig.getButtonTitle());
                buttonPositive.setOnClickListener(view -> {
                    onPositiveClickListeners.forEach(listener -> listener.onClick(view, this));
                    if (positiveButtonConfig.getCloseDialogOnClick())
                        handleDismiss();
                });
            }
        }
        if (buttonNegative != null && negativeButtonConfig != null) {
            buttonNegative.setEnabled(negativeButtonConfig.getButtonEnabled());
            buttonNegative.setTextColor(buttonNegative.getTextColors().withAlpha(negativeButtonConfig.getButtonEnabled() ? 255 : 140));
            if (StringUtils.isNullOrEmptyString(negativeButtonConfig.getButtonTitle()))
                buttonNegative.setVisibility(View.GONE);
            else {
                buttonNegative.setText(negativeButtonConfig.getButtonTitle());
                buttonNegative.setOnClickListener(view -> {
                    onNegativeClickListeners.forEach(listener -> listener.onClick(view, this));
                    if (negativeButtonConfig.getCloseDialogOnClick())
                        handleDismiss();
                });
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private View setBottomSheetDialogLayout() {
        if (!cancelableOnClickOutside)
            return setDialogLayout(LayoutInflater.from(getContext()));
        CoordinatorLayout parent = new CoordinatorLayout(getContext());
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        BottomSheetBehavior<View> behavior = new BottomSheetBehavior<>();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setFitToContents(true);
        final boolean[] handlingFling = {false};
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (handlingFling[0]) {
                    handlingFling[0] = false;
                    return;
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss();
                    return;
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    return;
                float threshold = (float) (bottomSheet.getHeight() * 0.45);
                behavior.setState((bottomSheet.getTop() > threshold) ? BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        params.setBehavior(behavior);
        View child = setDialogLayout(LayoutInflater.from(getContext()));
        child.setLayoutParams(params);
        parent.addView(child);
        GestureDetector gesture = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityY > 2000) {
                    handlingFling[0] = true;
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        parent.setOnTouchListener((v, event) -> gesture.onTouchEvent(event));
        return parent;
    }

    protected static final class DialogBundleHelper {
        private static final String DIALOG_TITLE_TEXT_TAG = "DIALOG_TITLE_TEXT_TAG";
        private static final String DIALOG_MESSAGE_TEXT_TAG = "DIALOG_MESSAGE_TEXT_TAG";
        private static final String DIALOG_POSITIVE_BUTTON_CONFIG_TAG = "DIALOG_POSITIVE_BUTTON_CONFIG_TAG";
        private static final String DIALOG_NEGATIVE_BUTTON_CONFIG_TAG = "DIALOG_NEGATIVE_BUTTON_CONFIG_TAG";
        private static final String DIALOG_CANCELABLE_TAG = "DIALOG_CANCELABLE_TAG";
        private static final String DIALOG_SHOWING_TAG = "DIALOG_SHOWING_TAG";
        private static final String DIALOG_TYPE_TAG = "DIALOG_TYPE_TAG";
        private static final String DIALOG_FULLSCREEN_TAG = "DIALOG_FULLSCREEN_TAG";
        private static final String DIALOG_ANIMATION_TAG = "DIALOG_ANIMATION_TAG";
        private final Bundle bundle;

        public DialogBundleHelper(Bundle bundle) {
            this.bundle = bundle;
        }

        public DialogBundleHelper() {
            bundle = new Bundle();
        }

        public Bundle getBundle() {
            return bundle;
        }

        public String getTitle() {
            return bundle.getString(DIALOG_TITLE_TEXT_TAG);
        }

        public String getMessage() {
            return bundle.getString(DIALOG_MESSAGE_TEXT_TAG);
        }

        public DialogButtonConfiguration getPositiveButtonConfig() {
            return bundle.getParcelable(DIALOG_POSITIVE_BUTTON_CONFIG_TAG);
        }

        public DialogButtonConfiguration getNegativeButtonConfig() {
            return bundle.getParcelable(DIALOG_NEGATIVE_BUTTON_CONFIG_TAG);
        }

        public boolean getCancelable() {
            return bundle.getBoolean(DIALOG_CANCELABLE_TAG);
        }

        public boolean getShowing() {
            return bundle.getBoolean(DIALOG_CANCELABLE_TAG);
        }

        public DialogTypes getDialogType() {
            return DialogTypes.valueOf(bundle.getInt(DIALOG_TYPE_TAG));
        }

        public boolean getFullscreen() {
            return bundle.getBoolean(DIALOG_FULLSCREEN_TAG);
        }

        public DialogAnimationTypes getAnimationType() {
            return DialogAnimationTypes.valueOf(bundle.getInt(DIALOG_ANIMATION_TAG));
        }

        public DialogBundleHelper withTitle(String dialogTitle) {
            bundle.putString(DIALOG_TITLE_TEXT_TAG, dialogTitle);
            return this;
        }

        public DialogBundleHelper withMessage(String dialogMessage) {
            bundle.putString(DIALOG_MESSAGE_TEXT_TAG, dialogMessage);
            return this;
        }

        public DialogBundleHelper withPositiveButtonConfig(DialogButtonConfiguration positiveButtonConfig) {
            bundle.putParcelable(DIALOG_POSITIVE_BUTTON_CONFIG_TAG, positiveButtonConfig);
            return this;
        }

        public DialogBundleHelper withNegativeButtonConfig(DialogButtonConfiguration negativeButtonConfig) {
            bundle.putParcelable(DIALOG_NEGATIVE_BUTTON_CONFIG_TAG, negativeButtonConfig);
            return this;
        }

        public DialogBundleHelper withShowing(boolean showing) {
            bundle.putBoolean(DIALOG_SHOWING_TAG, showing);
            return this;
        }

        public DialogBundleHelper withCancelable(boolean cancelable) {
            bundle.putBoolean(DIALOG_CANCELABLE_TAG, cancelable);
            return this;
        }

        public DialogBundleHelper withDialogType(DialogTypes dialogType) {
            bundle.putInt(DIALOG_TYPE_TAG, dialogType.getValue());
            return this;
        }

        public DialogBundleHelper withFullScreen(boolean fullScreen) {
            bundle.putBoolean(DIALOG_FULLSCREEN_TAG, fullScreen);
            return this;
        }

        public DialogBundleHelper withAnimation(DialogAnimationTypes animationType) {
            bundle.putInt(DIALOG_ANIMATION_TAG, animationType.getValue());
            return this;
        }
    }

    public static class DialogButtonConfiguration implements Parcelable {
        private String buttonTitle;
        private Boolean buttonEnabled;
        private Boolean closeDialogOnClick;

        public DialogButtonConfiguration(String buttonTitle) {
            this.buttonTitle = buttonTitle;
            this.buttonEnabled = true;
            this.closeDialogOnClick = true;
        }

        public DialogButtonConfiguration(String buttonTitle, boolean buttonEnabled) {
            this.buttonTitle = buttonTitle;
            this.buttonEnabled = buttonEnabled;
            this.closeDialogOnClick = true;
        }

        public DialogButtonConfiguration(String buttonTitle, boolean buttonEnabled, boolean closeDialogOnClick) {
            this.buttonTitle = buttonTitle;
            this.buttonEnabled = buttonEnabled;
            this.closeDialogOnClick = closeDialogOnClick;
        }

        public DialogButtonConfiguration(Parcel in) {
            this.buttonTitle = ParcelableUtils.readString(in);
            this.buttonEnabled = ParcelableUtils.readBoolean(in);
            this.closeDialogOnClick = ParcelableUtils.readBoolean(in);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            ParcelableUtils.writeString(dest, buttonTitle)
                    .writeBoolean(dest, buttonEnabled)
                    .writeBoolean(dest, closeDialogOnClick);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public DialogButtonConfiguration setButtonTitle(String buttonTitle) {
            this.buttonTitle = buttonTitle;
            return this;
        }

        public DialogButtonConfiguration setButtonEnabled(Boolean buttonEnabled) {
            this.buttonEnabled = buttonEnabled;
            return this;
        }

        public DialogButtonConfiguration setCloseDialogOnClick(Boolean closeDialogOnClick) {
            this.closeDialogOnClick = closeDialogOnClick;
            return this;
        }

        public String getButtonTitle() {
            return buttonTitle;
        }

        public Boolean getButtonEnabled() {
            return buttonEnabled;
        }

        public Boolean getCloseDialogOnClick() {
            return closeDialogOnClick;
        }

        public static final Creator<DialogButtonConfiguration> CREATOR = new Creator<DialogButtonConfiguration>() {
            public DialogButtonConfiguration createFromParcel(Parcel in) {
                return new DialogButtonConfiguration(in);
            }

            public DialogButtonConfiguration[] newArray(int size) {
                return new DialogButtonConfiguration[size];
            }
        };
    }

    public interface DialogButtonConfigurationCreator {
        DialogButtonConfiguration create(DialogButtonConfiguration currentConfiguration);
    }

    public interface DialogShowListener {
        void onShow(BaseDialogFragment dialogFragment);
    }

    public interface DialogHideListener {
        void onHide(BaseDialogFragment dialogFragment);
    }

    public interface DialogCancelListener {
        void onCancel(BaseDialogFragment dialogFragment);
    }

    public interface DialogButtonClickListener {
        void onClick(View view, BaseDialogFragment dialogFragment);
    }

    public enum DialogTypes {
        NORMAL(1),
        FULLSCREEN(2),
        BOTTOM_SHEET(3);

        private final int value;
        private static final SparseArray<DialogTypes> values = new SparseArray<>();

        DialogTypes(int value) {
            this.value = value;
        }

        static {
            for (DialogTypes animType : DialogTypes.values()) {
                values.put(animType.value, animType);
            }
        }

        public static DialogTypes valueOf(int animType) {
            return values.get(animType);
        }

        public int getValue() {
            return value;
        }
    }

    public enum DialogAnimationTypes {
        FADE(1),
        NO_ANIMATION(2),
        TRANSITION_FROM_LEFT_TO_RIGHT(3),
        TRANSITION_FROM_TOP_TO_BOTTOM(4),
        TRANSITION_FROM_BOTTOM_TO_BOTTOM(5);

        private final int value;
        private static final SparseArray<DialogAnimationTypes> values = new SparseArray<>();

        DialogAnimationTypes(int value) {
            this.value = value;
        }

        static {
            for (DialogAnimationTypes animType : DialogAnimationTypes.values()) {
                values.put(animType.value, animType);
            }
        }

        public static DialogAnimationTypes valueOf(int animType) {
            return values.get(animType);
        }

        public int getValue() {
            return value;
        }
    }
}