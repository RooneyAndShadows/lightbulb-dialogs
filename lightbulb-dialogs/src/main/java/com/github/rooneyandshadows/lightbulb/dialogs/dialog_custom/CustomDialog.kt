package com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.AppCompatTextView
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.*

@Suppress("unused")
open class CustomDialog : BaseDialogFragment() {
    private var loadingIndicator: ProgressBar? = null
    private var titleView: AppCompatTextView? = null
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
    override fun getDialogLayout(layoutInflater: LayoutInflater): View {
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
        titleView = requireView().findViewById(R.id.dialogTitleTextView)
        setupLoadingView()
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
            val endPadding = if (isLoading) ResourceUtils.getDimenPxById(context, R.dimen.dialog_spacing_size_small) else 0
            visibility = if (isLoading) View.VISIBLE else View.GONE
            titleView?.apply {
                setPadding(titleView!!.paddingLeft, titleView!!.paddingTop, endPadding, titleView!!.paddingBottom)
            }
        }
    }
}