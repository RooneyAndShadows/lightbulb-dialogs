package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogCustomBinding

@FragmentScreen(screenName = "Custom", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_custom", hasLeftDrawer = true)
class FragmentDialogCustom : BaseFragmentWithViewBinding<FragmentDemoDialogCustomBinding>() {
    private lateinit var customDialog: CustomDialog

    companion object {
        private const val DIALOG_TAG = "CUSTOM_DIALOG_TAG"
        private const val DIALOG_STATE_TAG = "CUSTOM_DIALOG_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_custom_title)
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
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        val dialogState = customDialog.saveDialogState()
        BundleUtils.putParcelable(DIALOG_STATE_TAG, outState, dialogState)
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogCustomBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogCustom)
            dialog = customDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogCustom)
            dialog = customDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = customDialog
    }

    @SuppressLint("InflateParams")
    private fun createDialog(dialogSavedState: Bundle?) {
        customDialog = CustomDialogBuilder(
            DIALOG_TAG,
            this,
            { CustomDialog.newCustomDialogInstance() },
            { _: CustomDialog, layoutInflater: LayoutInflater -> layoutInflater.inflate(R.layout.dialog_demo_custom, null) },
            dialogSavedState
        ).apply {
            val ctx = requireContext()
            val title = getDefaultDialogTitle(ctx)
            val message = getDefaultDialogMessage(ctx)
            withTitle(title)
            withMessage(message)
            withButton(getDefaultNegativeButton(ctx))
            withButton(getDefaultPositiveButton(ctx))
        }.buildDialog()
    }
}