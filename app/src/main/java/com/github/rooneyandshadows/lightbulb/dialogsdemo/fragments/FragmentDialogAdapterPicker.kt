package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.NavigateBackDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialogBuilder.AdapterPickerDialogInitializer
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogAdapterPickerBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.dialogs.DemoSingleSelectionDialog
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultNegativeButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultPositiveButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultSelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel

@FragmentScreen(screenName = "Adapter", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_adapter_picker")
class FragmentDialogAdapterPicker : BaseFragmentWithViewDataBinding<FragmentDemoDialogAdapterPickerBinding>() {
    private lateinit var adapterPickerDialog: DemoSingleSelectionDialog

    companion object {
        private const val DIALOG_TAG = "ADAPTER_PICKER_DIALOG_TAG"
        private const val DIALOG_STATE_TAG = "ADAPTER_PICKER_DIALOG_STATE"
    }

    @Override
    override fun doOnCreate(savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState)
        createDialog(savedInstanceState)
        if (savedInstanceState == null)
            adapterPickerDialog.setData(DemoModel.generateDemoCollection())
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogAdapterPickerBinding) {
        val typeSpinner = viewBinding.dialogTypeDropdown
        val animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
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
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        BundleUtils.putParcelable(DIALOG_STATE_TAG, outState, adapterPickerDialog.saveDialogState())
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

    private fun createDialog(savedInstanceState: Bundle?) {
        val ctx = requireContext()
        val title = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_title_text)
        val message = ResourceUtils.getPhrase(ctx, R.string.demo_dialog_default_message_text)
        val positiveText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_default_positive_button)
        val negativeText = ResourceUtils.getPhrase(requireContext(), R.string.demo_dialog_default_negative_button)
        val onPositiveButtonClick = getDefaultPositiveButtonClickListener()
        val onNegativeButtonClick = getDefaultNegativeButtonClickListener()
        val selectionCallback = getDefaultSelectionChangedListener<IntArray?>(ctx)
        adapterPickerDialog = AdapterPickerDialogBuilder(
            this,
            childFragmentManager,
            DIALOG_TAG,
            object : AdapterPickerDialogInitializer<DemoSingleSelectionDialog> {
                @Override
                override fun initialize(): DemoSingleSelectionDialog {
                    return DemoSingleSelectionDialog.newInstance()
                }
            }
        ).apply {
            withTitle(title)
            withMessage(message)
            withPositiveButton(DialogButtonConfiguration(positiveText), onPositiveButtonClick)
            withNegativeButton(DialogButtonConfiguration(negativeText), onNegativeButtonClick)
            withSelectionCallback(selectionCallback)
        }.buildDialog().apply {
            if (savedInstanceState == null) return@apply
            val savedState = BundleUtils.getParcelable(DIALOG_STATE_TAG, savedInstanceState, Bundle::class.java)
            restoreDialogState(savedState)
        }
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = NavigateBackDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}