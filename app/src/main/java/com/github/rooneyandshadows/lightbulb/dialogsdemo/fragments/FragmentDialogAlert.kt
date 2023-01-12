package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogAlertBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogAnimationTypeSpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogTypeSpinner

@FragmentScreen(screenName = "Alert", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_alert", hasLeftDrawer = true)
class FragmentDialogAlert : BaseFragmentWithViewDataBinding<FragmentDemoDialogAlertBinding>() {
    private lateinit var alertDialog: AlertDialog

    companion object {
        private const val DIALOG_TAG = "ALERT_DIALOG_TAG"
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogAlertBinding) {
        val typeSpinner = viewBinding.dialogTypeDropdown
        val animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        createDialog(typeSpinner, animationTypeSpinner)
        typeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogAlert)
            this.animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        animationTypeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogAlert)
            this.typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = alertDialog
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_alert_title)
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

    private fun createDialog(typeSpinner: DialogTypeSpinner, animationSpinner: DialogAnimationTypeSpinner) {
        val ctx = requireContext()
        val title = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_title_text)
        val message = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_message_text)
        val positiveText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_default_positive_button)
        val negativeText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_default_negative_button)
        val onPositiveButtonClick = object : DialogButtonClickListener {
            override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                val toastMessage = ResourceUtils.getPhrase(ctx, R.string.demo_positive_button_clicked_text)
                InteractionUtils.showMessage(ctx, toastMessage)
            }
        }
        val onNegativeButtonClick = object : DialogButtonClickListener {
            override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                val toastMessage = ResourceUtils.getPhrase(ctx, R.string.demo_negative_button_clicked_text)
                InteractionUtils.showMessage(ctx, toastMessage)
            }
        }
        alertDialog = AlertDialogBuilder(this, childFragmentManager, DIALOG_TAG).apply {
            withTitle(title)
            withMessage(message)
            withPositiveButton(DialogButtonConfiguration(positiveText), onPositiveButtonClick)
            withNegativeButton(DialogButtonConfiguration(negativeText), onNegativeButtonClick)
        }.buildDialog().apply {
            typeSpinner.dialog = this
            animationSpinner.dialog = this
        }
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}