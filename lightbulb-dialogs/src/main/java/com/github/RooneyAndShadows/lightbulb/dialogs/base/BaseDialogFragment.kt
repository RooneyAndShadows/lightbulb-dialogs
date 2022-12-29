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
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.*
import com.google.android.material.bottomsheet.BottomSheetBehavior

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseDialogFragment : DialogFragment(), DefaultLifecycleObserver {
    private lateinit var rootView: View
    private lateinit var parentFragManager: FragmentManager
    private lateinit var regularDialogConstraints: RegularDialogConstraints
    private lateinit var bottomSheetDialogConstraints: BottomSheetDialogConstraints
    private var dialogTag: String = javaClass.name.plus("_TAG")
    private var cancelableOnClickOutside = true
    private var isLifecycleOwnerInStateAllowingShow = false
    private val onPositiveClickListeners: MutableList<DialogButtonClickListener> = mutableListOf()
    private val onNegativeClickListeners: MutableList<DialogButtonClickListener> = mutableListOf()
    private val onShowListeners: MutableList<DialogShowListener> = mutableListOf()
    private val onHideListeners: MutableList<DialogHideListener> = mutableListOf()
    private val onCancelListeners: MutableList<DialogCancelListener> = mutableListOf()
    private var dialogCallbacks: DialogListeners? = null
    private var dialogLifecycleOwner: LifecycleOwner? = null
    protected lateinit var headerViewHierarchy: DialogLayoutHierarchyHeading
        private set
    protected lateinit var footerViewHierarchy: DialogLayoutHierarchyFooter
        private set
    lateinit var dialogType: DialogTypes
        private set
    lateinit var animationType: DialogAnimationTypes
        private set
    var isDialogShown = false
        private set
    var isFullscreen = false
        private set
    var title: String? = null
        set(value) {
            field = value
            configureHeading()
            measureDialogLayout()
        }
    var message: String? = null
        set(value) {
            field = value
            configureHeading()
            measureDialogLayout()
        }
    var positiveButtonConfig: DialogButtonConfiguration? = null
    var negativeButtonConfig: DialogButtonConfiguration? = null

    /**
     * Used to create layout for the dialog.
     *
     * @param layoutInflater - LayoutInflater to use for view creation.
     * @return layout for the dialog.
     */
    protected abstract fun getDialogLayout(layoutInflater: LayoutInflater): View

    /**
     * Used to configure content if necessary. Dialog layout is remeasured after this method.
     *
     * @param view - layout of the dialog.
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    protected abstract fun configureContent(
        view: View,
        savedInstanceState: Bundle?,
    )

    /**
     * Called to do initial creation of a fragment. This is called after onAttach(Activity)
     * and before onCreateView(LayoutInflater, ViewGroup, Bundle).
     *
     * @param dialogArguments - Arguments passed to the dialog fragment if any.
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    protected open fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {}

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     *
     * @param view - layout of the dialog
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    protected open fun doOnViewCreated(view: View?, savedInstanceState: Bundle?) {}

    /**
     * Called when all saved state has been restored into the view hierarchy of the dialog fragment.
     *
     * @param savedInstanceState
     */
    protected open fun doOnViewStateRestored(savedInstanceState: Bundle?) {}

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance if its process is
     * restarted.
     * @param outState - Bundle in which to place your saved state.
     */
    protected open fun doOnSaveInstanceState(outState: Bundle?) {}

    /**
     * Called upon dismiss of the dialog.
     */
    protected open fun doOnDismiss() {}

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

    protected open fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect,
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxWidth(), View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        //val params = ViewGroup.LayoutParams(0, 0)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var desiredWidth = dialogLayout.measuredWidth
        var desiredHeight = dialogLayout.measuredHeight
        desiredWidth = constraints.resolveWidth(desiredWidth)
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(desiredWidth + horPadding, desiredHeight + verPadding)
    }

    protected open fun setupFullScreenDialog(
        dialogWindow: Window,
        dialogLayout: View,
    ) {
    }

    protected open fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
    ) {
        val windowWidth = getWindowWidth()
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(windowWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        var desiredHeight = dialogLayout.measuredHeight
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight)
    }

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
        val hasSavedState = savedInstanceState != null
        if (hasSavedState) {
            title = helper.title
            message = helper.message
            positiveButtonConfig = helper.positiveButtonConfig
            negativeButtonConfig = helper.negativeButtonConfig
            cancelableOnClickOutside = helper.cancelable
            isDialogShown = helper.showing
            isFullscreen = helper.fullScreen
            dialogType = helper.dialogType
            animationType = helper.animationType
        } else {
            dialogType = helper.dialogType
            when (dialogType) {
                NORMAL -> {
                    isFullscreen = false
                    animationType = helper.animationType
                }
                FULLSCREEN -> {
                    isFullscreen = true
                    animationType = helper.animationType
                }
                BOTTOM_SHEET -> {
                    isFullscreen = false
                    animationType = TRANSITION_FROM_BOTTOM_TO_BOTTOM
                }
            }
            if (title == null) //if not set outside of builder | otherwise ignore
                title = helper.title
            if (message == null) //if not set outside of builder | otherwise ignore
                message = helper.message
            if (positiveButtonConfig == null) //if not set outside of builder | otherwise ignore
                positiveButtonConfig = helper.positiveButtonConfig
            if (negativeButtonConfig == null) //if not set outside of builder | otherwise ignore
                negativeButtonConfig = helper.negativeButtonConfig
            cancelableOnClickOutside = helper.cancelable
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
    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        regularDialogConstraints = getRegularConstraints()
        bottomSheetDialogConstraints = getBottomSheetConstraints()
        rootView = when (dialogType) {
            NORMAL, FULLSCREEN -> getDialogLayout(LayoutInflater.from(requireContext()))
            BOTTOM_SHEET -> getBottomSheetDialogLayout()
        }
        return rootView
    }

    @Override
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

    @Override
    final override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        doOnViewStateRestored(savedInstanceState)
    }

    @Override
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

    @Override
    final override fun onStart() {
        super<DialogFragment>.onStart()
        val dialogWindow = dialog?.window ?: return
        when (animationType) {
            NO_ANIMATION -> dialogWindow.setWindowAnimations(R.style.NoAnimation)
            FADE -> dialogWindow.setWindowAnimations(R.style.Animation_Fade)
            TRANSITION_FROM_BOTTOM_TO_BOTTOM -> dialogWindow.setWindowAnimations(R.style.Animation_FromBottomToBottom)
            TRANSITION_FROM_LEFT_TO_RIGHT -> dialogWindow.setWindowAnimations(R.style.Animation_FromLeftToRight)
            TRANSITION_FROM_TOP_TO_BOTTOM -> dialogWindow.setWindowAnimations(R.style.Animation_FromTopToBottom)
        }
    }

    @Override
    final override fun dismiss() {
        if (!isDialogShown) return
        super.dismiss()
        isDialogShown = false
    }

    @Override
    final override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        for (onHideListener in onHideListeners)
            onHideListener.doOnHide(this)
        doOnDismiss()
    }

    @Override
    final override fun onCancel(dialog: DialogInterface) {
        for (onCancelListener in onCancelListeners)
            onCancelListener.doOnCancel(this)
        dismiss()
    }

    @Override
    final override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (checkIfDialogCanBeShown())
            super.show(transaction, tag) else -1
    }

    @Override
    final override fun showNow(manager: FragmentManager, tag: String?) {
        if (checkIfDialogCanBeShown())
            super.showNow(manager, tag)
    }

    @Override
    final override fun show(manager: FragmentManager, tag: String?) {
        if (checkIfDialogCanBeShown())
            super.show(manager, tag)
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

    fun setParentFragManager(fragmentManager: FragmentManager) {
        this.parentFragManager = fragmentManager
    }

    fun setDialogTag(dialogTag: String) {
        this.dialogTag = dialogTag
    }

    fun setTitleAndMessage(title: String?, message: String?) {
        this.title = title
        this.message = message
        configureHeading()
        measureDialogLayout()
    }

    fun configurePositiveButton(configurationCreator: DialogButtonConfigurationCreator) {
        positiveButtonConfig = configurationCreator.create(positiveButtonConfig)
        configureButtons()
    }

    fun configureNegativeButton(configurationCreator: DialogButtonConfigurationCreator) {
        negativeButtonConfig = configurationCreator.create(negativeButtonConfig)
        configureButtons()
    }

    fun addOnPositiveClickListener(onPositiveClickListener: DialogButtonClickListener) {
        if (!onPositiveClickListeners.contains(onPositiveClickListener))
            onPositiveClickListeners.add(onPositiveClickListener)
    }

    fun addOnNegativeClickListeners(onNegativeClickListener: DialogButtonClickListener) {
        if (!onNegativeClickListeners.contains(onNegativeClickListener))
            onNegativeClickListeners.add(onNegativeClickListener)
    }

    fun addOnCancelListener(onCancelListener: DialogCancelListener) {
        if (!onCancelListeners.contains(onCancelListener))
            onCancelListeners.add(onCancelListener)
    }

    fun addOnShowListener(onShowListener: DialogShowListener) {
        if (!onShowListeners.contains(onShowListener))
            onShowListeners.add(onShowListener)
    }

    fun addOnHideListener(hideListener: DialogHideListener) {
        if (!onHideListeners.contains(hideListener))
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

    /**
     * Returns the maximum possible width according to the bounds given to the dialog.
     *
     * @return width in pixels
     */
    protected fun getMaxWidth(): Int {
        return when (dialogType) {
            NORMAL -> regularDialogConstraints.getMaxWidth()
            FULLSCREEN, BOTTOM_SHEET -> getWindowWidth()
        }
    }

    /**
     * Returns the maximum possible height according to the bounds given to the dialog.
     *
     * @return height in pixels
     */
    protected fun getMaxHeight(): Int {
        return when (dialogType) {
            NORMAL -> regularDialogConstraints.getMaxHeight()
            FULLSCREEN -> getWindowHeight()
            BOTTOM_SHEET -> bottomSheetDialogConstraints.getMaxHeight()
        }
    }

    protected fun getWindowWidth(): Int {
        val activity = requireActivity()
        return WindowUtils.getWindowWidth(activity)
    }

    protected fun getWindowHeight(): Int {
        val activity = requireActivity()
        return WindowUtils.getWindowHeight(activity)
    }

    protected fun getPercentOfWindowHeight(heightInPercents: Int): Int {
        return getWindowHeight() * validatePercents(heightInPercents) / 100
    }

    protected fun getPercentOfWindowWidth(widthInPercents: Int): Int {
        return getWindowWidth() * validatePercents(widthInPercents) / 100
    }

    protected fun measureDialogLayout() {
        val dialog = dialog
        if (dialog == null || dialog.window == null) return
        val window = dialog.window
        val fgPadding = Rect()
        window!!.decorView.background.getPadding(fgPadding)
        when (dialogType) {
            FULLSCREEN -> {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // overrides background to remove insets
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                setupFullScreenDialog(window, rootView)
            }
            NORMAL -> setupRegularDialog(
                regularDialogConstraints,
                window,
                rootView,
                fgPadding
            )
            BOTTOM_SHEET -> {
                window.setGravity(Gravity.BOTTOM)
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // overrides background to remove insets
                setupBottomSheetDialog(
                    bottomSheetDialogConstraints,
                    window,
                    rootView
                )
            }
        }
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
        val titleAndMessageContainer = dialogView.findViewById<LinearLayoutCompat?>(R.id.dialogTitleAndMessageContainer)
        val titleTextView = dialogView.findViewById<TextView?>(R.id.dialogTitleTextView)
        val messageTextView = dialogView.findViewById<TextView?>(R.id.dialogMessageTextView)
        headerViewHierarchy = DialogLayoutHierarchyHeading(titleAndMessageContainer, titleTextView, messageTextView)
        if (titleAndMessageContainer == null) return
        if (titleTextView == null && messageTextView == null || title.isNullOrBlank() && message.isNullOrBlank())
            titleAndMessageContainer.visibility = GONE
        else
            titleAndMessageContainer.visibility = View.VISIBLE
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
        val buttonsContainer = dialogView.findViewById<LinearLayoutCompat?>(R.id.dialogButtonsContainer)
        val buttonPositive = dialogView.findViewById<Button?>(R.id.dialogPositiveButton)
        val buttonNegative = dialogView.findViewById<Button?>(R.id.dialogNegativeButton)
        footerViewHierarchy = DialogLayoutHierarchyFooter(buttonsContainer, buttonPositive, buttonNegative)
        if (buttonsContainer == null)
            return
        if (buttonPositive == null && buttonNegative == null || positiveButtonConfig == null && negativeButtonConfig == null)
            buttonsContainer.visibility = GONE
        if (buttonPositive != null && positiveButtonConfig == null)
            buttonPositive.visibility = GONE
        if (buttonNegative != null && negativeButtonConfig == null)
            buttonNegative.visibility = GONE
        if (buttonPositive != null && positiveButtonConfig != null) {
            buttonPositive.isEnabled = positiveButtonConfig!!.buttonEnabled
            buttonPositive.setTextColor(buttonPositive.textColors.withAlpha(if (positiveButtonConfig!!.buttonEnabled) 255 else 140))
            if (positiveButtonConfig!!.buttonTitle.isBlank()) buttonPositive.visibility = GONE
            else {
                buttonPositive.text = positiveButtonConfig!!.buttonTitle
                buttonPositive.setOnClickListener { view: View? ->
                    for (listener in onPositiveClickListeners)
                        listener.doOnClick(view, this)
                    if (positiveButtonConfig!!.closeDialogOnClick)
                        handleDismiss()
                }
            }
        }
        if (buttonNegative != null && negativeButtonConfig != null) {
            buttonNegative.isEnabled = negativeButtonConfig!!.buttonEnabled
            buttonNegative.setTextColor(buttonNegative.textColors.withAlpha(if (negativeButtonConfig!!.buttonEnabled) 255 else 140))
            if (negativeButtonConfig!!.buttonTitle.isBlank()) buttonNegative.visibility = GONE
            else {
                buttonNegative.text = negativeButtonConfig!!.buttonTitle
                buttonNegative.setOnClickListener { view: View? ->
                    for (listener in onNegativeClickListeners)
                        listener.doOnClick(view, this)
                    if (negativeButtonConfig!!.closeDialogOnClick)
                        handleDismiss()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getBottomSheetDialogLayout(): View {
        val context = requireContext()
        if (!cancelableOnClickOutside) return getDialogLayout(LayoutInflater.from(context))
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
        val child = getDialogLayout(LayoutInflater.from(context))
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

    private fun canShowDialog(dialogLifecycleOwner: LifecycleOwner?): Boolean {
        if (dialogLifecycleOwner == null)
            Log.w(
                javaClass.name,
                "You are using dialog without lifecycle owner. This may produce unexpected behaviour when trying to " +
                        "show the dialog in specific lifecycle states. It is highly recommended to build your dialog with a " +
                        "lifecycle ownner."
            )
        if (dialogLifecycleOwner != null && !isLifecycleOwnerInStateAllowingShow) {
            Log.w(
                javaClass.name,
                "Showing of dialog cancelled: Lifecycle owner is not in valid state. You are probably trying to " +
                        "show the dialog after saveInstanceState has been executed."
            )
            return false
        }
        return true
    }
}