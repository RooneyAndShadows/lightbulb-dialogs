package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.Window
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogBundleHelper
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter.ColorModel
import java.util.ArrayList

class ColorPickerDialog : AdapterPickerDialog<ColorModel>() {
    private var spans = 0
    private var lastVisibleItemPosition = -1

    companion object {
        private val iconSize = ResourceUtils.dpToPx(50)
        fun newInstance(
            title: String?,
            message: String?,
            positive: DialogButtonConfiguration?,
            negative: DialogButtonConfiguration?,
            cancelable: Boolean,
            animationType: DialogAnimationTypes = DialogAnimationTypes.NO_ANIMATION
        ): ColorPickerDialog {
            val dialogFragment = ColorPickerDialog()
            dialogFragment.arguments = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType)
                .bundle
            return dialogFragment
        }
    }

    @Override
    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        super.doOnCreate(dialogArguments, savedInstanceState)
        if (savedInstanceState != null) lastVisibleItemPosition = savedInstanceState.getInt("LAST_VISIBLE_ITEM")
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        val manager = recyclerView.layoutManager as GridLayoutManager?
        if (manager != null) outState!!.putInt("LAST_VISIBLE_ITEM", manager.findFirstVisibleItemPosition())
    }

    override fun configureContent(view: View, savedInstanceState: Bundle?) {
        super.configureContent(view, savedInstanceState)
    }

    override fun configureRecyclerView(recyclerView: RecyclerView?) {
        super.configureRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        spans = Math.min(7, regularDialogConstraints.maxWidth / iconSize)
        recyclerView!!.layoutParams.height = 1 //Fixes rendering all possible icons (later will be resized)
        recyclerView.layoutManager = GridLayoutManager(
            context,
            spans,
            RecyclerView.VERTICAL,
            false
        )
        recyclerView.clipToPadding = false
        recyclerView.setPadding(ResourceUtils.dpToPx(8), 0, ResourceUtils.dpToPx(8), 0)
    }

    protected override val regularConstraints: RegularDialogConstraints?
        protected get() {
            val orientation = resources.configuration.orientation
            val maxWidth = getPercentOfWindowWidth(if (orientation == Configuration.ORIENTATION_PORTRAIT) 85 else 65)
            return RegularDialogConstraintsBuilder(this)
                .default()
                .withMaxWidth(maxWidth)
                .withMaxHeight(Math.min(getPercentOfWindowHeight(85), ResourceUtils.dpToPx(450)))
                .build()
        }

    override fun setupRegularDialog(
        constraints: RegularDialogConstraints,
        dialogWindow: Window,
        dialogLayout: View,
        fgPadding: Rect
    ) {
        val adapter = requireAdapter()
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        val items = adapter.getItems().size
        val rows = Math.ceil(items.toDouble() / spans).toInt()
        var dialogLayoutHeight = dialogLayout.measuredHeight
        val RecyclerRequiredHeight = rows * iconSize + recyclerView.paddingBottom + recyclerView.paddingTop
        val recyclerRequiredWidth = spans * iconSize + recyclerView.paddingRight + recyclerView.paddingLeft
        var recyclerWidth = recyclerView.measuredWidth
        var recyclerHeight = recyclerView.measuredHeight
        dialogLayoutHeight = dialogLayoutHeight - recyclerHeight
        if (recyclerWidth > recyclerRequiredWidth) recyclerWidth =
            recyclerRequiredWidth else if (recyclerWidth < recyclerRequiredWidth) recyclerWidth = spans * iconSize
        if (recyclerHeight > RecyclerRequiredHeight) recyclerHeight =
            RecyclerRequiredHeight else if (recyclerHeight < RecyclerRequiredHeight) recyclerHeight = iconSize * rows
        val desiredWidth = Math.max(recyclerWidth, dialogLayout.measuredWidth)
        var desiredHeight = dialogLayoutHeight + recyclerHeight
        if (desiredHeight > constraints.maxHeight) {
            desiredHeight -= recyclerHeight
            recyclerHeight = constraints.maxHeight - desiredHeight
            desiredHeight += recyclerHeight
        }
        val newWidth = constraints.resolveWidth(desiredWidth)
        val newHeight = constraints.resolveHeight(desiredHeight)
        recyclerView.layoutParams.height = recyclerHeight
        dialogWindow.setLayout(newWidth + horPadding, newHeight + verPadding)
        //dialogLayout.setLayoutParams(new ViewGroup.LayoutParams(newWidth, newHeight));
        if (lastVisibleItemPosition != -1) recyclerView.scrollToPosition(lastVisibleItemPosition)
    }

    private fun getAdapterIconsByExternalNames(externalNames: List<String>): Array<ColorModel?> {
        val adapterItems: List<ColorModel> = adapter!!.getItems()
        val positionsInAdapter: MutableList<Int> = ArrayList()
        for (adapterPosition in adapterItems.indices) {
            val adapterIcon = adapterItems[adapterPosition]
            for (externalName in externalNames) {
                if (adapterIcon.colorExternalName == externalName) positionsInAdapter.add(adapterPosition)
            }
        }
        val items: List<ColorModel> = adapter!!.getItems(positionsInAdapter)
        val itemsArray = arrayOfNulls<ColorModel>(items.size)
        for (i in items.indices) itemsArray[i] = items[i]
        return itemsArray
    }

    private fun getAdapterIconsByHexCodes(hexCodes: List<String>): Array<ColorModel?> {
        val adapterItems: List<ColorModel> = adapter!!.getItems()
        val positionsInAdapter: MutableList<Int> = ArrayList()
        for (adapterPosition in adapterItems.indices) {
            val adapterIcon = adapterItems[adapterPosition]
            for (hex in hexCodes) {
                if (adapterIcon.colorHex == hex) positionsInAdapter.add(adapterPosition)
            }
        }
        val items: List<ColorModel> = adapter!!.getItems(positionsInAdapter)
        val itemsArray = arrayOfNulls<ColorModel>(items.size)
        for (i in items.indices) itemsArray[i] = items[i]
        return itemsArray
    }
}