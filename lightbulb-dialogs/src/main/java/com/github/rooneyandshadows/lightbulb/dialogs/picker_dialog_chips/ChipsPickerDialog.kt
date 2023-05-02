package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.view.View.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsFilterView.*
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter.ChipModel
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter.ChipsPickerAdapter
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.adapter.ChipsPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.collection.EasyRecyclerAdapterCollection.CollectionChangeListener
import java.util.function.Predicate
import kotlin.math.ceil
import kotlin.math.min
import kotlin.streams.toList


@Suppress("unused", "UNUSED_PARAMETER")
class ChipsPickerDialog : AdapterPickerDialog<ChipModel>() {
    private var lastVisibleItemPosition = -1
    private var maxRows = DEFAULT_MAX_ROWS
    var isFilterable = true
        private set
    var allowAddNewOptions = true
        private set
    private var onChipCreatedListener: OnOptionCreatedListener? = null
    private var filterHintText: String? = null
    private var filterView: ChipsFilterView? = null
    private var cachedRecyclerHeight = -1
    override val adapter: ChipsPickerAdapter
        get() = super.adapter as ChipsPickerAdapter
    override var dialogType: DialogTypes
        get() = DialogTypes.BOTTOM_SHEET
        set(value) {}
    override val adapterCreator: AdapterCreator<ChipModel>
        get() = object : AdapterCreator<ChipModel> {
            override fun createAdapter(): ChipsPickerAdapter {
                return ChipsPickerAdapter()
            }
        }

    companion object {
        private const val DEFAULT_MAX_ROWS = 5
        private const val LAST_VISIBLE_ITEM_KEY = "LAST_VISIBLE_ITEM_KEY"
        private const val MAX_ROWS_KEY = "MAX_ROWS_KEY"
        private const val FILTER_HINT_TEXT = "FILTER_HINT_TEXT"
        private const val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"
        fun newInstance(): ChipsPickerDialog {
            return ChipsPickerDialog()
        }
    }

    init {
        with(adapter.collection) {
            addOnCollectionChangedListener(object : CollectionChangeListener {
                override fun onChanged() {
                    measureDialogLayout()
                }
            })
        }
    }

    @Override
    override fun withItemAnimator(): Boolean {
        return true
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.apply {
            putInt(MAX_ROWS_KEY, maxRows)
            putString(FILTER_HINT_TEXT, filterHintText)
            (recyclerView.layoutManager as FlowLayoutManager).apply {
                putParcelable(LAYOUT_MANAGER_STATE, onSaveInstanceState())
            }
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        savedState.apply bundle@{
            filterHintText = getString(FILTER_HINT_TEXT)
            maxRows = getInt(MAX_ROWS_KEY)
            (recyclerView.layoutManager as FlowLayoutManager).apply {
                val layoutState = BundleUtils.getParcelable(LAYOUT_MANAGER_STATE, this@bundle, Parcelable::class.java)!!
                onRestoreInstanceState(layoutState)
            }
        }
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        super.setupDialogContent(view, savedInstanceState)
        view.findViewById<LinearLayoutCompat>(R.id.dialogContent).apply {
            if (filterView != null) {
                (filterView!!.parent as ViewGroup).removeView(filterView)
                addView(filterView, 0, LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            } else {
                filterView = ChipsFilterView(context)
                addView(filterView, 0, LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
                filterHintText?.apply { filterView!!.setHintText(this) }
                filterView!!.dialog = this@ChipsPickerDialog
                filterView!!.setAllowAddition(allowAddNewOptions)
                filterView!!.isVisible = isFilterable
                filterView!!.setOnOptionCreatedListener(onChipCreatedListener)
            }
        }
        this.recyclerView.apply {
            val spacing = ResourceUtils.getDimenPxById(context, R.dimen.chips_picker_spacing_size)
            setPadding(ResourceUtils.getDimenPxById(context, R.dimen.spacing_size_medium))
            layoutParams.height = 1 //Fixes rendering all possible labels (later will be resized)
            layoutManager = FlowLayoutManager(FlowLayoutManager.VERTICAL).apply {
                setSpacingBetweenItems(spacing)
                setSpacingBetweenLines(spacing)
            }
            clipToPadding = false
            /* fixes drawing from bottom to top
                object : FlowLayoutManager.Listeners {
                    override fun onFirstLineDrawnWhileScrollingUp() {
                        adapter?.apply {
                            post {
                                val animator = itemAnimator
                                itemAnimator = null
                                //fixes drawing from bottom to top on items
                                if (itemCount > 0) notifyItemChanged(0, false)
                                itemAnimator = animator
                            }
                        }
                    }
                }
                */
            /*FlexboxItemDecoration(context).apply {
                    setOrientation(FlexboxItemDecoration.BOTH)
                    setDrawable(ResourceUtils.getDrawable(context, R.drawable.divider_space_small))
                    addItemDecoration(this)
                    layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
                }*/
        }
    }

    @Override
    override fun setupBottomSheetDialog(
        constraints: BottomSheetDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
    ) {
        filterView?.apply {
            val paddingTop = if (headerViewHierarchy.isVisible()) 0
            else ResourceUtils.getDimenPxById(
                requireContext(),
                R.dimen.spacing_size_medium
            )
            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
        val recyclerView = this.recyclerView
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val headerHeight = headerViewHierarchy.titleAndMessageContainer?.measuredHeight ?: 0
        val footerHeight = footerViewHierarchy.buttonsContainer?.measuredHeight ?: 0
        val filterViewHeight = filterView.let {
            if (it == null || !it.isVisible) return@let 0
            it.measure(widthMeasureSpec, heightMeasureSpec)
            return@let it.measuredHeight
        }
        val dialogHeightWithoutRecycler = headerHeight + footerHeight + filterViewHeight
        val recyclerDimens = calculateRecyclerWidthAndHeight()
        val desiredRecyclerHeight = recyclerDimens.second
        val maxRecyclerHeight = getMaxHeight() - dialogHeightWithoutRecycler
        val newRecyclerHeight = min(maxRecyclerHeight, desiredRecyclerHeight)
        val desiredHeight = dialogHeightWithoutRecycler + newRecyclerHeight
        val newHeight = constraints.resolveHeight(desiredHeight)
        recyclerView.layoutParams.height = newRecyclerHeight
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, newHeight)
        if (lastVisibleItemPosition != -1) recyclerView.scrollToPosition(lastVisibleItemPosition)
    }

    @SuppressLint("InflateParams")
    private fun calculateRecyclerWidthAndHeight(): Pair<Int, Int> {
        val recyclerView = this.recyclerView
        val chipView = LayoutInflater.from(context).inflate(R.layout.layout_chip_item, null).apply {
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            measure(widthMeasureSpec, heightMeasureSpec)
        }
        val itemDecorationSpace = ResourceUtils.getDimenPxById(requireContext(), R.dimen.chips_picker_spacing_size)
        val chipHeight = chipView.measuredHeight + itemDecorationSpace
        val textView: TextView = chipView.findViewById(R.id.chip_text_view)
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        var totalRequiredWidth = 0
        var calculatedRows = 0
        val maxWidth = getMaxWidth() - recyclerView.paddingStart - recyclerView.paddingEnd
        for (item in adapter.collection.getItems()) {
            textView.text = item.chipTitle
            chipView.measure(widthMeasureSpec, heightMeasureSpec)
            val widthToAdd = chipView.measuredWidth + itemDecorationSpace
            totalRequiredWidth += widthToAdd
            calculatedRows = ceil(totalRequiredWidth.toDouble() / maxWidth).toInt()
            if (calculatedRows >= maxRows) break
        }
        println(calculatedRows)
        calculatedRows = min(calculatedRows, maxRows)
        val rowsHeight = (calculatedRows * chipHeight)
        val desiredHeight = rowsHeight + recyclerView.paddingBottom
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

    fun setOnNewOptionListener(listener: OnOptionCreatedListener?) {
        this.onChipCreatedListener = listener
        filterView?.setOnOptionCreatedListener(onChipCreatedListener)
    }

    fun setFilterHintText(filterHintText: String) {
        this.filterHintText = filterHintText
        filterView?.setHintText(filterHintText)
    }

    fun setFilterable(isFilterable: Boolean) {
        this.isFilterable = isFilterable
        filterView?.visibility = if (isFilterable) VISIBLE else GONE
        if (isDialogShown) measureDialogLayout()
    }

    fun setAllowAddNewOptions(allowCreation: Boolean) {
        this.allowAddNewOptions = allowCreation
        filterView?.setAllowAddition(allowCreation)
    }

    fun setMaxRows(maxRows: Int) {
        if (maxRows <= 0) this.maxRows = DEFAULT_MAX_ROWS
        else this.maxRows = maxRows
    }

    fun getFilteredChips(): List<ChipModel> {
        return adapter.collection.filteredItems
    }

    fun getChips(predicate: Predicate<ChipModel>): List<ChipModel> {
        return adapter.collection.getItems(predicate)
    }

    private fun getChipsByNames(names: List<String>): List<ChipModel> {
        return adapter.collection.getItems().stream().filter(Predicate { chipModel ->
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