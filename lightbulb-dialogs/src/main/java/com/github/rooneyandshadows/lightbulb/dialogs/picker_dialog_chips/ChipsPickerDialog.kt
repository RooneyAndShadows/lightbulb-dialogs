package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.doOnTextChanged
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
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
        private const val DEFAULT_MAX_ROWS = 4
        private const val LAST_VISIBLE_ITEM_KEY = "LAST_VISIBLE_ITEM_KEY"
        private const val MAX_ROWS_KEY = "MAX_ROWS_KEY"
        fun newInstance(): ChipsPickerDialog {
            return ChipsPickerDialog()
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.apply {
            putInt(MAX_ROWS_KEY, maxRows)
            val layoutManager = recyclerView?.layoutManager as FlexboxLayoutManager?
            if (layoutManager != null) putInt(LAST_VISIBLE_ITEM_KEY, layoutManager.findFirstVisibleItemPosition())
            else putInt(LAST_VISIBLE_ITEM_KEY, lastVisibleItemPosition)
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        savedState.apply {
            lastVisibleItemPosition = savedState.getInt(LAST_VISIBLE_ITEM_KEY)
            maxRows = getInt(MAX_ROWS_KEY)
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
                filterView!!.dialog = this@ChipsPickerDialog
                filterView!!.allowAddition = allowAddNewOptions
            }
        }
        this.recyclerView?.apply {
            val spacing = ResourceUtils.getDimenPxById(context, R.dimen.spacing_size_small)
            val decoration = FlexboxSpaceItemDecoration(spacing, this)
            addItemDecoration(decoration)
            layoutParams.height = 1//Fixes rendering all possible labels (later will be resized)
            layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
            clipToPadding = false
        }
        //if (lastVisibleItemPosition != -1) recyclerView.scrollToPosition(lastVisibleItemPosition)
        /* iconListLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position < listItems.size() && adapter.getItemViewType(position) == Item.TYPE_HEADER) {
                    return iconListLayout.getSpanCount();
                } else {
                    return 1;
                }
            }
        });*/
    }

    @Override
    override fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
    ) {
        val recyclerDimens = calculateRecyclerWidthAndHeight()
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val recyclerView = this.recyclerView!!
        val recyclerHeight = if (cachedRecyclerHeight == -1) recyclerView.measuredHeight else cachedRecyclerHeight
        val dialogHeightWithoutRecycler = dialogLayout.measuredHeight - recyclerHeight
        val desiredRecyclerHeight = recyclerDimens.second
        val maxRecyclerHeight = getMaxHeight() - dialogHeightWithoutRecycler
        val newRecyclerHeight = min(maxRecyclerHeight, desiredRecyclerHeight)
        cachedRecyclerHeight = newRecyclerHeight
        val desiredHeight = dialogHeightWithoutRecycler + newRecyclerHeight
        val newHeight = constraints.resolveHeight(desiredHeight)
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
        for (item in chipsAdapter.getAllItems()) {
            textView.text = item.chipTitle
            chipView.measure(widthMeasureSpec, heightMeasureSpec)
            val widthToAdd = chipView.measuredWidth + itemDecorationSpace
            totalRequiredWidth += widthToAdd
            calculatedRows = totalRequiredWidth / getMaxWidth()
            if (calculatedRows >= maxRows) break
        }
        calculatedRows = min(calculatedRows, maxRows)
        val rowsHeight = (calculatedRows * (chipHeight + itemDecorationSpace))
        val desiredHeight = rowsHeight + recyclerView.paddingBottom + recyclerView.paddingTop
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
        return chipsAdapter.getAllItems().stream().filter(predicate).toList()
    }

    private fun getChipsByNames(names: List<String>): List<ChipModel> {
        return chipsAdapter.getAllItems().stream().filter(Predicate { chipModel ->
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