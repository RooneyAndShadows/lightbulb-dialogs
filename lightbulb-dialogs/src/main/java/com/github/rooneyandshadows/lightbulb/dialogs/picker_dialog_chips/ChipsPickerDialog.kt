package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.toList


@Suppress("unused", "UNUSED_PARAMETER")
class ChipsPickerDialog : AdapterPickerDialog<ChipModel>() {
    private var lastVisibleItemPosition = -1
    private var maxRows = DEFAULT_MAX_ROWS
    private var isFilterable = true
    private var allowAddNewOptions = true
    private var filterHintText: String? = null
    private var filterView: ChipsFilterView? = null
    private var cachedRecyclerHeight = -1
    private val chipsAdapter: ChipsPickerAdapter
        get() = adapter as ChipsPickerAdapter
    override var dialogType: DialogTypes
        get() = DialogTypes.BOTTOM_SHEET
        set(value) {}
    override val adapterCreator: AdapterCreator<ChipModel>
        get() = object : AdapterCreator<ChipModel> {
            override fun createAdapter(): EasyRecyclerAdapter<ChipModel> {
                return ChipsPickerAdapter()
            }
        }

    companion object {
        private const val DEFAULT_MAX_ROWS = 5
        private const val LAST_VISIBLE_ITEM_KEY = "LAST_VISIBLE_ITEM_KEY"
        private const val MAX_ROWS_KEY = "MAX_ROWS_KEY"
        private const val FILTER_HINT_TEXT = "FILTER_HINT_TEXT"
        fun newInstance(): ChipsPickerDialog {
            return ChipsPickerDialog()
        }
    }

    @Override
    override fun withItemAnimator(): Boolean {
        return false
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.apply {
            putInt(MAX_ROWS_KEY, maxRows)
            putString(FILTER_HINT_TEXT, filterHintText)
            (recyclerView?.layoutManager as FlowLayoutManager).apply {
                putParcelable("LAYOUT_MANAGER_STATE", onSaveInstanceState())
            }
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        savedState.apply bundle@{
            filterHintText = getString(FILTER_HINT_TEXT)
            maxRows = getInt(MAX_ROWS_KEY)
            (recyclerView?.layoutManager as FlowLayoutManager).apply {
                val layoutState = BundleUtils.getParcelable("LAYOUT_MANAGER_STATE", this@bundle, Parcelable::class.java)!!
                onRestoreInstanceState(layoutState)
            }
        }
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        super.setupDialogContent(view, savedInstanceState)
        view.findViewById<LinearLayoutCompat>(R.id.dialogContent).apply {
            if ((!allowAddNewOptions && !isFilterable)) return@apply
            if (filterView != null) {
                (filterView!!.parent as ViewGroup).removeView(filterView)
                addView(filterView, 0)
            } else {
                filterView = ChipsFilterView(context)
                addView(filterView, 0)
                filterHintText?.apply { filterView!!.setHintText(this) }
                filterView!!.dialog = this@ChipsPickerDialog
                filterView!!.allowAddition = allowAddNewOptions
            }
        }
        this.recyclerView?.apply {
            val spacing = ResourceUtils.getDimenPxById(context, R.dimen.chips_picker_spacing_size)
            setPadding(ResourceUtils.getDimenPxById(context, R.dimen.spacing_size_medium))
            layoutParams.height = 1//Fixes rendering all possible labels (later will be resized)
            layoutManager = FlowLayoutManager(FlowLayoutManager.VERTICAL, object : FlowLayoutManager.Listeners {
                override fun onFirstLineDrawnWhileScrollingUp() {
                    adapter?.apply {
                        post {
                            //fixes drawing from bottom to top on items
                            if (itemCount > 0) notifyItemChanged(0, false)
                        }
                    }
                }

            }).apply {
                setSpacingBetweenItems(spacing)
                setSpacingBetweenLines(spacing)
            }
            clipToPadding = false
            //FlexboxItemDecoration(context).apply {
            //    setOrientation(FlexboxItemDecoration.BOTH)
            //    setDrawable(ResourceUtils.getDrawable(context, R.drawable.divider_space_small))
            //    addItemDecoration(this)
            //    layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
            //}
        }
    }

    @Override
    override fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val recyclerView = this.recyclerView!!
        val recyclerDimens = calculateRecyclerWidthAndHeight()
        val recyclerHeight = recyclerView.measuredHeight
        val dialogHeightWithoutRecycler = dialogLayout.measuredHeight - recyclerHeight
        val desiredRecyclerHeight = recyclerDimens.second
        val maxRecyclerHeight = getMaxHeight() - dialogHeightWithoutRecycler
        val newRecyclerHeight = min(maxRecyclerHeight, desiredRecyclerHeight)
        val desiredHeight = dialogHeightWithoutRecycler + newRecyclerHeight
        val newHeight = constraints.resolveHeight(desiredHeight)
        println(newRecyclerHeight)
        recyclerView.layoutParams.height = newRecyclerHeight
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, newHeight)
        if (lastVisibleItemPosition != -1) recyclerView.scrollToPosition(lastVisibleItemPosition)
    }

    @SuppressLint("InflateParams")
    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect,
    ) {
        val recyclerDimens = calculateRecyclerWidthAndHeight()
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val recyclerView = this.recyclerView!!
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        val dialogHeightWithoutRecycler = dialogLayout.measuredHeight - recyclerView.measuredHeight
        val dialogWidthWithoutRecycler = dialogLayout.measuredWidth
        val desiredRecyclerWidth = recyclerDimens.first
        val desiredRecyclerHeight = recyclerDimens.second
        val maxRecyclerHeight = getMaxHeight() - dialogHeightWithoutRecycler
        val newRecyclerHeight = min(maxRecyclerHeight, desiredRecyclerHeight)
        val desiredWidth = max(dialogWidthWithoutRecycler, desiredRecyclerWidth)
        val desiredHeight = dialogHeightWithoutRecycler + newRecyclerHeight
        val newWidth = constraints.resolveWidth(desiredWidth)
        val newHeight = constraints.resolveHeight(desiredHeight)
        recyclerView.layoutParams.height = newRecyclerHeight
        dialogWindow.setLayout(newWidth + horPadding, newHeight + verPadding)
        if (lastVisibleItemPosition != -1) recyclerView.scrollToPosition(lastVisibleItemPosition)
    }

    @SuppressLint("InflateParams")
    private fun calculateRecyclerWidthAndHeight(): Pair<Int, Int> {
        val recyclerView = this.recyclerView!!
        val chipView = LayoutInflater.from(context).inflate(R.layout.layout_chip_item, null).apply {
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            measure(widthMeasureSpec, heightMeasureSpec)
        }
        val chipHeight = chipView.measuredHeight
        val itemDecorationSpace = ResourceUtils.getDimenPxById(requireContext(), R.dimen.chips_picker_spacing_size)
        val textView: TextView = chipView.findViewById(R.id.chip_text_view)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        var totalRequiredWidth = 0
        var calculatedRows = 0
        for (item in chipsAdapter.getItems()) {
            textView.text = item.chipTitle
            chipView.measure(widthMeasureSpec, heightMeasureSpec)
            val widthToAdd = chipView.measuredWidth + itemDecorationSpace
            totalRequiredWidth += widthToAdd
            calculatedRows = totalRequiredWidth / getMaxWidth()
            if (calculatedRows >= maxRows) break
        }
        calculatedRows = min(calculatedRows, maxRows)
        val rowsHeight = (calculatedRows * (chipHeight + itemDecorationSpace))
        val desiredHeight = rowsHeight + recyclerView.paddingBottom + recyclerView.paddingTop - (itemDecorationSpace * 2)
        println(desiredHeight)
        val desiredWidth = min(getMaxWidth(), totalRequiredWidth)
        return Pair(desiredWidth, desiredHeight)
    }

    @Override
    override fun getRegularConstraints(): RegularDialogConstraints {
        val orientation = resources.configuration.orientation
        val maxWidth = getPercentOfWindowWidth(if (orientation == Configuration.ORIENTATION_PORTRAIT) 85 else 65)
        return RegularDialogConstraintsBuilder(this)
            .default()
            .withMaxWidth(maxWidth)
            .withMaxHeight(min(getPercentOfWindowHeight(85), ResourceUtils.dpToPx(450)))
            .build()
    }

    fun setFilterHintText(filterHintText: String) {
        this.filterHintText = filterHintText
        filterView?.setHintText(filterHintText)
    }

    fun setFilterable(isFilterable: Boolean) {
        this.isFilterable = isFilterable
    }

    fun setAllowNewOptionCreation(allowCreation: Boolean) {
        this.allowAddNewOptions = allowCreation
    }

    fun setMaxRows(maxRows: Int) {
        if (maxRows <= 0) this.maxRows = DEFAULT_MAX_ROWS
        else this.maxRows = maxRows
    }

    fun getChips(predicate: Predicate<ChipModel>): List<ChipModel> {
        return chipsAdapter.getItems().stream().filter(predicate).toList()
    }

    private fun getChipsByNames(names: List<String>): List<ChipModel> {
        return chipsAdapter.getItems().stream().filter(Predicate { chipModel ->
            return@Predicate names.contains(chipModel.chipTitle)
        }).toList()
    }

    /*private class IconLayoutManager extends GridLayoutManager {
        private final int iconSize;
        IconLayoutManager(Context context, int iconSize) {
            super(context, -1, RecyclerView.VERTICAL, false);
            this.iconSize = iconSize;
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            int width = getWidth();
            int height = getHeight();
            if (getSpanCount() == -1 && iconSize > 0 && width > 0 && height > 0) {
                // Adjust span count on available space and icon size
                int layoutWidth = width - getPaddingRight() - getPaddingLeft();
                setSpanCount(Math.max(1, layoutWidth / iconSize));
            }
            super.onLayoutChildren(recycler, state);
        }
    }*/
}