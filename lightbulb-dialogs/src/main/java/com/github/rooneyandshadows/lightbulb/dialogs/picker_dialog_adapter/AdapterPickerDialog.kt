package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.callbacks.EasyAdapterSelectionChangedListener

@Suppress("unused")
abstract class AdapterPickerDialog<ItemType : EasyAdapterDataModel> :
    BasePickerDialogFragment<IntArray?>(AdapterPickerDialogSelection(null, null)) {
    private val adapterStateTag = "ADAPTER_STATE_TAG"
    private val adapterSelectionTag = "ADAPTER_SELECTION_TAG"
    private val adapterSelectionDraftTag = "ADAPTER_SELECTION_DRAFT_TAG"
    protected var recyclerView: RecyclerView? = null
        private set
    protected val adapter: EasyRecyclerAdapter<ItemType> by lazy {
        return@lazy adapterCreator.createAdapter()
    }
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private val selectionListener: EasyAdapterSelectionChangedListener
    protected abstract val adapterCreator: AdapterCreator<ItemType>

    companion object {
        fun <ItemType : EasyAdapterDataModel> newInstance(adapterCreator: AdapterCreator<ItemType>): AdapterPickerDialog<ItemType> {
            return object : AdapterPickerDialog<ItemType>() {
                override val adapterCreator: AdapterCreator<ItemType>
                    get() = adapterCreator
            }
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
    override fun configureContent(view: View, savedInstanceState: Bundle?) {
        selectViews(view)
        configureRecyclerView(adapter)
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
        val newSelection = if (selection.hasDraftSelection()) selection.getDraftSelection()
        else selection.getCurrentSelection()
        adapter.selectPositions(newSelection, newState = true, incremental = false)
    }

    @Override
    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect,
    ) {
        val recyclerView = this.recyclerView!!
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
        val recyclerView = this.recyclerView!!
        val parameters = recyclerView.layoutParams as LinearLayoutCompat.LayoutParams
        parameters.weight = 1f
        val maxHeight = getWindowHeight()
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
        dialogLayout: View,
    ) {
        val recyclerView = this.recyclerView!!
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWindowWidth(), View.MeasureSpec.EXACTLY)
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
    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        outState?.apply {
            putParcelable(adapterStateTag, adapter.saveAdapterState())
            selection.getCurrentSelection()?.apply {
                putIntArray(adapterSelectionTag, this)
            }
            selection.getDraftSelection()?.apply {
                putIntArray(adapterSelectionDraftTag, this)
            }
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        savedState.apply {
            val adapterState = BundleUtils.getParcelable(adapterStateTag, this, Bundle::class.java)!!
            val currentSelection = getIntArray(adapterSelectionTag)
            val draftSelection = getIntArray(adapterSelectionDraftTag)
            selection.setCurrentSelection(currentSelection)
            selection.setDraftSelection(draftSelection)
            adapter.restoreAdapterState(adapterState)
        }
    }

    fun setSelection(newSelection: Int) {
        setSelection(intArrayOf(newSelection))
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
        this.itemDecoration = itemDecoration
    }

    fun setData(data: List<ItemType>?) {
        adapter.setCollection(data ?: mutableListOf())
    }

    private fun selectViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.dialogRecycler)
    }

    private fun configureRecyclerView(adapter: EasyRecyclerAdapter<ItemType>) {
        val recyclerView = this.recyclerView!!
        adapter.addOrReplaceSelectionChangedListener(selectionListener)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(context)
        if (recyclerView.itemDecorationCount > 0) recyclerView.removeItemDecorationAt(0)
        if (itemDecoration != null) recyclerView.addItemDecoration(itemDecoration!!, 0)
        recyclerView.adapter = adapter
    }

    interface AdapterCreator<ItemType : EasyAdapterDataModel> {
        fun createAdapter(): EasyRecyclerAdapter<ItemType>
    }
}