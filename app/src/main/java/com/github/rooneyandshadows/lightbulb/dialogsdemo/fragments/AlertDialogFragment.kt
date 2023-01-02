package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.BindView
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.BaseActivity
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragment
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogsdemo.activity.MainActivity
import com.github.rooneyandshadows.lightbulb.dialogsdemo.activity.MenuConfigurations
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.adapter.DialogPropertyAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getAllAsDialogPropertyItems
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogPropertySpinner

@FragmentScreen(screenName = "Alert", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_alert", hasLeftDrawer = true)
class AlertDialogFragment : BaseFragment() {
    @BindView(name = "dialog_type_dropdown")
    lateinit var dialogTypeSpinner: DialogPropertySpinner

    @BindView(name = "dialog_animation_type_dropdown")
    lateinit var dialogAnimationTypeSpinner: DialogPropertySpinner

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        return ActionBarConfiguration(R.id.toolbar)
            .withActionButtons(true)
            .attachToDrawer(true)
            .withSubTitle(ResourceUtils.getPhrase(requireContext(), R.string.demo_alert_title))
            .withTitle(ResourceUtils.getPhrase(requireContext(), R.string.app_name))
    }

    @Override
    override fun doOnViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        initSpinners()
        if (getFragmentState() === FragmentStates.CREATED) {
            BaseActivity.updateMenuConfiguration(
                requireContext(),
                MainActivity::class.java
            ) { activity: BaseActivity -> MenuConfigurations.getConfiguration(activity) }
        }
        setupDrawerButton()
    }

    fun initSpinners() {
        dialogTypeSpinner.apply {
            setLifecycleOwner(this@AlertDialogFragment)
            setProperties(DialogTypes.getAllAsDialogPropertyItems())
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        dialogAnimationTypeSpinner.apply {
            setLifecycleOwner(this@AlertDialogFragment)
            setProperties(DialogAnimationTypes.getAllAsDialogPropertyItems())
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}