package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import android.graphics.PointF
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Astrocode on 26.05.18.
 */
class FlowLayoutManager @JvmOverloads constructor(
    orientation: Int,
    private val listeners: Listeners? = null,
    gravity: Int = Gravity.START,
    maxItemsInLine: Int = DEFAULT_COUNT_ITEM_IN_LINE,
    spacingBetweenItems: Int = 0,
    spacingBetweenLines: Int = 0,
) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {
    private var mGravity: Int
    private var mOrientation: Int
    private var mMaxItemsInLine: Int
    private var mSpacingBetweenItems: Int
    private var mSpacingBetweenLines: Int
    private var mLayoutManagerHelper: FLMLayoutManagerHelper
    private val mCurrentLines: ArrayList<Line>
    private var mFirstItemAdapterIndex: Int
    private var mFirstLineStartPosition: Int

    init {
        mCurrentLines = ArrayList()
        mGravity = gravity
        mFirstItemAdapterIndex = 0
        mFirstLineStartPosition = -1
        require(!(maxItemsInLine == 0 || maxItemsInLine < -1)) { ERROR_BAD_ARGUMENT }
        mMaxItemsInLine = maxItemsInLine
        mSpacingBetweenItems = spacingBetweenItems
        mSpacingBetweenLines = spacingBetweenLines
        require(!(orientation != HORIZONTAL && orientation != VERTICAL)) { ERROR_UNKNOWN_ORIENTATION }
        mOrientation = orientation
        mLayoutManagerHelper = FLMLayoutManagerHelper.createLayoutManagerHelper(this, orientation, mGravity)
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        var currentLine: Line? = null
        if (mFirstLineStartPosition == -1) {
            mFirstLineStartPosition = mLayoutManagerHelper.startAfterPadding
        }
        var topOrLeft = mFirstLineStartPosition
        detachAndScrapAttachedViews(recycler)
        mCurrentLines.clear()
        var i = mFirstItemAdapterIndex
        while (i < itemCount) {
            currentLine = addLineToEnd(i, topOrLeft, recycler)
            mCurrentLines.add(currentLine)
            topOrLeft = mSpacingBetweenLines + currentLine.mEndValueOfTheHighestItem
            if (currentLine.mEndValueOfTheHighestItem > mLayoutManagerHelper.endAfterPadding) {
                break
            }
            i += currentLine.mItemsCount
        }
        if (mFirstItemAdapterIndex > 0 && currentLine != null) {
            val availableOffset: Int =
                currentLine.mEndValueOfTheHighestItem - mLayoutManagerHelper.end + mLayoutManagerHelper.endPadding
            if (availableOffset < 0) {
                if (mOrientation == VERTICAL) {
                    scrollVerticallyBy(availableOffset, recycler, state)
                } else {
                    scrollHorizontallyBy(availableOffset, recycler, state)
                }
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val data = state as Bundle
        mFirstItemAdapterIndex = data.getInt(TAG_FIRST_ITEM_ADAPTER_INDEX)
        mFirstLineStartPosition = data.getInt(TAG_FIRST_LINE_START_POSITION)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val data = Bundle()
        data.putInt(TAG_FIRST_ITEM_ADAPTER_INDEX, mFirstItemAdapterIndex)
        data.putInt(TAG_FIRST_LINE_START_POSITION, mFirstLineStartPosition)
        return data
    }

    fun setMaxItemsInLine(maxItemsInLine: Int) {
        require(maxItemsInLine > 0) { ERROR_BAD_ARGUMENT }
        assertNotInLayoutOrScroll(null)
        mMaxItemsInLine = maxItemsInLine
        requestLayout()
    }

    fun setSpacingBetweenItems(spacingBetweenItems: Int) {
        require(spacingBetweenItems >= 0) { ERROR_BAD_ARGUMENT }
        assertNotInLayoutOrScroll(null)
        mSpacingBetweenItems = spacingBetweenItems
        requestLayout()
    }

    fun setSpacingBetweenLines(spacingBetweenLines: Int) {
        require(spacingBetweenLines >= 0) { ERROR_BAD_ARGUMENT }
        assertNotInLayoutOrScroll(null)
        mSpacingBetweenLines = spacingBetweenLines
        requestLayout()
    }
    /**
     * Return current orientation of the layout manager.
     */
    /**
     * Change orientation of the layout manager
     *
     * @param orientation New orientation.
     */
    var orientation: Int
        get() = mOrientation
        set(orientation) {
            require(!(orientation != HORIZONTAL && orientation != VERTICAL)) { ERROR_UNKNOWN_ORIENTATION }
            if (orientation != mOrientation) {
                assertNotInLayoutOrScroll(null)
                mOrientation = orientation
                mLayoutManagerHelper = FLMLayoutManagerHelper.createLayoutManagerHelper(this, orientation, mGravity)
                requestLayout()
            }
        }
    /**
     * Return current gravity of the layout manager.
     */
    /**
     * Change gravity of the layout manager.
     *
     * @param gravity New gravity.
     */
    var gravity: Int
        get() = mGravity
        set(gravity) {
            assertNotInLayoutOrScroll(null)
            if (gravity != mGravity) {
                mGravity = gravity
                mLayoutManagerHelper.setGravity(gravity)
                requestLayout()
            }
        }

    /**
     * Add one line to the end of recyclerView.
     *
     * @param startAdapterIndex Adapter index of first item of new line.
     * @param start             Start position(Top - if orientation is VERTICAL or Left - if orientation is HORIZONTAL) of the new line.
     * @return New line.
     */
    private fun addLineToEnd(startAdapterIndex: Int, start: Int, recycler: RecyclerView.Recycler): Line {
        var isEndOfLine = false
        var currentAdapterIndex = startAdapterIndex
        var currentItemsSize = 0
        var currentMaxValue = 0
        val line = Line()
        line.mStartValueOfTheHighestItem = start
        while (!isEndOfLine && currentAdapterIndex < itemCount) {
            val view = recycler.getViewForPosition(currentAdapterIndex)
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val widthOrHeight = mLayoutManagerHelper.getDecoratedMeasurementInOther(view)
            val heightOrWidth = mLayoutManagerHelper.getDecoratedMeasurement(view)
            if (line.mItemsCount == mMaxItemsInLine || currentItemsSize + widthOrHeight >= mLayoutManagerHelper.lineSize) {
                isEndOfLine = true
                if (currentItemsSize == 0) {
                    currentMaxValue = heightOrWidth
                    line.mEndValueOfTheHighestItem = line.mStartValueOfTheHighestItem + currentMaxValue
                    line.mItemsCount++
                } else {
                    detachAndScrapView(view, recycler)
                    continue
                }
            } else {
                if (heightOrWidth > currentMaxValue) {
                    currentMaxValue = heightOrWidth
                    line.mEndValueOfTheHighestItem = line.mStartValueOfTheHighestItem + currentMaxValue
                }
                line.mItemsCount++
            }
            currentItemsSize += widthOrHeight + mSpacingBetweenItems
            currentAdapterIndex++
        }
        layoutItemsToEnd(
            currentItemsSize - mSpacingBetweenItems,
            currentMaxValue,
            line.mItemsCount,
            line.mStartValueOfTheHighestItem
        )
        return line
    }

    /**
     * Arrange of views from start to end.
     *
     * @param itemsSize                  Size(width - if orientation is VERTICAL or height - if orientation is HORIZONTAL) of all items(include spacing) in line.
     * @param maxItemHeightOrWidth       Max item height(if VERTICAL) or width(if HORIZONTAL) in line.
     * @param itemsInLine                Item count in line.
     * @param startValueOfTheHighestItem Start position(Top - if orientation is VERTICAL or Left - if orientation is HORIZONTAL) of the line.
     */
    private fun layoutItemsToEnd(
        itemsSize: Int,
        maxItemHeightOrWidth: Int,
        itemsInLine: Int,
        startValueOfTheHighestItem: Int,
    ) {
        var currentStart = mLayoutManagerHelper.getStartPositionOfFirstItem(itemsSize)
        var i = itemsInLine
        val childCount = childCount
        var currentStartValue: Int
        while (i > 0) {
            val view = getChildAt(childCount - i)
            val widthOrHeight = mLayoutManagerHelper.getDecoratedMeasurementInOther(view)
            val heightOrWidth = mLayoutManagerHelper.getDecoratedMeasurement(view)
            currentStartValue = startValueOfTheHighestItem + mLayoutManagerHelper.getPositionOfCurrentItem(
                maxItemHeightOrWidth,
                heightOrWidth
            )
            if (mOrientation == VERTICAL) {
                layoutDecoratedWithMargins(
                    view!!, currentStart, currentStartValue,
                    currentStart + widthOrHeight, currentStartValue + heightOrWidth
                )
            } else {
                layoutDecoratedWithMargins(
                    view!!, currentStartValue, currentStart,
                    currentStartValue + heightOrWidth, currentStart + widthOrHeight
                )
            }
            currentStart += widthOrHeight + mSpacingBetweenItems
            i--
        }
    }

    /**
     * Add one line to the start of recyclerView.
     *
     * @param startAdapterIndex Adapter index of first item of new line.
     * @param end               End position(Bottom - if orientation is VERTICAL or Right - if orientation is HORIZONTAL) of the new line.
     * @return New line.
     */
    private fun addLineToStart(startAdapterIndex: Int, end: Int, recycler: RecyclerView.Recycler): Line {
        var isEndOfLine = false
        var currentAdapterIndex = startAdapterIndex
        var currentItemsSize = 0
        var currentMaxValue = 0
        val line = Line()
        line.mEndValueOfTheHighestItem = end
        while (!isEndOfLine && currentAdapterIndex >= 0) {
            val view = recycler.getViewForPosition(currentAdapterIndex)
            addView(view, 0)
            measureChildWithMargins(view, 0, 0)
            val widthOrHeight = mLayoutManagerHelper.getDecoratedMeasurementInOther(view)
            val heightOrWidth = mLayoutManagerHelper.getDecoratedMeasurement(view)
            if (line.mItemsCount == mMaxItemsInLine || currentItemsSize + widthOrHeight >= mLayoutManagerHelper.lineSize) {
                isEndOfLine = true
                if (currentItemsSize == 0) {
                    currentMaxValue = heightOrWidth
                    line.mStartValueOfTheHighestItem = line.mEndValueOfTheHighestItem - currentMaxValue
                    line.mItemsCount++
                } else {
                    detachAndScrapView(view, recycler)
                    continue
                }
            } else {
                if (heightOrWidth > currentMaxValue) {
                    currentMaxValue = heightOrWidth
                    line.mStartValueOfTheHighestItem = line.mEndValueOfTheHighestItem - currentMaxValue
                }
                line.mItemsCount++
            }
            currentItemsSize += widthOrHeight + mSpacingBetweenItems
            currentAdapterIndex--
        }
        layoutItemsToStart(
            currentItemsSize - mSpacingBetweenItems,
            currentMaxValue,
            line.mItemsCount,
            line.mStartValueOfTheHighestItem
        )
        return line
    }

    /**
     * Arrange of views from end to start.
     *
     * @param itemsSize                  Size(width - if orientation is VERTICAL or height - if orientation is HORIZONTAL) of all items(include spacing) in line.
     * @param maxItemHeightOrWidth       Max item height(if VERTICAL) or width(if HORIZONTAL) in line.
     * @param itemsInLine                Item count in line.
     * @param startValueOfTheHighestItem Start position(Top - if orientation is VERTICAL or Left - if orientation is HORIZONTAL) of the line.
     */
    private fun layoutItemsToStart(
        itemsSize: Int,
        maxItemHeightOrWidth: Int,
        itemsInLine: Int,
        startValueOfTheHighestItem: Int,
    ) {
        var currentStart = mLayoutManagerHelper.getStartPositionOfFirstItem(itemsSize)
        var i = 0
        var currentStartValue: Int
        while (i < itemsInLine) {
            val view = getChildAt(i)
            val widthOrHeight = mLayoutManagerHelper.getDecoratedMeasurementInOther(view)
            val heightOrWidth = mLayoutManagerHelper.getDecoratedMeasurement(view)
            currentStartValue = startValueOfTheHighestItem + mLayoutManagerHelper.getPositionOfCurrentItem(
                maxItemHeightOrWidth,
                heightOrWidth
            )
            if (mOrientation == VERTICAL) {
                layoutDecoratedWithMargins(
                    view!!, currentStart, currentStartValue,
                    currentStart + widthOrHeight, currentStartValue + heightOrWidth
                )
            } else {
                layoutDecoratedWithMargins(
                    view!!, currentStartValue, currentStart,
                    currentStartValue + heightOrWidth, currentStart + widthOrHeight
                )
            }
            currentStart += widthOrHeight + mSpacingBetweenItems
            i++
        }
    }

    /**
     * Adds to start (and delete from end) of the recyclerView the required number of lines depending on the offset.
     *
     * @param offset   Original offset.
     * @param recycler
     * @return Real offset.
     */
    private fun addLinesToStartAndDeleteFromEnd(offset: Int, recycler: RecyclerView.Recycler): Int {
        var line = mCurrentLines[0]
        val availableOffset: Int = line.mStartValueOfTheHighestItem - mLayoutManagerHelper.startAfterPadding
        var currentOffset = if (availableOffset < offset) offset else availableOffset
        var adapterViewIndex = getPosition(getChildAt(0)!!) - 1
        var startValueOfNewLine = line.mStartValueOfTheHighestItem - mSpacingBetweenLines
        while (adapterViewIndex >= 0) {
            if (currentOffset <= offset) {
                deleteLinesFromEnd(offset, recycler)
                break
            } else {
                deleteLinesFromEnd(currentOffset, recycler)
            }
            line = addLineToStart(adapterViewIndex, startValueOfNewLine, recycler)
            mCurrentLines.add(0, line)
            startValueOfNewLine = line.mStartValueOfTheHighestItem - mSpacingBetweenLines
            currentOffset = line.mStartValueOfTheHighestItem
            adapterViewIndex -= line.mItemsCount
        }
        listeners?.onFirstLineDrawnWhileScrollingUp()
        return if (currentOffset < offset) offset else currentOffset
    }

    /**
     * Removes lines from the end. The number of deleted lines depends on the offset.
     *
     * @param offset   Current offset.
     * @param recycler
     */
    private fun deleteLinesFromEnd(offset: Int, recycler: RecyclerView.Recycler) {
        var lineToDel: Line? = mCurrentLines[mCurrentLines.size - 1]
        while (lineToDel != null) {
            lineToDel = if (lineToDel.mStartValueOfTheHighestItem - offset >
                mLayoutManagerHelper.endAfterPadding
            ) {
                for (i in 0 until lineToDel.mItemsCount) {
                    removeAndRecycleView(getChildAt(childCount - 1)!!, recycler)
                }
                mCurrentLines.remove(lineToDel)
                mCurrentLines[mCurrentLines.size - 1]
            } else {
                null
            }
        }
    }

    /**
     * Adds to end (and delete from start) of the recyclerView the required number of lines depending on the offset.
     *
     * @param offset   Original offset.
     * @param recycler
     * @return Real offset.
     */
    private fun addLinesToEndAndDeleteFromStart(offset: Int, recycler: RecyclerView.Recycler): Int {
        var line = mCurrentLines[mCurrentLines.size - 1]
        val availableOffset: Int =
            line.mEndValueOfTheHighestItem - mLayoutManagerHelper.end + mLayoutManagerHelper.endPadding
        var currentOffset = if (availableOffset > offset) offset else availableOffset
        var adapterViewIndex = getPosition(getChildAt(childCount - 1)!!) + 1
        var startValueOfNewLine = line.mEndValueOfTheHighestItem + mSpacingBetweenLines
        while (adapterViewIndex < itemCount) {
            if (currentOffset >= offset) {
                deleteLinesFromStart(offset, recycler)
                break
            } else {
                deleteLinesFromStart(currentOffset, recycler)
            }
            line = addLineToEnd(adapterViewIndex, startValueOfNewLine, recycler)
            mCurrentLines.add(line)
            startValueOfNewLine = line.mEndValueOfTheHighestItem + mSpacingBetweenLines
            currentOffset = line.mEndValueOfTheHighestItem - mLayoutManagerHelper.end
            adapterViewIndex += line.mItemsCount
        }
        return if (currentOffset > offset) offset else currentOffset
    }

    /**
     * Removes lines from the start. The number of deleted lines depends on the offset.
     *
     * @param offset   Current offset.
     * @param recycler
     */
    private fun deleteLinesFromStart(offset: Int, recycler: RecyclerView.Recycler) {
        var lineToDel: Line? = mCurrentLines[0]
        while (lineToDel != null) {
            lineToDel = if (lineToDel.mEndValueOfTheHighestItem - offset <
                mLayoutManagerHelper.startAfterPadding
            ) {
                for (i in 0 until lineToDel.mItemsCount) {
                    removeAndRecycleView(getChildAt(0)!!, recycler)
                }
                mCurrentLines.remove(lineToDel)
                // mItemsInLines.add(lineToDel.mItemsCount);
                mCurrentLines[0]
            } else {
                null
            }
        }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        var offset = 0
        if (childCount > 0 && dy != 0) {
            offset = if (dy > 0) {
                addLinesToEndAndDeleteFromStart(dy, recycler)
            } else {
                addLinesToStartAndDeleteFromEnd(dy, recycler)
            }
            if (offset != 0) {
                for (i in mCurrentLines.indices) {
                    mCurrentLines[i].offset(-offset)
                }
                offsetChildrenVertical(-offset)
            }
            val firstView = getChildAt(0)
            mFirstLineStartPosition = mLayoutManagerHelper.getDecoratedStart(firstView)
            mFirstItemAdapterIndex = getPosition(firstView!!)
        }
        return offset
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        var offset = 0
        if (childCount > 0 && dx != 0) {
            offset = if (dx > 0) {
                addLinesToEndAndDeleteFromStart(dx, recycler)
            } else {
                addLinesToStartAndDeleteFromEnd(dx, recycler)
            }
            if (offset != 0) {
                for (i in mCurrentLines.indices) {
                    mCurrentLines[i].offset(-offset)
                }
                offsetChildrenHorizontal(-offset)
            }
            val firstView = getChildAt(0)
            mFirstLineStartPosition = mLayoutManagerHelper.getDecoratedStart(firstView)
            mFirstItemAdapterIndex = getPosition(firstView!!)
        }
        return offset
    }

    override fun canScrollVertically(): Boolean {
        return mOrientation == VERTICAL
    }

    override fun canScrollHorizontally(): Boolean {
        return mOrientation == HORIZONTAL
    }

    override fun scrollToPosition(position: Int) {
        if (position >= 0 && position <= itemCount - 1) {
            mFirstItemAdapterIndex = position
            mFirstLineStartPosition = -1
            requestLayout()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val linearSmoothScroller = LinearSmoothScroller(recyclerView.context)
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }
        val firstChildPos = getPosition(getChildAt(0)!!)
        val direction = if (targetPosition < firstChildPos) -1 else 1
        return if (mOrientation == HORIZONTAL) {
            PointF(direction.toFloat(), 0f)
        } else {
            PointF(0f, direction.toFloat())
        }
    }

    /**
     * Representation of line in RecyclerView.
     */
    private class Line {
        var mStartValueOfTheHighestItem = 0
        var mEndValueOfTheHighestItem = 0
        var mItemsCount = 0
        fun offset(offset: Int) {
            mStartValueOfTheHighestItem += offset
            mEndValueOfTheHighestItem += offset
        }
    }

    /**
     * Orientation and gravity helper.
     */
    private abstract class FLMLayoutManagerHelper private constructor(
        var mLayoutManager: RecyclerView.LayoutManager,
        var mGravity: Int,
    ) {
        fun setGravity(gravity: Int) {
            mGravity = gravity
        }

        abstract val end: Int
        abstract val endPadding: Int
        abstract val lineSize: Int
        abstract val endAfterPadding: Int
        abstract val startAfterPadding: Int
        abstract fun getDecoratedStart(view: View?): Int
        abstract fun getDecoratedMeasurement(view: View?): Int
        abstract fun getDecoratedMeasurementInOther(view: View?): Int
        abstract fun getStartPositionOfFirstItem(itemsSize: Int): Int
        abstract fun getPositionOfCurrentItem(itemMaxSize: Int, itemSize: Int): Int

        companion object {
            fun createLayoutManagerHelper(
                layoutManager: RecyclerView.LayoutManager,
                orientation: Int,
                gravity: Int,
            ): FLMLayoutManagerHelper {
                return when (orientation) {
                    VERTICAL -> createVerticalLayoutManagerHelper(
                        layoutManager,
                        gravity
                    )
                    HORIZONTAL -> createHorizontalLayoutManagerHelper(
                        layoutManager,
                        gravity
                    )
                    else -> throw IllegalArgumentException(ERROR_UNKNOWN_ORIENTATION)
                }
            }

            private fun createVerticalLayoutManagerHelper(
                layoutManager: RecyclerView.LayoutManager,
                gravity: Int,
            ): FLMLayoutManagerHelper {
                return object : FLMLayoutManagerHelper(layoutManager, gravity) {
                    override val end: Int
                        get() = mLayoutManager.height
                    override val endPadding: Int
                        get() = mLayoutManager.paddingBottom
                    override val lineSize: Int
                        get() = mLayoutManager.width - mLayoutManager.paddingLeft - mLayoutManager.paddingRight
                    override val endAfterPadding: Int
                        get() = mLayoutManager.height - mLayoutManager.paddingBottom
                    override val startAfterPadding: Int
                        get() = mLayoutManager.paddingTop

                    override fun getDecoratedStart(view: View?): Int {
                        val params = view!!.layoutParams as RecyclerView.LayoutParams
                        return mLayoutManager.getDecoratedTop(view) - params.topMargin
                    }

                    override fun getDecoratedMeasurement(view: View?): Int {
                        val params = view!!.layoutParams as RecyclerView.LayoutParams
                        return mLayoutManager.getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin
                    }

                    override fun getDecoratedMeasurementInOther(view: View?): Int {
                        val params = view!!.layoutParams as RecyclerView.LayoutParams
                        return mLayoutManager.getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin
                    }

                    override fun getStartPositionOfFirstItem(itemsSize: Int): Int {
                        val horizontalGravity = GravityCompat.getAbsoluteGravity(mGravity, mLayoutManager.layoutDirection)
                        val startPosition: Int
                        startPosition = when (horizontalGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                            Gravity.RIGHT -> mLayoutManager.width - mLayoutManager.paddingRight - itemsSize
                            Gravity.CENTER_HORIZONTAL -> ((mLayoutManager.width - mLayoutManager.paddingLeft - mLayoutManager.paddingRight) / 2
                                    - itemsSize / 2)
                            else -> mLayoutManager.paddingLeft
                        }
                        return startPosition
                    }

                    override fun getPositionOfCurrentItem(itemMaxSize: Int, itemSize: Int): Int {
                        val verticalGravity = mGravity and Gravity.VERTICAL_GRAVITY_MASK
                        val currentPosition: Int
                        currentPosition = when (verticalGravity) {
                            Gravity.CENTER_VERTICAL -> itemMaxSize / 2 - itemSize / 2
                            Gravity.BOTTOM -> itemMaxSize - itemSize
                            else -> 0
                        }
                        return currentPosition
                    }
                }
            }

            private fun createHorizontalLayoutManagerHelper(
                layoutManager: RecyclerView.LayoutManager,
                gravity: Int,
            ): FLMLayoutManagerHelper {
                return object : FLMLayoutManagerHelper(layoutManager, gravity) {
                    override val end: Int
                        get() = mLayoutManager.width
                    override val endPadding: Int
                        get() = mLayoutManager.paddingRight
                    override val lineSize: Int
                        get() = mLayoutManager.height - mLayoutManager.paddingTop - mLayoutManager.paddingBottom
                    override val endAfterPadding: Int
                        get() = mLayoutManager.width - mLayoutManager.paddingRight
                    override val startAfterPadding: Int
                        get() = mLayoutManager.paddingLeft

                    override fun getDecoratedStart(view: View?): Int {
                        val params = view!!.layoutParams as RecyclerView.LayoutParams
                        return mLayoutManager.getDecoratedLeft(view) - params.leftMargin
                    }

                    override fun getDecoratedMeasurement(view: View?): Int {
                        return mLayoutManager.getDecoratedMeasuredWidth(view!!)
                    }

                    override fun getDecoratedMeasurementInOther(view: View?): Int {
                        return mLayoutManager.getDecoratedMeasuredHeight(view!!)
                    }

                    override fun getStartPositionOfFirstItem(itemsSize: Int): Int {
                        val verticalGravity = mGravity and Gravity.VERTICAL_GRAVITY_MASK
                        val startPosition: Int
                        startPosition = when (verticalGravity) {
                            Gravity.CENTER_VERTICAL -> ((mLayoutManager.height - mLayoutManager.paddingTop - mLayoutManager.paddingBottom) / 2
                                    - itemsSize / 2)
                            Gravity.BOTTOM -> mLayoutManager.height - mLayoutManager.paddingBottom - itemsSize
                            else -> mLayoutManager.paddingTop
                        }
                        return startPosition
                    }

                    override fun getPositionOfCurrentItem(itemMaxSize: Int, itemSize: Int): Int {
                        val horizontalGravity = GravityCompat.getAbsoluteGravity(mGravity, mLayoutManager.layoutDirection)
                        val currentPosition: Int
                        currentPosition = when (horizontalGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                            Gravity.CENTER_HORIZONTAL -> itemMaxSize / 2 - itemSize / 2
                            Gravity.RIGHT -> itemMaxSize - itemSize
                            else -> 0
                        }
                        return currentPosition
                    }
                }
            }
        }
    }

    interface Listeners {
        fun onFirstLineDrawnWhileScrollingUp()
    }

    companion object {
        const val VERTICAL = OrientationHelper.VERTICAL
        const val HORIZONTAL = OrientationHelper.HORIZONTAL
        const val DEFAULT_COUNT_ITEM_IN_LINE = -1
        private const val TAG_FIRST_ITEM_ADAPTER_INDEX = "TAG_FIRST_ITEM_ADAPTER_INDEX"
        private const val TAG_FIRST_LINE_START_POSITION = "TAG_FIRST_LINE_START_POSITION"
        private const val ERROR_UNKNOWN_ORIENTATION = "Unknown orientation!"
        private const val ERROR_BAD_ARGUMENT = "Inappropriate field value!"
    }
}