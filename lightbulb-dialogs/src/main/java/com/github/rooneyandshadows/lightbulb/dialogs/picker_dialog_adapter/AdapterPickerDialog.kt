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
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.callbacks.EasyAdapterSelectionChangedListener
import java.util.Arrays

@Suppress("unused")
abstract class AdapterPickerDialog<ItemType : EasyAdapterDataModel> : BasePickerDialogFragment<IntArray>(
    AdapterPickerDialogSelection(null, null)
) {
    protected var recyclerView: RecyclerView? = null
        private set
    var itemDecoration: ItemDecoration? = null
        private set
    val adapter: EasyRecyclerAdapter<ItemType> by lazy {
        return@lazy adapterCreator.createAdapter()
    }
    protected abstract val adapterCreator: AdapterCreator<ItemType>

    companion object {
        private const val ADAPTER_STATE_TAG = "ADAPTER_STATE_TAG"
        fun <ItemType : EasyAdapterDataModel> newInstance(adapterCreator: AdapterCreator<ItemType>): AdapterPickerDialog<ItemType> {
            return object : AdapterPickerDialog<ItemType>() {
                override val adapterCreator: AdapterCreator<ItemType>
                    get() = adapterCreator
            }
        }
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_picker_normal, null)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.dialogRecycler)
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
    override fun onSelectionChange(newSelection: IntArray?) {
        val currentAdapterSelection = adapter.selectedPositionsAsArray
        val needAdapterSync = !Arrays.equals(newSelection, currentAdapterSelection)
        if (needAdapterSync) adapter.selectPositions(newSelection, newState = true, incremental = false)
    }

    @Override
    override fun doOnViewStateRestored(savedInstanceState: Bundle?) {
        super.doOnViewStateRestored(savedInstanceState)
        val selectionListener = object : EasyAdapterSelectionChangedListener {
            override fun onChanged(newSelection: IntArray?) {
                if (isDialogShown) selection.setDraftSelection(newSelection)
                else selection.setCurrentSelection(newSelection)
            }
        }
        adapter.addOrReplaceSelectionChangedListener(selectionListener)
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
    override fun doOnSaveDialogProperties(outState: Bundle) {
        super.doOnSaveDialogProperties(outState)
        outState.apply {
            putParcelable(ADAPTER_STATE_TAG, adapter.saveAdapterState())
        }
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        savedState.apply {
            val adapterState = BundleUtils.getParcelable(ADAPTER_STATE_TAG, this, Bundle::class.java)!!
            adapter.restoreAdapterState(adapterState)
        }
    }

    @Override
    override fun setSelection(newSelection: IntArray?) {
        super.setSelection(newSelection)
        adapter.selectPositions(positions = newSelection, newState = true, incremental = false)
    }

    fun selectItem(item: ItemType?) {
        if (item == null) return
        val position = adapter.getPosition(item)
        if (position != -1) setSelection(intArrayOf(position))
    }

    fun setSelection(newSelection: Int) {
        setSelection(intArrayOf(newSelection))
    }

    fun setItemDecoration(itemDecoration: ItemDecoration?) {
        this.itemDecoration = itemDecoration
        recyclerView?.apply {
            (0 until itemDecorationCount).forEach {
                removeItemDecorationAt(0)
            }
            if (itemDecoration != null) addItemDecoration(itemDecoration)
        }
    }

    fun setData(data: List<ItemType>?) {
        adapter.setCollection(data ?: mutableListOf())
    }

    private fun clearItemDecorations() {
        recyclerView?.apply {
            (0 until itemDecorationCount).forEach {
                removeItemDecorationAt(0)
            }
        }
    }

    private fun configureRecyclerView(adapter: EasyRecyclerAdapter<ItemType>) {
        val recyclerView = this.recyclerView!!
        recyclerView.isVerticalScrollBarEnabled = true
        recyclerView.isScrollbarFadingEnabled = false
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