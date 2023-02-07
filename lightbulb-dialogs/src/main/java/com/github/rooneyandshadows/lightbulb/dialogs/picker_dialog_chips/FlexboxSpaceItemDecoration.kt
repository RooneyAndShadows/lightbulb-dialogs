package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
class FlexboxSpaceItemDecoration : RecyclerView.ItemDecoration {
    private val verticalSpacing: Int
    private val horizontalSpacing: Int

    constructor(spacing: Int, recyclerView: RecyclerView) {
        verticalSpacing = spacing / 2
        horizontalSpacing = spacing / 2
        recyclerView.setPadding(horizontalSpacing, spacing, horizontalSpacing, 0)
    }

    constructor(verticalSpacing: Int, horizontalSpacing: Int, recyclerView: RecyclerView) {
        this.verticalSpacing = verticalSpacing / 2
        this.horizontalSpacing = horizontalSpacing / 2
        recyclerView.setPadding(this.verticalSpacing, verticalSpacing, this.verticalSpacing, 0)
    }

    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = horizontalSpacing
        outRect.right = horizontalSpacing
        outRect.bottom = verticalSpacing * 2
    }
}