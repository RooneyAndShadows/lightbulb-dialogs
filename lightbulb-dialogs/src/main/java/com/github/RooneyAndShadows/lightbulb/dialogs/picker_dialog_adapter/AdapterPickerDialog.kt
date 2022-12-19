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
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.callbacks.EasyAdapterSelectionChangedListener
import java.util.ArrayList

open class AdapterPickerDialog<ItemType : EasyAdapterDataModel?> :
    BasePickerDialogFragment<IntArray?>(AdapterPickerDialogSelection(null, null)) {
    protected var recyclerView: RecyclerView? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    protected var adapter: EasyRecyclerAdapter<ItemType>? = null
    private val selectionListener: EasyAdapterSelectionChangedListener
    override fun setDialogLayout(inflater: LayoutInflater?): View {
        return View.inflate(context, R.layout.dialog_picker_normal, null)
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {
        recyclerView = view!!.findViewById(R.id.dialogRecycler)
        recyclerView.setItemAnimator(null)
        adapter!!.addOrReplaceSelectionChangedListener(selectionListener)
        configureRecyclerView(recyclerView)
        recyclerView.setAdapter(adapter)
    }

    protected override val regularConstraints: RegularDialogConstraints?
        protected get() {
            val orientation = resources.configuration.orientation
            return RegularDialogConstraintsBuilder(this)
                .default()
                .withMinWidth(getPercentOfWindowWidth(if (orientation == Configuration.ORIENTATION_PORTRAIT) 70 else 60))
                .withMaxHeight(getPercentOfWindowHeight(if (orientation == Configuration.ORIENTATION_PORTRAIT) 70 else 85))
                .build()
        }

    override fun synchronizeSelectUi() {
        val newSelection = if (selection.hasDraftSelection()) selection.draftSelection else selection.currentSelection
        if (adapter != null) adapter!!.selectPositions(newSelection, true, false)
    }

    protected open fun configureRecyclerView(recyclerView: RecyclerView?) {
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        if (recyclerView.itemDecorationCount > 0) recyclerView.removeItemDecorationAt(0)
        if (itemDecoration != null) recyclerView.addItemDecoration(itemDecoration!!, 0)
    }

    override fun setupRegularDialog(
        constraints: RegularDialogConstraints?,
        dialogWindow: Window?,
        dialogLayout: View?,
        fgPadding: Rect
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints!!.maxWidth, View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.maxHeight, View.MeasureSpec.AT_MOST)
        dialogLayout!!.measure(widthMeasureSpec, heightMeasureSpec)
        val desiredHeightForRecycler = recyclerView!!.measuredHeight
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var desiredWidth = dialogLayout.measuredWidth
        var desiredHeight = dialogLayout.measuredHeight
        if (desiredHeight > constraints.maxHeight) {
            desiredHeight -= desiredHeightForRecycler
            val recyclerExactHeight = constraints.maxHeight - desiredHeight
            recyclerView!!.layoutParams.height = recyclerExactHeight
            desiredHeight += recyclerExactHeight
        }
        desiredWidth = constraints.resolveWidth(desiredWidth)
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow!!.setLayout(desiredWidth + horPadding, desiredHeight + verPadding)
    }

    override fun setupFullScreenDialog(dialogWindow: Window?, dialogLayout: View?) {
        val parameters = recyclerView!!.layoutParams as LinearLayoutCompat.LayoutParams
        parameters.weight = 1f
        val maxHeight = windowHeight
        var desiredHeight = dialogLayout!!.measuredHeight
        val desiredHeightForRecycler = recyclerView!!.measuredHeight
        if (desiredHeight > maxHeight) {
            desiredHeight -= desiredHeightForRecycler
            recyclerView!!.layoutParams.height = maxHeight - desiredHeight
        }
    }

    override fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints?,
        dialogWindow: Window?,
        dialogLayout: View?
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(windowWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout!!.measure(widthMeasureSpec, heightMeasureSpec)
        var desiredHeight = dialogLayout.measuredHeight
        val desiredHeightForRecycler = recyclerView!!.measuredHeight
        if (desiredHeight > constraints!!.maxHeight) {
            desiredHeight -= desiredHeightForRecycler
            val recyclerExactHeight = constraints.maxHeight - desiredHeight
            recyclerView!!.layoutParams.height = recyclerExactHeight
            desiredHeight += recyclerExactHeight
        }
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight)
    }

    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireNotNull(dialogArguments) { "Bundle args required" }
            if (hasSelection()) selection.setCurrentSelection(selection.currentSelection) else selection.setCurrentSelection(
                dialogArguments.getIntArray(
                    ADAPTER_SELECTION_TAG
                )
            )
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(ADAPTER_SELECTION_TAG))
            selection.setDraftSelection(savedInstanceState.getIntArray(ADAPTER_SELECTION_DRAFT_TAG))
        }
    }

    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        if (selection.currentSelection != null) outState!!.putIntArray(ADAPTER_SELECTION_TAG, selection.currentSelection)
        if (selection.draftSelection != null) outState!!.putIntArray(ADAPTER_SELECTION_DRAFT_TAG, selection.draftSelection)
    }

    fun setSelection(newSelection: Int) {
        selection.currentSelection = intArrayOf(newSelection)
    }

    fun setAdapter(adapter: EasyRecyclerAdapter<ItemType>?) {
        this.adapter = adapter
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
        this.itemDecoration = itemDecoration
    }

    fun setData(data: ArrayList<ItemType>?) {
        adapter!!.setCollection(data!!)
    }

    companion object {
        private const val ADAPTER_SELECTION_TAG = "ADAPTER_SELECTION_TAG"
        private const val ADAPTER_SELECTION_DRAFT_TAG = "ADAPTER_SELECTION_DRAFT_TAG"
        fun <ItemType : EasyAdapterDataModel?> newInstance(
            title: String?, message: String?, positive: DialogButtonConfiguration?, negative: DialogButtonConfiguration?,
            cancelable: Boolean, dialogType: DialogTypes?, animationType: DialogAnimationTypes?
        ): AdapterPickerDialog<ItemType> {
            val dialogFragment = AdapterPickerDialog<ItemType>()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(dialogType ?: DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
                .bundle
            return dialogFragment
        }
    }

    init {
        selectionListener = EasyAdapterSelectionChangedListener { newSelection: IntArray? ->
            if (isDialogShown) selection.setDraftSelection(
                newSelection,
                false
            ) else selection.setCurrentSelection(newSelection, false)
        }
    }
}