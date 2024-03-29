package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogLoadingBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultDialogMessage
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultDialogTitle
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getHomeDrawable

@FragmentScreen(screenName = "Loading", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_loading", hasLeftDrawer = true)
class FragmentDialogLoading : BaseFragmentWithViewBinding<FragmentDemoDialogLoadingBinding>() {
    private lateinit var loadingDialog: LoadingDialog

    companion object {
        private const val DIALOG_TAG = "LOADING_DIALOG_TAG"
        private const val DIALOG_STATE_TAG = "LOADING_DIALOG_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_loading_title)
        val subTitle = ResourceUtils.getPhrase(requireContext(), R.string.app_name)
        val homeIcon = getHomeDrawable(requireContext())
        return ActionBarConfiguration(R.id.toolbar)
            .withActionButtons(true)
            .withHomeIcon(homeIcon)
            .withTitle(title)
            .withSubTitle(subTitle)
    }

    @Override
    override fun doOnCreate(savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState)
        var dialogSavedState: Bundle? = null
        savedInstanceState?.apply {
            dialogSavedState = BundleUtils.getParcelable(DIALOG_STATE_TAG, this, Bundle::class.java)
        }
        createDialog(dialogSavedState)
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogLoadingBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogLoading)
            dialog = loadingDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogLoading)
            dialog = loadingDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = loadingDialog
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        loadingDialog = LoadingDialogBuilder(
            DIALOG_TAG,
            this,
            dialogSavedState,
        ).apply {
            val ctx = requireContext()
            val title = getDefaultDialogTitle(ctx)
            val message = getDefaultDialogMessage(ctx)
            val onShowListener = DialogShowListener { dialogFragment ->
                requireView().postDelayed({
                    dialogFragment.dismiss()
                    val toastMessage = ResourceUtils.getPhrase(ctx, R.string.demo_action_completed_text)
                    InteractionUtils.showMessage(ctx, toastMessage)
                }, 3000)
            }
            withTitle(title)
            withMessage(message)
            withOnShowListener(onShowListener)
        }.buildDialog()
    }
}