package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.java.commons.string.StringUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ParcelableUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.WindowUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import java.util.ArrayList
import java.util.function.Consumer

abstract class BaseDialogFragment : DialogFragment(), DefaultLifecycleObserver {
    private var rootView: View? = null
    protected var title: String? = null
        set(value) {
            field = value
            configureHeading()
            measureDialogLayout()
        }
    protected var message: String? = null
        set(value) {
            field = value
            configureHeading()
            measureDialogLayout()
        }
    private var dialogTag: String? = null
    private var cancelableOnClickOutside = true
    private var positiveButtonConfig: DialogButtonConfiguration? = null
    private var negativeButtonConfig: DialogButtonConfiguration? = null
    var isDialogShown = false
        private set
    var isFullscreen = false
        protected set
    protected var isLifecycleOwnerInStateAllowingShow = false
    var dialogType: DialogTypes? = null
        private set
    private var animationType: DialogAnimationTypes? = null
    private var parentFragmentManager: FragmentManager? = null
    protected var titleAndMessageContainer: LinearLayoutCompat? = null
    protected var buttonsContainer: LinearLayoutCompat? = null
    protected var buttonPositive: Button? = null
    protected var buttonNegative: Button? = null
    protected var regularDialogConstraints: RegularDialogConstraints? = null
    protected var bottomSheetDialogConstraints: BottomSheetDialogConstraints? = null
    private val onPositiveClickListeners = ArrayList<DialogButtonClickListener>()
    private val onNegativeClickListeners = ArrayList<DialogButtonClickListener>()
    private val onShowListeners = ArrayList<DialogShowListener>()
    private val onHideListeners = ArrayList<DialogHideListener>()
    private val onCancelListeners = ArrayList<DialogCancelListener>()
    private var dialogCallbacks: DialogCallbacks? = null
    private var dialogLifecycleOwner: LifecycleOwner? = null
    protected abstract fun setDialogLayout(inflater: LayoutInflater?): View
    protected abstract fun configureContent(view: View?, savedInstanceState: Bundle?)
    protected open fun create(dialogArguments: Bundle?, savedInstanceState: Bundle?) {}
    protected fun viewCreated(view: View?, savedInstanceState: Bundle?) {}
    protected fun viewRestored(savedInstanceState: Bundle?) {}
    protected open fun saveInstanceState(outState: Bundle?) {}
    protected fun onDismiss() {}

    @Override
    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        isLifecycleOwnerInStateAllowingShow = true
    }

    @Override
    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
        isLifecycleOwnerInStateAllowingShow = false
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super<DialogFragment>.onCreate(savedInstanceState)
        val helper = DialogBundleHelper(savedInstanceState ?: arguments)
        if (savedInstanceState == null) {
            requireNotNull(helper.bundle) { "Bundle args required" }
            dialogType = helper.getDialogType()
            when (dialogType) {
                DialogTypes.NORMAL -> {
                    isFullscreen = false
                    animationType = helper.getAnimationType()
                }
                DialogTypes.FULLSCREEN -> {
                    isFullscreen = true
                    animationType = helper.getAnimationType()
                }
                DialogTypes.BOTTOM_SHEET -> {
                    isFullscreen = false
                    animationType = DialogAnimationTypes.TRANSITION_FROM_BOTTOM_TO_BOTTOM
                }
                else -> {}
            }
            if (title == null) //if not set outside of builder | otherwise ignore
                title = helper.getTitle()
            if (message == null) //if not set outside of builder | otherwise ignore
                message = helper.getMessage()
            if (positiveButtonConfig == null) //if not set outside of builder | otherwise ignore
                positiveButtonConfig = helper.getPositiveButtonConfig()
            if (negativeButtonConfig == null) //if not set outside of builder | otherwise ignore
                negativeButtonConfig = helper.getNegativeButtonConfig()
            cancelableOnClickOutside = helper.cancelable
        } else {
            title = helper.getTitle()
            message = helper.getMessage()
            positiveButtonConfig = helper.getPositiveButtonConfig()
            negativeButtonConfig = helper.getNegativeButtonConfig()
            cancelableOnClickOutside = helper.cancelable
            isDialogShown = helper.showing
            isFullscreen = helper.getFullscreen()
            dialogType = helper.getDialogType()
            animationType = helper.getAnimationType()
        }
        isCancelable = cancelableOnClickOutside
        create(arguments, savedInstanceState)
        if (dialogCallbacks != null) dialogCallbacks!!.onCreate(this, arguments, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val helper = DialogBundleHelper(outState)
            .withTitle(title)
            .withMessage(message)
            .withPositiveButtonConfig(positiveButtonConfig)
            .withNegativeButtonConfig(negativeButtonConfig)
            .withCancelable(cancelableOnClickOutside)
            .withShowing(isDialogShown)
            .withFullScreen(isFullscreen)
            .withDialogType(dialogType)
            .withAnimation(animationType)
        saveInstanceState(helper.bundle)
        if (dialogCallbacks != null) dialogCallbacks!!.onSaveInstanceState(this, view, outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        regularDialogConstraints = regularConstraints
        bottomSheetDialogConstraints = bottomSheetConstraints
        if (regularDialogConstraints == null)
            regularDialogConstraints = RegularDialogConstraintsBuilder(this)
                .default()
                .build()
        if (bottomSheetDialogConstraints == null)
            bottomSheetDialogConstraints = BottomSheetDialogConstraintsBuilder(this)
                .default()
                .build()
        when (dialogType) {
            DialogTypes.NORMAL, DialogTypes.FULLSCREEN -> rootView = setDialogLayout(
                LayoutInflater.from(
                    context
                )
            )
            DialogTypes.BOTTOM_SHEET -> rootView = setBottomSheetDialogLayout()
            else -> {}
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureHeading()
        configureButtons()
        configureContent(rootView, savedInstanceState)
        measureDialogLayout()
        viewCreated(view, savedInstanceState)
        if (dialogCallbacks != null) dialogCallbacks!!.onInflated(this, view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewRestored(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        isDialogShown = true
        dialog.setOnShowListener {
            if (savedInstanceState == null)
                for (onShowListener in onShowListeners)
                    onShowListener.onShow(this)
            // executes animation and onShowEvent only if dialog is shown for the first time in it's lifecycle
        }
        return dialog
    }

    override fun onStart() {
        super<DialogFragment>.onStart()
        if (dialog == null || dialog!!.window == null) return
        val dialogWindow = dialog!!.window
        when (animationType) {
            DialogAnimationTypes.NO_ANIMATION -> dialogWindow!!.setWindowAnimations(R.style.NoAnimation)
            DialogAnimationTypes.FADE -> dialogWindow!!.setWindowAnimations(R.style.Animation_Fade)
            DialogAnimationTypes.TRANSITION_FROM_BOTTOM_TO_BOTTOM -> dialogWindow!!.setWindowAnimations(R.style.Animation_FromBottomToBottom)
            DialogAnimationTypes.TRANSITION_FROM_LEFT_TO_RIGHT -> dialogWindow!!.setWindowAnimations(R.style.Animation_FromLeftToRight)
            DialogAnimationTypes.TRANSITION_FROM_TOP_TO_BOTTOM -> dialogWindow!!.setWindowAnimations(R.style.Animation_FromTopToBottom)
            else -> {}
        }
    }

    override fun dismiss() {
        if (!isDialogShown) return
        super.dismiss()
        isDialogShown = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        for (onHideListener in onHideListeners)
            onHideListener.onHide(this)
        onDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        for (onCancelListener in onCancelListeners)
            onCancelListener.onCancel(this)
        dismiss()
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (checkIfDialogCanBeShown()) super.show(transaction, tag) else -1
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (checkIfDialogCanBeShown()) super.showNow(manager, tag)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (checkIfDialogCanBeShown()) super.show(manager, tag)
    }

    fun show() {
        if (!checkIfDialogCanBeShown()) return
        val prev = parentFragmentManager!!.findFragmentByTag(dialogTag)
        val transaction = parentFragmentManager!!.beginTransaction()
        if (prev != null) transaction.remove(prev)
        super.show(transaction, dialogTag)
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        dialogLifecycleOwner = lifecycleOwner
        if (dialogLifecycleOwner == null) return
        dialogLifecycleOwner!!.lifecycle.removeObserver(this)
        dialogLifecycleOwner!!.lifecycle.addObserver(this)
    }

    fun setFragmentManager(fragmentManager: FragmentManager?) {
        this.parentFragmentManager = fragmentManager
    }

    fun setDialogTag(dialogTag: String?) {
        this.dialogTag = dialogTag
    }

    fun setTitleAndMessage(title: String?, message: String?) {
        this.title = title
        this.message = message
        configureHeading()
        measureDialogLayout()
    }

    fun configurePositiveButton(configurationCreator: DialogButtonConfigurationCreator?) {
        positiveButtonConfig = configurationCreator?.create(positiveButtonConfig)
        configureButtons()
    }

    fun configureNegativeButton(configurationCreator: DialogButtonConfigurationCreator?) {
        negativeButtonConfig = configurationCreator?.create(negativeButtonConfig)
        configureButtons()
    }

    fun addOnPositiveClickListener(onPositiveClickListener: DialogButtonClickListener?) {
        if (onPositiveClickListener != null && !onPositiveClickListeners.contains(onPositiveClickListener)) onPositiveClickListeners.add(
            onPositiveClickListener
        )
    }

    fun addOnNegativeClickListeners(onNegativeClickListener: DialogButtonClickListener?) {
        if (onNegativeClickListener != null && !onNegativeClickListeners.contains(onNegativeClickListener)) onNegativeClickListeners.add(
            onNegativeClickListener
        )
    }

    fun addOnCancelListener(onCancelListener: DialogCancelListener?) {
        if (onCancelListener != null && !onCancelListeners.contains(onCancelListener)) onCancelListeners.add(onCancelListener)
    }

    fun addOnShowListener(onShowListener: DialogShowListener?) {
        if (onShowListener != null && !onShowListeners.contains(onShowListener)) onShowListeners.add(onShowListener)
    }

    fun addOnHideListener(hideListener: DialogHideListener?) {
        if (hideListener != null && !onHideListeners.contains(hideListener)) onHideListeners.add(hideListener)
    }

    fun removeOnPositiveClickListener(onPositiveClickListener: DialogButtonClickListener) {
        onPositiveClickListeners.remove(onPositiveClickListener)
    }

    fun removeOnNegativeClickListeners(onNegativeClickListener: DialogButtonClickListener) {
        onNegativeClickListeners.remove(onNegativeClickListener)
    }

    fun removeOnCancelListener(onCancelListener: DialogCancelListener) {
        onCancelListeners.remove(onCancelListener)
    }

    fun removeOnShowListener(onShowListener: DialogShowListener) {
        onShowListeners.remove(onShowListener)
    }

    fun removeOnHideListener(hideListener: DialogHideListener) {
        onHideListeners.remove(hideListener)
    }

    fun setDialogCallbacks(callbacks: DialogCallbacks?) {
        dialogCallbacks = callbacks
    }

    protected open val regularConstraints: RegularDialogConstraints?
        get() = null

    protected open val bottomSheetConstraints: BottomSheetDialogConstraints?
        get() = null

    protected open fun canShowDialog(dialogLifecycleOwner: LifecycleOwner?): Boolean {
        if (dialogLifecycleOwner == null) Log.w(
            javaClass.name,
            "You are using dialog without lifecycle owner. This may produce unexpected behaviour when trying to show the dialog in specific lifecycle states. It is highly recommended to build your dialog with a lifecycle ownner."
        )
        if (dialogLifecycleOwner != null && !isLifecycleOwnerInStateAllowingShow) {
            Log.w(
                javaClass.name,
                "Showing of dialog cancelled: Lifecycle owner is not in valid state. You are probably trying to show dialog after saveInstanceState has been executed."
            )
            return false
        }
        return true
    }

    protected fun getPercentOfWindowHeight(heightInPercents: Int): Int {
        return windowHeight * validatePercents(heightInPercents) / 100
    }

    protected fun getPercentOfWindowWidth(widthInPercents: Int): Int {
        return windowWidth * validatePercents(widthInPercents) / 100
    }

    protected val windowHeight: Int
        get() {
            val activity = context as Activity? ?: return -1
            return WindowUtils.getWindowHeight(activity)
        }

    protected val windowWidth: Int
        get() {
            val activity = context as Activity? ?: return -1
            return WindowUtils.getWindowWidth(activity)
        }

    protected fun measureDialogLayout() {
        val dialog = dialog
        if (dialog == null || dialog.window == null) return
        val window = dialog.window
        val fgPadding = Rect()
        window!!.decorView.background.getPadding(fgPadding)
        when (dialogType) {
            DialogTypes.FULLSCREEN -> {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // overrides background to remove insets
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                setupFullScreenDialog(window, rootView)
            }
            DialogTypes.NORMAL -> setupRegularDialog(regularDialogConstraints, window, rootView, fgPadding)
            DialogTypes.BOTTOM_SHEET -> {
                window.setGravity(Gravity.BOTTOM)
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // overrides background to remove insets
                setupBottomSheetDialog(bottomSheetDialogConstraints, window, rootView)
            }
            else -> {}
        }
    }

    protected open fun setupRegularDialog(
        constraints: RegularDialogConstraints?,
        dialogWindow: Window?,
        dialogLayout: View?,
        fgPadding: Rect
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxWidth(), View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val params = ViewGroup.LayoutParams(0, 0)
        dialogLayout!!.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var desiredWidth = dialogLayout.measuredWidth
        var desiredHeight = dialogLayout.measuredHeight
        desiredWidth = constraints.resolveWidth(desiredWidth)
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow!!.setLayout(desiredWidth + horPadding, desiredHeight + verPadding)
    }

    protected open fun setupFullScreenDialog(dialogWindow: Window?, dialogLayout: View?) {}
    protected open fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints?,
        dialogWindow: Window?,
        dialogLayout: View?
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(windowWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout!!.measure(widthMeasureSpec, heightMeasureSpec)
        var desiredHeight = dialogLayout.measuredHeight
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight)
    }

    private fun validatePercents(percentsValue: Int): Int {
        return if (percentsValue < 0) 0
        else if (percentsValue > 100) 100
        else percentsValue
    }

    private fun checkIfDialogCanBeShown(): Boolean {
        return !isDialogShown && canShowDialog(dialogLifecycleOwner)
    }

    private fun handleDismiss() {
        if (!isDialogShown) return
        dismiss()
    }

    private fun configureHeading() {
        val dialogView = rootView ?: return
        titleAndMessageContainer = dialogView.findViewById<LinearLayoutCompat>(R.id.titleAndMessageContainer)
        val titleTextView = dialogView.findViewById<TextView>(R.id.title)
        val messageTextView = dialogView.findViewById<TextView>(R.id.message)
        if (titleAndMessageContainer == null) return
        if (titleTextView == null && messageTextView == null || StringUtils.isNullOrEmptyString(title) && StringUtils.isNullOrEmptyString(
                message
            )
        ) titleAndMessageContainer.setVisibility(
            View.GONE
        ) else titleAndMessageContainer.setVisibility(View.VISIBLE)
        if (titleTextView != null) {
            if (StringUtils.isNullOrEmptyString(title)) titleTextView.visibility = View.GONE else {
                titleTextView.visibility = View.VISIBLE
                titleTextView.text = title
            }
        }
        if (messageTextView != null) {
            if (StringUtils.isNullOrEmptyString(message)) messageTextView.visibility = View.GONE else {
                messageTextView.visibility = View.VISIBLE
                messageTextView.text = message
            }
        }
    }

    private fun configureButtons() {
        val dialogView = rootView ?: return
        buttonsContainer = dialogView.findViewById<LinearLayoutCompat>(R.id.buttonsContainer)
        buttonPositive = dialogView.findViewById(R.id.pos_button)
        buttonNegative = dialogView.findViewById(R.id.neg_button)
        if (buttonsContainer == null) return
        if (buttonPositive == null && buttonNegative == null || positiveButtonConfig == null && negativeButtonConfig == null) buttonsContainer.setVisibility(
            View.GONE
        )
        if (buttonPositive != null && positiveButtonConfig == null) buttonPositive!!.visibility = View.GONE
        if (buttonNegative != null && negativeButtonConfig == null) buttonNegative!!.visibility = View.GONE
        if (buttonPositive != null && positiveButtonConfig != null) {
            buttonPositive!!.isEnabled = positiveButtonConfig!!.buttonEnabled
            buttonPositive!!.setTextColor(buttonPositive!!.textColors.withAlpha(if (positiveButtonConfig!!.buttonEnabled) 255 else 140))
            if (StringUtils.isNullOrEmptyString(positiveButtonConfig!!.buttonTitle)) buttonPositive!!.visibility =
                View.GONE else {
                buttonPositive!!.text = positiveButtonConfig!!.buttonTitle
                buttonPositive!!.setOnClickListener { view: View? ->
                    onPositiveClickListeners.forEach(Consumer { listener: DialogButtonClickListener ->
                        listener.onClick(
                            view,
                            this
                        )
                    })
                    if (positiveButtonConfig!!.closeDialogOnClick) handleDismiss()
                }
            }
        }
        if (buttonNegative != null && negativeButtonConfig != null) {
            buttonNegative!!.isEnabled = negativeButtonConfig!!.buttonEnabled
            buttonNegative!!.setTextColor(buttonNegative!!.textColors.withAlpha(if (negativeButtonConfig!!.buttonEnabled) 255 else 140))
            if (StringUtils.isNullOrEmptyString(negativeButtonConfig!!.buttonTitle)) buttonNegative!!.visibility =
                View.GONE else {
                buttonNegative!!.text = negativeButtonConfig!!.buttonTitle
                buttonNegative!!.setOnClickListener { view: View? ->
                    onNegativeClickListeners.forEach(Consumer { listener: DialogButtonClickListener ->
                        listener.onClick(
                            view,
                            this
                        )
                    })
                    if (negativeButtonConfig!!.closeDialogOnClick) handleDismiss()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBottomSheetDialogLayout(): View {
        if (!cancelableOnClickOutside) return setDialogLayout(LayoutInflater.from(context))
        val parent = CoordinatorLayout(context)
        val params: CoordinatorLayout.LayoutParams =
            CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val behavior: BottomSheetBehavior<View> = BottomSheetBehavior<View>()
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        behavior.setFitToContents(true)
        val handlingFling = booleanArrayOf(false)
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (handlingFling[0]) {
                    handlingFling[0] = false
                    return
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss()
                    return
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) return
                val threshold = (bottomSheet.height * 0.45).toFloat()
                behavior.setState(if (bottomSheet.top > threshold) BottomSheetBehavior.STATE_COLLAPSED else BottomSheetBehavior.STATE_EXPANDED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        params.setBehavior(behavior)
        val child = setDialogLayout(LayoutInflater.from(context))
        child.layoutParams = params
        parent.addView(child)
        val gesture = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (velocityY > 2000) {
                    handlingFling[0] = true
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                } else behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
        parent.setOnTouchListener(View.OnTouchListener { v: View?, event: MotionEvent? ->
            gesture.onTouchEvent(
                event!!
            )
        })
        return parent
    }

    protected class DialogBundleHelper {
        val bundle: Bundle?

        constructor(bundle: Bundle?) {
            this.bundle = bundle
        }

        constructor() {
            bundle = Bundle()
        }

        fun getTitle(): String? {
            return bundle!!.getString(DIALOG_TITLE_TEXT_TAG)
        }

        fun getMessage(): String? {
            return bundle!!.getString(DIALOG_MESSAGE_TEXT_TAG)
        }

        fun getPositiveButtonConfig(): DialogButtonConfiguration? {
            return bundle!!.getParcelable(DIALOG_POSITIVE_BUTTON_CONFIG_TAG)
        }

        fun getNegativeButtonConfig(): DialogButtonConfiguration? {
            return bundle!!.getParcelable(DIALOG_NEGATIVE_BUTTON_CONFIG_TAG)
        }

        val cancelable: Boolean
            get() = bundle!!.getBoolean(DIALOG_CANCELABLE_TAG)
        val showing: Boolean
            get() = bundle!!.getBoolean(DIALOG_CANCELABLE_TAG)

        fun getDialogType(): DialogTypes {
            return DialogTypes.valueOf(bundle!!.getInt(DIALOG_TYPE_TAG))
        }

        fun getFullscreen(): Boolean {
            return bundle!!.getBoolean(DIALOG_FULLSCREEN_TAG)
        }

        fun getAnimationType(): DialogAnimationTypes {
            return DialogAnimationTypes.valueOf(bundle!!.getInt(DIALOG_ANIMATION_TAG))
        }

        fun withTitle(dialogTitle: String?): DialogBundleHelper {
            bundle!!.putString(DIALOG_TITLE_TEXT_TAG, dialogTitle)
            return this
        }

        fun withMessage(dialogMessage: String?): DialogBundleHelper {
            bundle!!.putString(DIALOG_MESSAGE_TEXT_TAG, dialogMessage)
            return this
        }

        fun withPositiveButtonConfig(positiveButtonConfig: DialogButtonConfiguration?): DialogBundleHelper {
            bundle!!.putParcelable(DIALOG_POSITIVE_BUTTON_CONFIG_TAG, positiveButtonConfig)
            return this
        }

        fun withNegativeButtonConfig(negativeButtonConfig: DialogButtonConfiguration?): DialogBundleHelper {
            bundle!!.putParcelable(DIALOG_NEGATIVE_BUTTON_CONFIG_TAG, negativeButtonConfig)
            return this
        }

        fun withShowing(showing: Boolean): DialogBundleHelper {
            bundle!!.putBoolean(DIALOG_SHOWING_TAG, showing)
            return this
        }

        fun withCancelable(cancelable: Boolean): DialogBundleHelper {
            bundle!!.putBoolean(DIALOG_CANCELABLE_TAG, cancelable)
            return this
        }

        fun withDialogType(dialogType: DialogTypes?): DialogBundleHelper {
            bundle!!.putInt(DIALOG_TYPE_TAG, dialogType!!.value)
            return this
        }

        fun withFullScreen(fullScreen: Boolean): DialogBundleHelper {
            bundle!!.putBoolean(DIALOG_FULLSCREEN_TAG, fullScreen)
            return this
        }

        fun withAnimation(animationType: DialogAnimationTypes?): DialogBundleHelper {
            bundle!!.putInt(DIALOG_ANIMATION_TAG, animationType!!.value)
            return this
        }

        companion object {
            private const val DIALOG_TITLE_TEXT_TAG = "DIALOG_TITLE_TEXT_TAG"
            private const val DIALOG_MESSAGE_TEXT_TAG = "DIALOG_MESSAGE_TEXT_TAG"
            private const val DIALOG_POSITIVE_BUTTON_CONFIG_TAG = "DIALOG_POSITIVE_BUTTON_CONFIG_TAG"
            private const val DIALOG_NEGATIVE_BUTTON_CONFIG_TAG = "DIALOG_NEGATIVE_BUTTON_CONFIG_TAG"
            private const val DIALOG_CANCELABLE_TAG = "DIALOG_CANCELABLE_TAG"
            private const val DIALOG_SHOWING_TAG = "DIALOG_SHOWING_TAG"
            private const val DIALOG_TYPE_TAG = "DIALOG_TYPE_TAG"
            private const val DIALOG_FULLSCREEN_TAG = "DIALOG_FULLSCREEN_TAG"
            private const val DIALOG_ANIMATION_TAG = "DIALOG_ANIMATION_TAG"
        }
    }

    class DialogButtonConfiguration : Parcelable {
        var buttonTitle: String
            private set
        var buttonEnabled: Boolean
            private set
        var closeDialogOnClick: Boolean
            private set

        constructor(buttonTitle: String) {
            this.buttonTitle = buttonTitle
            buttonEnabled = true
            closeDialogOnClick = true
        }

        constructor(buttonTitle: String, buttonEnabled: Boolean) {
            this.buttonTitle = buttonTitle
            this.buttonEnabled = buttonEnabled
            closeDialogOnClick = true
        }

        constructor(buttonTitle: String, buttonEnabled: Boolean, closeDialogOnClick: Boolean) {
            this.buttonTitle = buttonTitle
            this.buttonEnabled = buttonEnabled
            this.closeDialogOnClick = closeDialogOnClick
        }

        constructor(`in`: Parcel?) {
            buttonTitle = ParcelableUtils.readString(`in`)
            buttonEnabled = ParcelableUtils.readBoolean(`in`)
            closeDialogOnClick = ParcelableUtils.readBoolean(`in`)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            ParcelableUtils.writeString(dest, buttonTitle)
                .writeBoolean(dest, buttonEnabled)
                .writeBoolean(dest, closeDialogOnClick)
        }

        override fun describeContents(): Int {
            return 0
        }

        fun setButtonTitle(buttonTitle: String): DialogButtonConfiguration {
            this.buttonTitle = buttonTitle
            return this
        }

        fun setButtonEnabled(buttonEnabled: Boolean): DialogButtonConfiguration {
            this.buttonEnabled = buttonEnabled
            return this
        }

        fun setCloseDialogOnClick(closeDialogOnClick: Boolean): DialogButtonConfiguration {
            this.closeDialogOnClick = closeDialogOnClick
            return this
        }

        companion object {
            val CREATOR: Parcelable.Creator<DialogButtonConfiguration> =
                object : Parcelable.Creator<DialogButtonConfiguration?> {
                    override fun createFromParcel(`in`: Parcel): DialogButtonConfiguration? {
                        return DialogButtonConfiguration(`in`)
                    }

                    override fun newArray(size: Int): Array<DialogButtonConfiguration?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    interface DialogButtonConfigurationCreator {
        fun create(currentConfiguration: DialogButtonConfiguration?): DialogButtonConfiguration?
    }

    interface DialogShowListener {
        fun onShow(dialogFragment: BaseDialogFragment?)
    }

    interface DialogHideListener {
        fun onHide(dialogFragment: BaseDialogFragment?)
    }

    interface DialogCancelListener {
        fun onCancel(dialogFragment: BaseDialogFragment?)
    }

    interface DialogButtonClickListener {
        fun onClick(view: View?, dialogFragment: BaseDialogFragment?)
    }

    enum class DialogTypes(val value: Int) {
        NORMAL(1), FULLSCREEN(2), BOTTOM_SHEET(3);

        companion object {
            private val values = SparseArray<DialogTypes>()
            fun valueOf(animType: Int): DialogTypes {
                return values[animType]
            }

            init {
                for (animType in values()) {
                    values.put(
                        com.github.rooneyandshadows.lightbulb.dialogs.base.animType.value,
                        com.github.rooneyandshadows.lightbulb.dialogs.base.animType
                    )
                }
            }
        }
    }

    enum class DialogAnimationTypes(val value: Int) {
        FADE(1), NO_ANIMATION(2), TRANSITION_FROM_LEFT_TO_RIGHT(3), TRANSITION_FROM_TOP_TO_BOTTOM(4), TRANSITION_FROM_BOTTOM_TO_BOTTOM(
            5
        );

        companion object {
            private val values = SparseArray<DialogAnimationTypes>()
            fun valueOf(animType: Int): DialogAnimationTypes {
                return values[animType]
            }

            init {
                for (animType in values()) {
                    values.put(
                        com.github.rooneyandshadows.lightbulb.dialogs.base.animType.value,
                        com.github.rooneyandshadows.lightbulb.dialogs.base.animType
                    )
                }
            }
        }
    }

    interface DialogCallbacks {
        fun onCreate(dialog: BaseDialogFragment?, dialogArguments: Bundle?, savedInstanceState: Bundle?)
        fun onInflated(dialog: BaseDialogFragment?, layout: View?, savedInstanceState: Bundle?)
        fun onSaveInstanceState(dialog: BaseDialogFragment?, layout: View?, outState: Bundle?)
    }
}