package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.BindView
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
import com.github.rooneyandshadows.lightbulb.textinputview.TextInputView
import com.google.android.material.button.MaterialButton

@FragmentScreen(screenName = "Alert", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_alert", hasLeftDrawer = true)
class AlertDialogFragment : BaseFragmentWithViewDataBinding<FragmentDemoDialogAlertBinding>() {
    @BindView(name = "dialog_type_dropdown")
    lateinit var dialogTypeSpinner: DialogTypeSpinner

    @BindView(name = "dialog_animation_type_dropdown")
    lateinit var dialogAnimationTypeSpinner: DialogAnimationTypeSpinner

    @BindView(name = "dialog_title_textview")
    lateinit var titleTextInputView: TextInputView

    @BindView(name = "dialog_message_textview")
    lateinit var messageTextInputView: TextInputView

    @BindView(name = "show_button")
    lateinit var showButton: MaterialButton

    private var alertDialog: AlertDialog? = null

    companion object {
        private const val DIALOG_TAG = "ALERT_DIALOG_TAG"
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
        initializeViews()
        initializeDialog()
        if (getFragmentState() === FragmentStates.CREATED) {
            BaseActivity.updateMenuConfiguration(
                requireContext(),
                MainActivity::class.java
            ) { activity: BaseActivity -> MenuConfigurations.getConfiguration(activity) }
        }
        setupDrawerButton()
    }

    fun initializeDialog() {
        val positiveText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_positive_button)
        val negativeText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_negative_button)
        alertDialog = AlertDialogBuilder(this, childFragmentManager, DIALOG_TAG)
            .withTitle(titleTextInputView.text)
            .withMessage(messageTextInputView.text)
            .withPositiveButton(DialogButtonConfiguration(positiveText), object : DialogButtonClickListener {
                override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                }
            })
            .withNegativeButton(DialogButtonConfiguration(negativeText), object : DialogButtonClickListener {
                override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {
                }
            })
            .buildDialog().apply {
                dialogTypeSpinner.dialog = this
                dialogAnimationTypeSpinner.dialog = this
            }
    }

    fun initializeViews() {
        titleTextInputView.addTextChangedCallback { new, _ -> alertDialog!!.title = new }
        messageTextInputView.addTextChangedCallback { new, _ -> alertDialog!!.message = new }
        dialogTypeSpinner.apply {
            setLifecycleOwner(this@AlertDialogFragment)
            animationTypeSpinner = dialogAnimationTypeSpinner
        }
        dialogAnimationTypeSpinner.apply {
            setLifecycleOwner(this@AlertDialogFragment)
            typeSpinner = dialogTypeSpinner
        }
        showButton.setOnClickListener {
            alertDialog!!.show()
        }
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}