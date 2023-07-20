package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogAlertBinding

@FragmentScreen(screenName = "Alert", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_alert", hasLeftDrawer = true)
class FragmentDialogAlert : BaseFragmentWithViewBinding<FragmentDemoDialogAlertBinding>() {
    private lateinit var alertDialog: AlertDialog

    companion object {
        private const val DIALOG_TAG = "ALERT_DIALOG_TAG"
        private const val DIALOG_STATE_TAG = "ALERT_DIALOG_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_alert_title)
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
        val dialogState = alertDialog.saveDialogState()
        BundleUtils.putParcelable(DIALOG_STATE_TAG, outState, dialogState)
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogAlertBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogAlert)
            dialog = alertDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogAlert)
            dialog = alertDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = alertDialog
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        alertDialog = AlertDialogBuilder(
            DIALOG_TAG,
            this,
            dialogSavedState
        ).apply {
            val ctx = requireContext()
            withTitle(getDefaultDialogTitle(ctx))
            withMessage(getDefaultDialogMessage(ctx))
            withButton(getDefaultNegativeButton(ctx))
            withButton(getDefaultPositiveButton(ctx))
        }.buildDialog()
    }
}