package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerAdapter.*
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

@Suppress("unused", "UNUSED_PARAMETER")
class ChipsPickerDialog : AdapterPickerDialog<ChipModel>() {
    private var rows: Int = 4
    private var lastVisibleItemPosition = -1
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}
    override val adapterCreator: AdapterCreator<ChipModel>
        get() = object : AdapterCreator<ChipModel> {
            override fun createAdapter(): EasyRecyclerAdapter<ChipModel> {
                return ChipsPickerAdapter()
            }
        }

    companion object {
        private const val LAST_VISIBLE_ITEM_KEY = "LAST_VISIBLE_ITEM_KEY"

        fun newInstance(): ChipsPickerDialog {
            return ChipsPickerDialog()
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        val gridLayoutManager = recyclerView?.layoutManager as GridLayoutManager?
        if (gridLayoutManager != null)
            outState.putInt(LAST_VISIBLE_ITEM_KEY, gridLayoutManager.findFirstVisibleItemPosition())
        else outState.putInt(LAST_VISIBLE_ITEM_KEY, lastVisibleItemPosition)
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        lastVisibleItemPosition = savedState.getInt(LAST_VISIBLE_ITEM_KEY)
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        super.setupDialogContent(view, savedInstanceState)
        this.recyclerView?.apply {
            val itemDecoration = FlexboxSpaceItemDecoration(ResourceUtils.dpToPx(10), this)
            addItemDecoration(itemDecoration)
            layoutParams.height = getMaxHeight()
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

    @SuppressLint("InflateParams")
    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect,
    ) {
        val chipView = LayoutInflater.from(context).inflate(R.layout.layout_chip_item, null).apply {
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            measure(widthMeasureSpec, heightMeasureSpec)
        }
        val recyclerView = this.recyclerView!!
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var dialogLayoutHeight = dialogLayout.measuredHeight
        var recyclerHeight = rows * chipView.measuredHeight + recyclerView.paddingBottom + recyclerView.paddingTop
        var recyclerWidth =
            getRequiredWidth(chipView as LinearLayoutCompat) + recyclerView.paddingRight + recyclerView.paddingLeft
        val maxHeight = constraints.getMaxHeight()
        dialogLayoutHeight -= recyclerHeight
        val desiredWidth = max(recyclerWidth, dialogLayout.measuredWidth)
        var desiredHeight = dialogLayoutHeight + recyclerHeight
        if (desiredHeight > maxHeight) {
            desiredHeight -= recyclerHeight
            recyclerHeight = maxHeight - desiredHeight
            desiredHeight += recyclerHeight
        }
        val newWidth = constraints.resolveWidth(desiredWidth)
        val newHeight = constraints.resolveHeight(desiredHeight)
        recyclerView.layoutParams.height = recyclerHeight
        dialogWindow.setLayout(newWidth + horPadding, newHeight + verPadding)
        //dialogLayout.setLayoutParams(new ViewGroup.LayoutParams(newWidth, newHeight));
        if (lastVisibleItemPosition != -1) recyclerView.scrollToPosition(lastVisibleItemPosition)
    }

    @SuppressLint("InflateParams")
    private fun getRequiredWidth(chipView: LinearLayoutCompat): Int {
        var requiredWidth = 0

        val textView: TextView = chipView.findViewById(R.id.chip_text_view)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        adapter.getItems().forEach {
            textView.text = it.chipTitle
            chipView.measure(widthMeasureSpec, heightMeasureSpec)
            requiredWidth += chipView.measuredWidth
            if (requiredWidth >= getMaxWidth()) return@forEach
        }
        return requiredWidth
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

    fun getChips(predicate: Predicate<ChipModel>): List<ChipModel> {
        return adapter.getItems(predicate)
    }

    private fun getChipsByNames(names: List<String>): List<ChipModel> {
        return adapter.getItems(Predicate { chipModel ->
            return@Predicate names.contains(chipModel.chipTitle)
        })
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