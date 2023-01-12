package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.NavigateBackDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogShowListener
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_loading.LoadingDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogLoadingBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogAnimationTypeSpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogTypeSpinner

@FragmentScreen(screenName = "Loading", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_loading")
class FragmentDialogLoading : BaseFragmentWithViewDataBinding<FragmentDemoDialogLoadingBinding>() {
    private lateinit var loadingDialog: LoadingDialog

    companion object {
        private const val DIALOG_TAG = "LOADING_DIALOG_TAG"
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogLoadingBinding) {
        val typeSpinner = viewBinding.dialogTypeDropdown
        val animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        createDialog(typeSpinner, animationTypeSpinner)
        typeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogLoading)
            this.animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        animationTypeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogLoading)
            this.typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = loadingDialog
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_loading_title)
        val subTitle = ResourceUtils.getPhrase(requireContext(), R.string.app_name)
        return ActionBarConfiguration(R.id.toolbar)
            .withActionButtons(true)
            .withTitle(title)
            .withSubTitle(subTitle)
    }

    @Override
    override fun doOnViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        setupDrawerButton()
    }

    private fun createDialog(typeSpinner: DialogTypeSpinner, animationSpinner: DialogAnimationTypeSpinner) {
        val ctx = requireContext()
        val title = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_title_text)
        val message = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_message_text)
        val onShowListener = object : DialogShowListener {
            override fun doOnShow(dialogFragment: BaseDialogFragment) {
                requireView().postDelayed({
                    dialogFragment.dismiss()
                    val toastMessage = ResourceUtils.getPhrase(ctx, R.string.demo_action_completed_text)
                    InteractionUtils.showMessage(ctx, toastMessage)
                }, 3000)
            }
        }
        loadingDialog = LoadingDialogBuilder(this, childFragmentManager, DIALOG_TAG).apply {
            withTitle(title)
            withMessage(message)
            withOnShowListener(onShowListener)
        }.buildDialog().apply {
            typeSpinner.dialog = this
            animationSpinner.dialog = this
        }
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = NavigateBackDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}