package com.github.rooneyandshadows.lightbulb.dialogs.base

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


class DialogRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr) {
    private var motionEventListeners: MutableList<MotionEventListener> = mutableListOf()

    fun addMotionEventListener(listener: MotionEventListener) {
        motionEventListeners.add(listener)
    }

    @Override
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionEventListeners.forEach {
            it.onMotionEvent(event)
        }
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return false
    }

    @Override
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    interface MotionEventListener {
        fun onMotionEvent(event: MotionEvent)
    }
}