package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.NavigateBackDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogAdapterPickerBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getRadioButtonAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogAnimationTypeSpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogTypeSpinner
import com.github.rooneyandshadows.lightbulb.recycleradapters.implementation.RadioButtonSelectableAdapter

@FragmentScreen(screenName = "Adapter", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_adapter_picker")
class FragmentDialogAdapterPicker : BaseFragmentWithViewDataBinding<FragmentDemoDialogAdapterPickerBinding>() {
    private lateinit var adapterPickerDialog: AdapterPickerDialog<DemoModel>
    private lateinit var adapter: RadioButtonSelectableAdapter<DemoModel>

    companion object {
        private const val DIALOG_TAG = "ADAPTER_PICKER_DIALOG_TAG"
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogAdapterPickerBinding) {
        val typeSpinner = viewBinding.dialogTypeDropdown
        val animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        createDialog(typeSpinner, animationTypeSpinner)
        typeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogAdapterPicker)
            this.animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        animationTypeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogAdapterPicker)
            this.typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = adapterPickerDialog
    }

    @Override
    override fun doOnCreate(savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState)
        adapter = getRadioButtonAdapter(requireContext())
        savedInstanceState?.apply {
            val adapterState = BundleUtils.getParcelable("ADAPTER_STATE", this, Bundle::class.java)!!
            adapter.restoreAdapterState(adapterState)
        }
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.putParcelable("ADAPTER_STATE", adapter.saveAdapterState())
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_adapter_title)
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
        val selectionCallback = object : SelectionChangedListener<IntArray?> {
            override fun onSelectionChanged(oldValue: IntArray?, newValue: IntArray?) {
                //TODO write logic
            }
        }
        adapterPickerDialog = AdapterPickerDialogBuilder(
            this,
            childFragmentManager,
            DIALOG_TAG,
            getRadioButtonAdapter(ctx)
        ).apply {
            withTitle(title)
            withMessage(message)
            withPositiveButton(DialogButtonConfiguration(positiveText), onPositiveButtonClick)
            withNegativeButton(DialogButtonConfiguration(negativeText), onNegativeButtonClick)
            withSelectionCallback(selectionCallback)
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