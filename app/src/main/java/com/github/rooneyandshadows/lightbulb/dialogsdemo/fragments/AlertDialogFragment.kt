package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.BaseActivity
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert.AlertDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.activity.MainActivity
import com.github.rooneyandshadows.lightbulb.dialogsdemo.activity.MenuConfigurations
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogAlertBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogAnimationTypeSpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogTypeSpinner

@FragmentScreen(screenName = "Alert", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_alert", hasLeftDrawer = true)
class AlertDialogFragment : BaseFragmentWithViewDataBinding<FragmentDemoDialogAlertBinding>() {
    private lateinit var alertDialog: AlertDialog

    companion object {
        private const val DIALOG_TAG = "ALERT_DIALOG_TAG"
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogAlertBinding) {
        val typeSpinner = viewBinding.dialogTypeDropdown
        val animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        initializeDialog(typeSpinner, animationTypeSpinner)
        typeSpinner.apply {
            setLifecycleOwner(this@AlertDialogFragment)
            this.animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        animationTypeSpinner.apply {
            setLifecycleOwner(this@AlertDialogFragment)
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
        if (getFragmentState() === FragmentStates.CREATED) {
            BaseActivity.updateMenuConfiguration(
                requireContext(),
                MainActivity::class.java
            ) { activity: BaseActivity -> MenuConfigurations.getConfiguration(activity) }
        }
        setupDrawerButton()
    }

    fun initializeDialog(typeSpinner: DialogTypeSpinner, animationTypeSpinner: DialogAnimationTypeSpinner) {
        val ctx = requireContext()
        val title = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_title_text)
        val message = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_message_text)
        val positiveText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_positive_button)
        val negativeText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_negative_button)
        alertDialog = AlertDialogBuilder(this, childFragmentManager, DIALOG_TAG)
            .withTitle(title)
            .withMessage(message)
            .withPositiveButton(DialogButtonConfiguration(positiveText), object : DialogButtonClickListener {
                override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {}
            })
            .withNegativeButton(DialogButtonConfiguration(negativeText), object : DialogButtonClickListener {
                override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {}
            })
            .buildDialog().apply {
                typeSpinner.dialog = this
                animationTypeSpinner.dialog = this
            }
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}