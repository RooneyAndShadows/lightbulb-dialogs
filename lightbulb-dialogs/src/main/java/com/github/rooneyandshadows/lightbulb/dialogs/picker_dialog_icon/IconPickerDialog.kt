package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconModel
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconPickerAdapter
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.collection.ExtendedCollection.SelectableModes.SELECT_SINGLE
import java.util.function.Predicate
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Suppress("unused", "UNUSED_PARAMETER")
class IconPickerDialog : AdapterPickerDialog<IconModel>() {
    private var spans = 0
    private var lastVisibleItemPosition = -1
    private val iconSize = ResourceUtils.dpToPx(50)
    override var dialogType: DialogTypes
        get() = DialogTypes.NORMAL
        set(value) {}
    override val adapter: IconPickerAdapter
        get() = super.adapter as IconPickerAdapter

    companion object {
        private const val LAST_VISIBLE_ITEM_KEY = "LAST_VISIBLE_ITEM_KEY"

        fun newInstance(): IconPickerDialog {
            return IconPickerDialog()
        }
    }

    init {
        setAdapter(IconPickerAdapter(SELECT_SINGLE))
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
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
        recyclerView.layoutParams.height = 1 //Fixes rendering all possible icons (later will be resized)
        spans = min(7, getMaxWidth() / iconSize)
        recyclerView.layoutManager = GridLayoutManager(context, spans, RecyclerView.VERTICAL, false)
        recyclerView.clipToPadding = false
        recyclerView.setPadding(ResourceUtils.dpToPx(8), 0, ResourceUtils.dpToPx(8), 0)
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
    override fun getRegularConstraints(): RegularDialogConstraints {
        val orientation = resources.configuration.orientation
        val maxWidth = getPercentOfWindowWidth(if (orientation == Configuration.ORIENTATION_PORTRAIT) 85 else 65)
        return RegularDialogConstraintsBuilder(this)
            .default()
            .withMaxWidth(maxWidth)
            .withMaxHeight(min(getPercentOfWindowHeight(85), ResourceUtils.dpToPx(450)))
            .build()
    }

    @Override
    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect,
    ) {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        val items = adapter.collection.getItems().size
        val rows = ceil(items.toDouble() / spans).toInt()
        var dialogLayoutHeight = dialogLayout.measuredHeight
        val recyclerRequiredHeight = rows * iconSize + recyclerView.paddingBottom + recyclerView.paddingTop
        val recyclerRequiredWidth = spans * iconSize + recyclerView.paddingRight + recyclerView.paddingLeft
        var recyclerWidth = recyclerView.measuredWidth
        var recyclerHeight = recyclerView.measuredHeight
        val maxHeight = constraints.getMaxHeight()
        dialogLayoutHeight -= recyclerHeight
        if (recyclerWidth > recyclerRequiredWidth) recyclerWidth =
            recyclerRequiredWidth else if (recyclerWidth < recyclerRequiredWidth) recyclerWidth = spans * iconSize
        if (recyclerHeight > recyclerRequiredHeight) recyclerHeight =
            recyclerRequiredHeight else if (recyclerHeight < recyclerRequiredHeight) recyclerHeight = iconSize * rows
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

    fun getIcons(predicate: Predicate<IconModel>): List<IconModel> {
        return adapter.collection.getItems(predicate)
    }

    private fun getIconsByNames(iconNames: List<String>): List<IconModel> {
        return adapter.collection.getItems(Predicate { iconModel ->
            return@Predicate iconNames.contains(iconModel.iconName)
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