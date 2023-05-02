package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.MotionEvent.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.DialogRecyclerView
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.data.EasyAdapterDataModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectionChangeListener
import java.util.*


@Suppress("unused")
@JvmSuppressWildcards
abstract class AdapterPickerDialog<ItemType : EasyAdapterDataModel>
    : BasePickerDialogFragment<IntArray>(AdapterPickerDialogSelection(null, null)) {
    private val internalAdapter: DialogPickerAdapter<ItemType> by lazy {
        return@lazy adapterCreator.createAdapter()
    }
    protected lateinit var recyclerView: DialogRecyclerView
        private set
    var itemDecoration: ItemDecoration? = null
        private set
    open val adapter: DialogPickerAdapter<ItemType>
        get() = internalAdapter
    protected abstract val adapterCreator: AdapterCreator<ItemType>

    companion object {
        private const val ADAPTER_STATE_TAG = "ADAPTER_STATE_TAG"
    }

    protected open fun withItemAnimator(): Boolean {
        return false
    }

    @Override
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        return View.inflate(context, R.layout.dialog_picker_normal, null)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        recyclerView = requireView().findViewById(R.id.dialogRecycler)
        recyclerView.apply {
            isVerticalScrollBarEnabled = true
            isScrollbarFadingEnabled = false
            if (!withItemAnimator()) itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            addMotionEventListener(object : DialogRecyclerView.MotionEventListener {
                override fun onMotionEvent(event: MotionEvent) {
                    when (event.action) {
                        ACTION_DOWN, ACTION_MOVE -> enableBottomSheetDrag(false)
                        ACTION_UP -> enableBottomSheetDrag(true)
                    }
                }
            })
            if (itemDecorationCount > 0) removeItemDecorationAt(0)
            if (itemDecoration != null) addItemDecoration(itemDecoration!!, 0)
            adapter = this@AdapterPickerDialog.internalAdapter
        }
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
        val selection = newSelection ?: intArrayOf()
        with(internalAdapter.collection) {
            val currentAdapterSelection = selectedPositionsAsArray
            val needAdapterSync = !selection.contentEquals(currentAdapterSelection)
            if (!needAdapterSync) return@with
            selectPositions(selection, newState = true, incremental = false)
        }
    }

    @Override
    override fun doOnViewStateRestored(savedInstanceState: Bundle?) {
        super.doOnViewStateRestored(savedInstanceState)
        with(internalAdapter.collection) {
            addOnSelectionChangeListener(object : SelectionChangeListener {
                override fun onChanged(newSelection: IntArray?) {
                    if (isDialogShown) dialogSelection.setDraftSelection(newSelection)
                    else dialogSelection.setCurrentSelection(newSelection)
                }
            })
        }
    }

    @Override
    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect,
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
            putParcelable(ADAPTER_STATE_TAG, internalAdapter.saveAdapterState())
        }
    }

    @Override
    override fun doOnRestoreDialogProperties(savedState: Bundle) {
        super.doOnRestoreDialogProperties(savedState)
        savedState.apply {
            val adapterState = BundleUtils.getParcelable(ADAPTER_STATE_TAG, this, Bundle::class.java)!!
            internalAdapter.restoreAdapterState(adapterState)
        }
    }

    @Override
    override fun setSelection(newSelection: IntArray?) {
        super.setSelection(newSelection)
        with(internalAdapter.collection) {
            val selection = newSelection ?: intArrayOf()
            selectPositions(positions = selection, newState = true, incremental = false)
        }
    }

    fun setData(data: List<ItemType>?) {
        internalAdapter.collection.set(data ?: mutableListOf())
    }

    fun selectItem(item: ItemType?) {
        if (item == null) return
        val position = internalAdapter.collection.getPosition(item)
        if (position != -1) setSelection(intArrayOf(position))
    }

    fun setSelection(newSelection: Int) {
        setSelection(intArrayOf(newSelection))
    }

    fun setItemDecoration(itemDecoration: ItemDecoration?) {
        this.itemDecoration = itemDecoration
        if (view == null) return
        clearItemDecorations()
        if (itemDecoration == null) return
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun clearItemDecorations() {
        recyclerView.apply {
            (0 until itemDecorationCount).forEach { _ ->
                removeItemDecorationAt(0)
            }
        }
    }

    interface AdapterCreator<ItemType : EasyAdapterDataModel> {
        fun createAdapter(): DialogPickerAdapter<ItemType>
    }
}