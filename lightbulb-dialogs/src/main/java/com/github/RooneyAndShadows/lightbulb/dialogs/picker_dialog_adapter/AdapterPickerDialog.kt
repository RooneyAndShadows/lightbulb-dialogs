package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import androidx.appcompat.widget.LinearLayoutCompat
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.callbacks.EasyAdapterSelectionChangedListener

open class AdapterPickerDialog<ItemType : EasyAdapterDataModel> :
    BasePickerDialogFragment<IntArray?>(AdapterPickerDialogSelection(null, null)) {
    private val adapterSelectionTag = "ADAPTER_SELECTION_TAG"
    private val adapterSelectionDraftTag = "ADAPTER_SELECTION_DRAFT_TAG"
    private lateinit var recyclerView: RecyclerView
    private var adapter: EasyRecyclerAdapter<ItemType>? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private val selectionListener: EasyAdapterSelectionChangedListener

    companion object {
        fun <ItemType : EasyAdapterDataModel> newInstance(
            title: String?,
            message: String?,
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            cancelable: Boolean = true,
            dialogType: DialogTypes = DialogTypes.NORMAL,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION
        ): AdapterPickerDialog<ItemType> {
            val dialogFragment = AdapterPickerDialog<ItemType>()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(dialogType)
                .withAnimation(animationType)
                .bundle
            return dialogFragment
        }
    }

    init {
        selectionListener = object : EasyAdapterSelectionChangedListener {
            override fun onChanged(newSelection: IntArray?) {
                if (isDialogShown) selection.setDraftSelection(
                    newSelection,
                    false
                ) else selection.setCurrentSelection(newSelection, false)
            }
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_picker_normal, null)
    }

    @Override
    final override fun configureContent(view: View, savedInstanceState: Bundle?) {
        requireAdapter { adapter ->
            selectViews(view)
            configureRecyclerView(adapter)
            configureContent(view, recyclerView, savedInstanceState)
        }
    }

    protected open fun configureContent(
        view: View,
        recyclerView: RecyclerView,
        savedInstanceState: Bundle?
    ) {
    }

    @Override
    override fun getRegularConstraints(): RegularDialogConstraints {
        val orientation = resources.configuration.orientation
        return RegularDialogConstraintsBuilder(this)
            .default()
            .withMinWidth(getPercentOfWindowWidth(if (orientation == Configuration.ORIENTATION_PORTRAIT) 70 else 60))
            .withMaxHeight(getPercentOfWindowHeight(if (orientation == Configuration.ORIENTATION_PORTRAIT) 70 else 85))
            .build()
    }

    @Override
    override fun synchronizeSelectUi() {
        requireAdapter { adapter ->
            val newSelection = if (selection.hasDraftSelection()) selection.getDraftSelection()
            else selection.getCurrentSelection()
            adapter.selectPositions(newSelection, newState = true, incremental = false)
        }
    }

    @Override
    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxWidth(), View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxHeight(), View.MeasureSpec.AT_MOST)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val desiredHeightForRecycler = recyclerView.measuredHeight
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var desiredWidth = dialogLayout.measuredWidth
        var desiredHeight = dialogLayout.measuredHeight
        if (desiredHeight > constraints.getMaxHeight()) {
            desiredHeight -= desiredHeightForRecycler
            val recyclerExactHeight = constraints.getMaxHeight() - desiredHeight
            recyclerView.layoutParams.height = recyclerExactHeight
            desiredHeight += recyclerExactHeight
        }
        desiredWidth = constraints.resolveWidth(desiredWidth)
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(desiredWidth + horPadding, desiredHeight + verPadding)
    }

    @Override
    override fun setupFullScreenDialog(dialogWindow: Window, dialogLayout: View) {
        val parameters = recyclerView.layoutParams as LinearLayoutCompat.LayoutParams
        parameters.weight = 1f
        val maxHeight = windowHeight
        var desiredHeight = dialogLayout.measuredHeight
        val desiredHeightForRecycler = recyclerView.measuredHeight
        if (desiredHeight > maxHeight) {
            desiredHeight -= desiredHeightForRecycler
            recyclerView.layoutParams.height = maxHeight - desiredHeight
        }
    }

    @Override
    override fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(windowWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        var desiredHeight = dialogLayout.measuredHeight
        val desiredHeightForRecycler = recyclerView.measuredHeight
        if (desiredHeight > constraints.getMaxHeight()) {
            desiredHeight -= desiredHeightForRecycler
            val recyclerExactHeight = constraints.getMaxHeight() - desiredHeight
            recyclerView.layoutParams.height = recyclerExactHeight
            desiredHeight += recyclerExactHeight
        }
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight)
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            selection.setCurrentSelection(savedInstanceState.getIntArray(adapterSelectionTag))
            selection.setDraftSelection(savedInstanceState.getIntArray(adapterSelectionDraftTag))
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        if (selection.getCurrentSelection() != null) outState!!.putIntArray(
            adapterSelectionTag,
            selection.getCurrentSelection()
        )
        if (selection.getDraftSelection() != null) outState!!.putIntArray(
            adapterSelectionDraftTag,
            selection.getDraftSelection()
        )
    }

    fun setAdapter(adapter: EasyRecyclerAdapter<ItemType>) {
        this.adapter = adapter
    }

    fun setSelection(newSelection: Int) {
        setSelection(intArrayOf(newSelection))
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
        this.itemDecoration = itemDecoration
    }

    fun setData(data: MutableList<ItemType>?) {
        requireAdapter { adapter ->
            adapter.setCollection(data ?: mutableListOf())
        }
    }

    private fun selectViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.dialogRecycler)
    }

    private fun configureRecyclerView(adapter: EasyRecyclerAdapter<ItemType>) {
        adapter.addOrReplaceSelectionChangedListener(selectionListener)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(context)
        if (recyclerView.itemDecorationCount > 0) recyclerView.removeItemDecorationAt(0)
        if (itemDecoration != null) recyclerView.addItemDecoration(itemDecoration!!, 0)
        recyclerView.adapter = adapter
    }

    protected fun requireAdapter(run: (adapter: EasyRecyclerAdapter<ItemType>) -> Unit) {
        if (adapter == null) throw Exception("Dialog picker adapter must not be null.")
        run.invoke(adapter!!)
    }
}