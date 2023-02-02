package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
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

@FragmentScreen(screenName = "Loading", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_loading", hasLeftDrawer = true)
class FragmentDialogLoading : BaseFragmentWithViewBinding<FragmentDemoDialogLoadingBinding>() {
    private lateinit var loadingDialog: LoadingDialog

    companion object {
        private const val DIALOG_TAG = "LOADING_DIALOG_TAG"
        private const val DIALOG_STATE_TAG = "LOADING_DIALOG_STATE_TAG"
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

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_loading_title)
        val subTitle = ResourceUtils.getPhrase(requireContext(), R.string.app_name)
        return ActionBarConfiguration(R.id.toolbar)
            .withActionButtons(true)
            .attachToDrawer(true)
            .withTitle(title)
            .withSubTitle(subTitle)
    }

    @Override
    override fun doOnViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        setupDrawerButton()
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        loadingDialog = LoadingDialogBuilder(this, childFragmentManager, DIALOG_TAG).apply {
            val ctx = requireContext()
            val title = getDefaultDialogTitle(ctx)
            val message = getDefaultDialogMessage(ctx)
            val onShowListener = object : DialogShowListener {
                override fun doOnShow(dialogFragment: BaseDialogFragment) {
                    requireView().postDelayed({
                        dialogFragment.dismiss()
                        val toastMessage = ResourceUtils.getPhrase(ctx, R.string.demo_action_completed_text)
                        InteractionUtils.showMessage(ctx, toastMessage)
                    }, 3000)
                }
            }
            withInitialDialogState(dialogSavedState)
            withTitle(title)
            withMessage(message)
            withOnShowListener(onShowListener)
        }.buildDialog()
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}