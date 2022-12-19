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

class CustomDialog : BaseDialogFragment() {
    private var loadingIndicator: ProgressBar? = null
    private var titleView: AppCompatTextView? = null
    private var dialogInflater: CustomDialogInflater? = null
    private var loading = false
    override fun setDialogLayout(inflater: LayoutInflater?): View {
        val view = View.inflate(context, R.layout.dialog_custom, null)
        val contentContainer = view.findViewById<LinearLayoutCompat>(R.id.customDialogContentContainer)
        contentContainer.removeAllViews()
        if (dialogType == DialogTypes.FULLSCREEN) contentContainer.layoutParams.height = 0
        if (dialogInflater != null) contentContainer.addView(dialogInflater!!.inflateView(this, inflater))
        return view
    }

    override fun doOnCreate(dialogArguments: Bundle?, savedInstanceState: Bundle?) {
        loading = savedInstanceState?.getBoolean(IS_LOADING_KEY)
            ?: dialogArguments!!.getBoolean(IS_LOADING_KEY)
    }

    override fun configureContent(view: View?, savedInstanceState: Bundle?) {
        selectViews()
        setupLoadingView()
    }

    override fun doOnSaveInstanceState(outState: Bundle?) {
        super.doOnSaveInstanceState(outState)
        outState!!.putBoolean(IS_LOADING_KEY, loading)
    }

    fun setLoading(isLoading: Boolean) {
        loading = isLoading
        setupLoadingView()
    }

    fun setDialogInflater(dialogInflater: CustomDialogInflater?) {
        this.dialogInflater = dialogInflater
    }

    interface CustomDialogInflater {
        fun inflateView(dialog: CustomDialog?, layoutInflater: LayoutInflater?): View?
    }

    private fun selectViews() {
        if (view == null) return
        loadingIndicator = view!!.findViewById(R.id.loadingIndicator)
        titleView = view!!.findViewById(R.id.title)
    }

    private fun setupLoadingView() {
        if (loadingIndicator == null) return
        loadingIndicator!!.visibility = if (loading) View.VISIBLE else View.GONE
        val endPadding =
            if (loading) ResourceUtils.getDimenPxById(loadingIndicator!!.context, R.dimen.dialog_spacing_size_small) else 0
        titleView!!.setPadding(titleView!!.paddingLeft, titleView!!.paddingTop, endPadding, titleView!!.paddingBottom)
    }

    companion object {
        private const val IS_LOADING_KEY = "IS_LOADING_KEY"
        fun newCustomDialogInstance(
            title: String?, message: String?, positive: DialogButtonConfiguration?, negative: DialogButtonConfiguration?,
            cancelable: Boolean, loading: Boolean, dialogType: DialogTypes?, animationType: DialogAnimationTypes?
        ): CustomDialog {
            val f = CustomDialog()
            val helper = DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(dialogType ?: DialogTypes.NORMAL)
                .withAnimation(animationType ?: DialogAnimationTypes.NO_ANIMATION)
            helper.bundle.putBoolean(IS_LOADING_KEY, loading)
            f.arguments = helper.bundle
            return f
        }
    }
}