package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.InteractionUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerAdapter
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogIconPickerBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogAnimationTypeSpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.DialogTypeSpinner
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.AppIconUtils
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterSelectableModes.SELECT_SINGLE

@FragmentScreen(screenName = "IconPicker", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_icon_picker", hasLeftDrawer = true)
class FragmentDialogIconPicker : BaseFragmentWithViewDataBinding<FragmentDemoDialogIconPickerBinding>() {
    private lateinit var dialog: IconPickerDialog
    private lateinit var adapter: IconPickerAdapter

    companion object {
        private const val DIALOG_TAG = "COLOR_PICKER_TAG"
    }

    @Override
    override fun doOnCreate(savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState)
        adapter = IconPickerAdapter(requireContext(), SELECT_SINGLE).apply {
            if (savedInstanceState == null)
                setCollection(AppIconUtils.allForPicker)
        }
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
    override fun onViewBound(viewBinding: FragmentDemoDialogIconPickerBinding) {
        val typeSpinner = viewBinding.dialogTypeDropdown
        val animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        createDialog(typeSpinner, animationTypeSpinner)
        typeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogIconPicker)
            this.animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        animationTypeSpinner.apply {
            setLifecycleOwner(this@FragmentDialogIconPicker)
            this.typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = dialog
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_icon_title)
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
        val onSelectionChanged = object : BasePickerDialogFragment.SelectionChangedListener<IntArray?> {
            override fun onSelectionChanged(oldValue: IntArray?, newValue: IntArray?) {
                //TODO write logic
            }
        }
        dialog = IconPickerDialogBuilder(this, childFragmentManager, DIALOG_TAG, adapter).apply {
            withTitle(title)
            withMessage(message)
            withPositiveButton(DialogButtonConfiguration(positiveText), onPositiveButtonClick)
            withNegativeButton(DialogButtonConfiguration(negativeText), onNegativeButtonClick)
            withSelectionCallback(onSelectionChanged)
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