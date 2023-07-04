package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import androidx.appcompat.widget.LinearLayoutCompat
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*

@Suppress("unused")
open class CustomDialog : BaseDialogFragment() {
    private var loadingIndicator: ProgressBar? = null
    private var dialogInflater: CustomDialogInflater? = null
    var isLoading = false
        private set

    companion object {
        private const val IS_LOADING_KEY = "IS_LOADING_KEY"

        @JvmStatic
        fun newCustomDialogInstance(): CustomDialog {
            return CustomDialog()
        }
    }

    @Override
    final override fun getDialogLayout(layoutInflater: LayoutInflater): View {
        val view = View.inflate(context, R.layout.dialog_custom, null)
        val contentContainer = view.findViewById<LinearLayoutCompat>(R.id.customDialogContentContainer)
        contentContainer.removeAllViews()
        if (dialogType == DialogTypes.FULLSCREEN) contentContainer.layoutParams.height = 0
        dialogInflater?.apply {
            val content = inflateView(this@CustomDialog, layoutInflater)
            contentContainer.addView(content)
        }
        return view
    }

    @Override
    override fun setupDialogContent(view: View, savedInstanceState: Bundle?) {
        loadingIndicator = requireView().findViewById(R.id.loadingIndicator)
        setupLoadingView()
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
        //val params = ViewGroup.LayoutParams(0, 0)
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec)
        val horPadding = fgPadding.left + fgPadding.right
        val verPadding = fgPadding.top + fgPadding.bottom
        var desiredWidth = dialogLayout.measuredWidth
        var desiredHeight = dialogLayout.measuredHeight
        desiredWidth = constraints.resolveWidth(desiredWidth)
        desiredHeight = constraints.resolveHeight(desiredHeight)
        dialogWindow.setLayout(desiredWidth + horPadding, desiredHeight + verPadding)
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.apply {
            putBoolean(IS_LOADING_KEY, isLoading)
        }
    }

    @Override
    override fun doOnRestoreInstanceState(savedState: Bundle) {
        super.doOnRestoreInstanceState(savedState)
        savedState.apply {
            setLoading(getBoolean(IS_LOADING_KEY))
        }
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        setupLoadingView()
    }

    fun setDialogInflater(dialogInflater: CustomDialogInflater?) {
        this.dialogInflater = dialogInflater
    }

    interface CustomDialogInflater {
        fun inflateView(dialog: CustomDialog, layoutInflater: LayoutInflater): View?
    }

    private fun setupLoadingView() {
        loadingIndicator?.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}