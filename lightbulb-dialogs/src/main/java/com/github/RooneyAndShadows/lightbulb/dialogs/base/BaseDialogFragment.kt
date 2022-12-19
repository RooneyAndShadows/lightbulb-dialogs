package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.rooneyandshadows.lightbulb.commons.utils.WindowUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.google.android.material.bottomsheet.BottomSheetBehavior

abstract class BaseDialogFragment : DialogFragment(), DefaultLifecycleObserver {
    private lateinit var rootView: View
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
    lateinit var dialogType: DialogTypes
        private set
    private lateinit var animationType: DialogAnimationTypes
    private lateinit var parentFragManager: FragmentManager
    protected var titleAndMessageContainer: LinearLayoutCompat? = null
    protected var buttonsContainer: LinearLayoutCompat? = null
    protected var buttonPositive: Button? = null
    protected var buttonNegative: Button? = null
    private lateinit var regularDialogConstraints: RegularDialogConstraints
    private lateinit var bottomSheetDialogConstraints: BottomSheetDialogConstraints
    private val onPositiveClickListeners = ArrayList<DialogButtonClickListener>()
    private val onNegativeClickListeners = ArrayList<DialogButtonClickListener>()
    private val onShowListeners = ArrayList<DialogShowListener>()
    private val onHideListeners = ArrayList<DialogHideListener>()
    private val onCancelListeners = ArrayList<DialogCancelListener>()
    private var dialogCallbacks: DialogListeners? = null
    private var dialogLifecycleOwner: LifecycleOwner? = null
    protected abstract fun setDialogLayout(inflater: LayoutInflater?): View
    protected abstract fun configureContent(view: View?, savedInstanceState: Bundle?)
    protected open fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {}
    protected fun doOnViewCreated(view: View?, savedInstanceState: Bundle?) {}
    protected fun doOnViewStateRestored(savedInstanceState: Bundle?) {}
    protected open fun doOnSaveInstanceState(outState: Bundle?) {}
    protected fun onDismiss() {}

    @Override
    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        isLifecycleOwnerInStateAllowingShow = true
    }

    @Override
    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        isLifecycleOwnerInStateAllowingShow = false
    }

    @Override
    final override fun onCreate(savedInstanceState: Bundle?) {
        super<DialogFragment>.onCreate(savedInstanceState)
        val bundle = savedInstanceState ?: requireArguments()
        val helper = DialogBundleHelper(bundle)
        if (savedInstanceState == null) {
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
        doOnCreate(arguments, savedInstanceState)
        if (dialogCallbacks != null)
            dialogCallbacks!!.doOnCreate(this, arguments, savedInstanceState)
    }

    @Override
    final override fun onSaveInstanceState(outState: Bundle) {
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
        doOnSaveInstanceState(helper.bundle)
        if (dialogCallbacks != null)
            dialogCallbacks!!.doOnSaveInstanceState(this, view, outState)
    }

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        regularDialogConstraints = getRegularConstraints()
        bottomSheetDialogConstraints = getBottomSheetConstraints()
        rootView = when (dialogType) {
            DialogTypes.NORMAL, DialogTypes.FULLSCREEN -> setDialogLayout(
                LayoutInflater.from(
                    context
                )
            )
            DialogTypes.BOTTOM_SHEET -> setBottomSheetDialogLayout()
        }
        return rootView
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureHeading()
        configureButtons()
        configureContent(rootView, savedInstanceState)
        measureDialogLayout()
        doOnViewCreated(view, savedInstanceState)
        if (dialogCallbacks != null)
            dialogCallbacks!!.doOnInflated(this, view, savedInstanceState)
    }

    final override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        doOnViewStateRestored(savedInstanceState)
    }

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        isDialogShown = true
        dialog.setOnShowListener {
            if (savedInstanceState == null)
                for (onShowListener in onShowListeners)
                    onShowListener.doOnShow(this)
            // executes animation and onShowEvent only if dialog is shown for the first time in it's lifecycle
        }
        return dialog
    }

    final override fun onStart() {
        super<DialogFragment>.onStart()
        if (dialog == null || dialog!!.window == null)
            return
        val dialogWindow = dialog!!.window
        when (animationType) {
            DialogAnimationTypes.NO_ANIMATION -> dialogWindow!!.setWindowAnimations(R.style.NoAnimation)
            DialogAnimationTypes.FADE -> dialogWindow!!.setWindowAnimations(R.style.Animation_Fade)
            DialogAnimationTypes.TRANSITION_FROM_BOTTOM_TO_BOTTOM -> dialogWindow!!.setWindowAnimations(R.style.Animation_FromBottomToBottom)
            DialogAnimationTypes.TRANSITION_FROM_LEFT_TO_RIGHT -> dialogWindow!!.setWindowAnimations(R.style.Animation_FromLeftToRight)
            DialogAnimationTypes.TRANSITION_FROM_TOP_TO_BOTTOM -> dialogWindow!!.setWindowAnimations(R.style.Animation_FromTopToBottom)
        }
    }

    override fun dismiss() {
        if (!isDialogShown) return
        super.dismiss()
        isDialogShown = false
    }

    final override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        for (onHideListener in onHideListeners)
            onHideListener.doOnHide(this)
        onDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        for (onCancelListener in onCancelListeners)
            onCancelListener.doOnCancel(this)
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
        val prev = parentFragManager.findFragmentByTag(dialogTag)
        val transaction = parentFragManager.beginTransaction()
        if (prev != null) transaction.remove(prev)
        super.show(transaction, dialogTag)
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        dialogLifecycleOwner = lifecycleOwner
        if (dialogLifecycleOwner == null) return
        dialogLifecycleOwner!!.lifecycle.removeObserver(this)
        dialogLifecycleOwner!!.lifecycle.addObserver(this)
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.parentFragManager = fragmentManager
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
        if (onPositiveClickListener != null && !onPositiveClickListeners.contains(onPositiveClickListener))
            onPositiveClickListeners.add(onPositiveClickListener)
    }

    fun addOnNegativeClickListeners(onNegativeClickListener: DialogButtonClickListener?) {
        if (onNegativeClickListener != null && !onNegativeClickListeners.contains(onNegativeClickListener))
            onNegativeClickListeners.add(onNegativeClickListener)
    }

    fun addOnCancelListener(onCancelListener: DialogCancelListener?) {
        if (onCancelListener != null && !onCancelListeners.contains(onCancelListener))
            onCancelListeners.add(onCancelListener)
    }

    fun addOnShowListener(onShowListener: DialogShowListener?) {
        if (onShowListener != null && !onShowListeners.contains(onShowListener))
            onShowListeners.add(onShowListener)
    }

    fun addOnHideListener(hideListener: DialogHideListener?) {
        if (hideListener != null && !onHideListeners.contains(hideListener))
            onHideListeners.add(hideListener)
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

    fun setDialogCallbacks(callbacks: DialogListeners?) {
        dialogCallbacks = callbacks
    }

    protected open fun getRegularConstraints(): RegularDialogConstraints {
        return RegularDialogConstraintsBuilder(this)
            .default()
            .build()
    }

    protected open fun getBottomSheetConstraints(): BottomSheetDialogConstraints {
        return BottomSheetDialogConstraintsBuilder(this)
            .default()
            .build()
    }

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
        }
    }

    protected open fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxWidth(), View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val params = ViewGroup.LayoutParams(0, 0)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var desiredWidth = dialogLayout.measuredWidth
        var desiredHeight = dialogLayout.measuredHeight
        desiredWidth = constraints.resolveWidth(desiredWidth)
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(desiredWidth + horPadding, desiredHeight + verPadding)
    }

    protected open fun setupFullScreenDialog(dialogWindow: Window?, dialogLayout: View?) {}

    protected open fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(windowWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        var desiredHeight = dialogLayout.measuredHeight
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight)
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
        val dialogView = rootView
        titleAndMessageContainer = dialogView.findViewById(R.id.titleAndMessageContainer)
        val titleTextView = dialogView.findViewById<TextView>(R.id.title)
        val messageTextView = dialogView.findViewById<TextView>(R.id.message)
        if (titleAndMessageContainer == null) return
        if (titleTextView == null && messageTextView == null || title.isNullOrBlank() && message.isNullOrBlank())
            titleAndMessageContainer!!.visibility = GONE
        else
            titleAndMessageContainer!!.visibility = View.VISIBLE
        if (titleTextView != null) {
            if (title.isNullOrBlank()) titleTextView.visibility = GONE
            else {
                titleTextView.visibility = View.VISIBLE
                titleTextView.text = title
            }
        }
        if (messageTextView != null) {
            if (message.isNullOrBlank()) messageTextView.visibility = GONE
            else {
                messageTextView.visibility = View.VISIBLE
                messageTextView.text = message
            }
        }
    }

    private fun configureButtons() {
        val dialogView = rootView
        buttonsContainer = dialogView.findViewById(R.id.buttonsContainer)
        buttonPositive = dialogView.findViewById(R.id.pos_button)
        buttonNegative = dialogView.findViewById(R.id.neg_button)
        if (buttonsContainer == null)
            return
        if (buttonPositive == null && buttonNegative == null || positiveButtonConfig == null && negativeButtonConfig == null)
            buttonsContainer!!.visibility = GONE
        if (buttonPositive != null && positiveButtonConfig == null)
            buttonPositive!!.visibility = GONE
        if (buttonNegative != null && negativeButtonConfig == null)
            buttonNegative!!.visibility = GONE
        if (buttonPositive != null && positiveButtonConfig != null) {
            buttonPositive!!.isEnabled = positiveButtonConfig!!.buttonEnabled
            buttonPositive!!.setTextColor(buttonPositive!!.textColors.withAlpha(if (positiveButtonConfig!!.buttonEnabled) 255 else 140))
            if (positiveButtonConfig!!.buttonTitle.isBlank()) buttonPositive!!.visibility = GONE
            else {
                buttonPositive!!.text = positiveButtonConfig!!.buttonTitle
                buttonPositive!!.setOnClickListener { view: View? ->
                    for (listener in onPositiveClickListeners)
                        listener.doOnClick(view, this)
                    if (positiveButtonConfig!!.closeDialogOnClick)
                        handleDismiss()
                }
            }
        }
        if (buttonNegative != null && negativeButtonConfig != null) {
            buttonNegative!!.isEnabled = negativeButtonConfig!!.buttonEnabled
            buttonNegative!!.setTextColor(buttonNegative!!.textColors.withAlpha(if (negativeButtonConfig!!.buttonEnabled) 255 else 140))
            if (negativeButtonConfig!!.buttonTitle.isBlank()) buttonNegative!!.visibility = GONE
            else {
                buttonNegative!!.text = negativeButtonConfig!!.buttonTitle
                buttonNegative!!.setOnClickListener { view: View? ->
                    for (listener in onNegativeClickListeners)
                        listener.doOnClick(view, this)
                    if (negativeButtonConfig!!.closeDialogOnClick)
                        handleDismiss()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBottomSheetDialogLayout(): View {
        val context = requireContext()
        if (!cancelableOnClickOutside) return setDialogLayout(LayoutInflater.from(context))
        val parent = CoordinatorLayout(context)
        val params: CoordinatorLayout.LayoutParams = CoordinatorLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val behavior: BottomSheetBehavior<View> = BottomSheetBehavior<View>()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isFitToContents = true
        val handlingFling = booleanArrayOf(false)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @Override
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
                behavior.state =
                    if (bottomSheet.top > threshold) BottomSheetBehavior.STATE_COLLAPSED else BottomSheetBehavior.STATE_EXPANDED
            }

            @Override
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        params.behavior = behavior
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
        parent.setOnTouchListener { _: View?, event: MotionEvent? ->
            gesture.onTouchEvent(event!!)
        }
        return parent
    }
}