package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
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
import com.github.rooneyandshadows.lightbulb.dialogs.base.view.DialogFooterView
import com.github.rooneyandshadows.lightbulb.dialogs.base.view.DialogHeaderView
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseDialogFragment : DialogFragment(), DefaultLifecycleObserver {
    private lateinit var parentFragManager: FragmentManager
    private lateinit var regularDialogConstraints: RegularDialogConstraints
    private lateinit var bottomSheetDialogConstraints: BottomSheetDialogConstraints
    private lateinit var rootView: View
    private var dialogTag: String = javaClass.name.plus("_TAG")
    private var isLifecycleOwnerInStateAllowingShow = false
    private val dialogButtons: MutableList<DialogButton> = mutableListOf()
    private val onShowListeners: MutableList<DialogShowListener> = mutableListOf()
    private val onHideListeners: MutableList<DialogHideListener> = mutableListOf()
    private val onCancelListeners: MutableList<DialogCancelListener> = mutableListOf()
    private var dialogCallbacks: DialogListeners? = null
    private var dialogLifecycleOwner: LifecycleOwner? = null
    private var bottomSheetBehavior: LockableBottomSheetBehavior<View>? = null
    protected var dialogHeaderView: DialogHeaderView? = null
        private set
    protected var dialogFooterView: DialogFooterView? = null
        private set
    protected var isAttached = false
    var isDialogShown = false
        private set
    open var dialogType: DialogTypes = NORMAL
        set(value) {
            field = value
            if (value == BOTTOM_SHEET && dialogAnimationType != TRANSITION_FROM_BOTTOM_TO_BOTTOM)
                dialogAnimationType = TRANSITION_FROM_BOTTOM_TO_BOTTOM
        }
    open var dialogAnimationType: DialogAnimationTypes = NO_ANIMATION
        set(value) {
            field = if (dialogType == BOTTOM_SHEET && value != TRANSITION_FROM_BOTTOM_TO_BOTTOM)
                TRANSITION_FROM_BOTTOM_TO_BOTTOM
            else value
        }
    var dialogTitle: String? = null
        private set
    var dialogMessage: String? = null
        private set


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
    protected abstract fun setupDialogContent(
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
     * Called when fragment has been attached to context.
     *
     * @param context - target context.
     */
    protected open fun doOnAttach(context: Context) {}

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
    protected open fun doOnSaveInstanceState(outState: Bundle) {}

    /**
     * Called to ask the fragment to restore its state. Called in onViewCreated.
     *
     * @param savedState - Bundle containing previously saved values.
     */
    protected open fun doOnRestoreInstanceState(savedState: Bundle) {}

    /**
     * Called when dialog saved dialog non view dependent properties.
     *
     * @param outState - Bundle in which to place your saved state.
     */
    protected open fun doOnSaveDialogProperties(outState: Bundle) {}

    /**
     * Called to ask the fragment to restore its state. Called in onCreate
     *
     * @param savedState - Bundle containing previously saved values.
     */
    protected open fun doOnRestoreDialogProperties(savedState: Bundle) {}

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
    final override fun onAttach(context: Context) {
        super.onAttach(context)
        isAttached = true
        doOnAttach(context)
    }

    @Override
    final override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        isLifecycleOwnerInStateAllowingShow = true
    }

    @Override
    final override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        isLifecycleOwnerInStateAllowingShow = false
    }

    @Override
    final override fun onCreate(savedInstanceState: Bundle?) {
        super<DialogFragment>.onCreate(savedInstanceState)
        restoreDialogProperties(savedInstanceState)
        doOnCreate(arguments, savedInstanceState)
        if (dialogCallbacks != null)
            dialogCallbacks!!.doOnCreate(this, arguments, savedInstanceState)
    }

    private fun saveDialogProperties(outState: Bundle) {
        DialogBundleHelper(outState).apply {
            withTitle(dialogTitle)
            withMessage(dialogMessage)
            withCancelable(isCancelable)
            withShowing(isDialogShown)
            withDialogType(dialogType)
            withAnimation(dialogAnimationType)
            withButtonConfigurations(dialogButtons)
        }.bundle.apply {
            doOnSaveDialogProperties(this)
        }
    }

    private fun restoreDialogProperties(savedState: Bundle?) {
        savedState?.apply {
            val helper = DialogBundleHelper(this)
            dialogTitle = helper.title
            dialogMessage = helper.message
            isCancelable = helper.cancelable
            isDialogShown = helper.showing
            dialogType = helper.type
            dialogAnimationType = helper.animationType
            helper.buttonConfigurations?.forEach { savedButtonConfig ->
                getButtonConfiguration(savedButtonConfig.buttonTag)?.apply {
                    buttonTitle = savedButtonConfig.buttonTitle
                    buttonEnabled = savedButtonConfig.buttonEnabled
                    closeDialogOnClick = savedButtonConfig.closeDialogOnClick
                }
            }
            configureButtons()
            doOnRestoreDialogProperties(savedState)
        }
    }

    fun onRestoreInstanceState(savedState: Bundle?) {
        savedState?.apply {
            doOnRestoreInstanceState(savedState)
        }
    }

    @Override
    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveDialogProperties(outState)
        doOnSaveInstanceState(outState)
        if (dialogCallbacks != null)
            dialogCallbacks!!.doOnSaveInstanceState(this@BaseDialogFragment, view, outState)
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
        setupDialogContent(rootView, savedInstanceState)
        onRestoreInstanceState(savedInstanceState)
        configureHeading()
        configureButtons()
        measureDialogLayout()
        //doOnViewCreated(view, savedInstanceState)
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
        when (dialogAnimationType) {
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

    protected fun enableBottomSheetDrag(newState: Boolean) {
        bottomSheetBehavior?.swipeEnabled = newState
    }

    fun setDialogTitle(title: String?) {
        dialogTitle = title
        configureHeading()
        measureDialogLayout()
    }

    fun setDialogMessage(message: String?) {
        dialogMessage = message
        configureHeading()
        measureDialogLayout()
    }

    fun addButton(buttonConfig: DialogButton) {
        addDialogButtonInternally(buttonConfig, true)
    }

    fun removeButton(index: Int) {
        removeDialogButtonInternally(index, true)
    }

    fun removeButton(buttonConfig: DialogButton) {
        removeDialogButtonInternally(buttonConfig, true)
    }

    fun removeButton(buttonTag: String) {
        removeDialogButtonInternally(buttonTag, true)
    }

    fun getButtons(): List<DialogButton> {
        return dialogButtons.toList()
    }

    fun getButtonConfiguration(buttonTag: String): DialogButton? {
        return dialogButtons.singleOrNull { it.buttonTag == buttonTag }
    }

    fun saveDialogState(): Bundle {
        return Bundle().apply {
            if (isDialogShown) putBoolean(BaseDialogBuilder.IGNORE_MANUALLY_SAVED_STATE, true)
            else saveDialogProperties(this)
        }
    }

    fun restoreDialogState(savedState: Bundle?) {
        savedState?.apply {
            val ignoreManualRestore = getBoolean(BaseDialogBuilder.IGNORE_MANUALLY_SAVED_STATE, false)
            if (ignoreManualRestore) return@apply
            restoreDialogProperties(this)
        }
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
        this.dialogTitle = title
        this.dialogMessage = message
        configureHeading()
        measureDialogLayout()
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
        if (!this::rootView.isInitialized) return
        val dialog = dialog ?: return
        val window = dialog.window ?: return
        val dialogView = rootView
        val fgPadding = Rect()
        window.decorView.background.getPadding(fgPadding)
        when (dialogType) {
            FULLSCREEN -> {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // overrides background to remove insets
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                setupFullScreenDialog(window, dialogView)
            }

            NORMAL -> setupRegularDialog(regularDialogConstraints, window, dialogView, fgPadding)
            BOTTOM_SHEET -> {
                window.setGravity(Gravity.BOTTOM)
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // overrides background to remove insets
                setupBottomSheetDialog(bottomSheetDialogConstraints, window, dialogView)
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

    private fun configureHeading() {
        if (!this::rootView.isInitialized) return
        val dialogView = rootView
        dialogHeaderView = dialogView.findViewById<DialogHeaderView>(R.id.dialogTitleAndMessageContainer)?.apply {
            title = dialogTitle
            message = dialogMessage
        }
    }

    private fun configureButtons() {
        if (!this::rootView.isInitialized) return
        val dialogView = rootView
        dialogFooterView = dialogView.findViewById<DialogFooterView>(R.id.dialogButtonsContainer)?.apply {
            initialize(this@BaseDialogFragment)
        }
    }

    @Suppress("SameParameterValue")
    private fun addDialogButtonInternally(buttonConfig: DialogButton, syncButtons: Boolean) {
        if (dialogButtons.contains(buttonConfig)) return
        if (dialogButtons.any { it.buttonTag == buttonConfig.buttonTag }) return
        dialogButtons.add(buttonConfig)
        if (!syncButtons) return
        configureButtons()
    }

    @Suppress("SameParameterValue")
    private fun removeDialogButtonInternally(index: Int, syncButtons: Boolean) {
        if (dialogButtons.size <= index) return
        dialogButtons.removeAt(index)
        if (!syncButtons) return
        configureButtons()
    }

    @Suppress("SameParameterValue")
    private fun removeDialogButtonInternally(buttonTag: String, syncButtons: Boolean) {
        val buttons = dialogButtons
        if (!buttons.removeIf { it.buttonTag == buttonTag }) return
        if (!syncButtons) return
        configureButtons()
    }

    @Suppress("SameParameterValue")
    private fun removeDialogButtonInternally(buttonConfig: DialogButton, syncButtons: Boolean) {
        val buttons = dialogButtons
        if (!buttons.remove(buttonConfig)) return
        if (!syncButtons) return
        configureButtons()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getBottomSheetDialogLayout(): View {
        val context = requireContext()
        if (!isCancelable || this@BaseDialogFragment is AdapterPickerDialog<*>)
            return getDialogLayout(LayoutInflater.from(context))
        val parent = CoordinatorLayout(context)
        val params: CoordinatorLayout.LayoutParams = CoordinatorLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        )
        val child = getDialogLayout(LayoutInflater.from(context))
        LockableBottomSheetBehavior<View>().apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isFitToContents = true
            val handlingFling = booleanArrayOf(false)
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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
                    state = if (bottomSheet.top > threshold) BottomSheetBehavior.STATE_COLLAPSED
                    else BottomSheetBehavior.STATE_EXPANDED
                }

                @Override
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            val gesture = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                    if (velocityY > 2000) {
                        handlingFling[0] = true
                        setState(BottomSheetBehavior.STATE_COLLAPSED)
                    } else setState(BottomSheetBehavior.STATE_EXPANDED)
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
            parent.addView(child)
            parent.setOnTouchListener { _: View?, event: MotionEvent? ->
                try {
                    gesture.onTouchEvent(event!!)
                } catch (e: Exception) {
                    return@setOnTouchListener true
                }
            }
            params.behavior = bottomSheetBehavior
        }
        child.layoutParams = params
        return parent
    }

    private fun canShowDialog(dialogLifecycleOwner: LifecycleOwner?): Boolean {
        if (dialogLifecycleOwner == null)
            Log.w(
                javaClass.name,
                "You are using dialog without lifecycle owner. This may produce unexpected behaviour when trying to " +
                        "show the dialog in specific lifecycle states. It is highly recommended to build your dialog with a " +
                        "lifecycle owner."
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