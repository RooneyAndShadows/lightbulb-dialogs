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
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogColorPickerBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.color.AppColorUtils

@FragmentScreen(screenName = "ColorPicker", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_color_picker", hasLeftDrawer = true)
class FragmentDialogColorPicker : BaseFragmentWithViewDataBinding<FragmentDemoDialogColorPickerBinding>() {
    private lateinit var colorPickerDialog: ColorPickerDialog

    companion object {
        private const val DIALOG_TAG = "COLOR_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "COLOR_PICKER_STATE_TAG"
    }

    @Override
    override fun doOnCreate(savedInstanceState: Bundle?) {
        super.doOnCreate(savedInstanceState)
        val setInitialValues = savedInstanceState == null
        var dialogSavedState: Bundle? = null
        savedInstanceState?.apply {
            dialogSavedState = BundleUtils.getParcelable(DIALOG_STATE_TAG, this, Bundle::class.java)
        }
        createDialog(dialogSavedState)
        if (setInitialValues)
            colorPickerDialog.setData(AppColorUtils.allForPicker)
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.putParcelable(DIALOG_STATE_TAG, colorPickerDialog.saveDialogState())
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogColorPickerBinding) {
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogColorPicker)
            dialog = colorPickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogColorPicker)
            dialog = colorPickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = colorPickerDialog
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_color_title)
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

    private fun createDialog(dialogSavedState: Bundle?) {
        colorPickerDialog = ColorPickerDialogBuilder(this, childFragmentManager, DIALOG_TAG).apply {
            val ctx = requireContext()
            val title = getDefaultDialogTitle(ctx)
            val message = getDefaultDialogMessage(ctx)
            val positiveButtonText = getDefaultPositiveButtonText(ctx)
            val negativeButtonText = getDefaultNegativeButtonText(ctx)
            val positiveButtonClickListener = getDefaultPositiveButtonClickListener()
            val negativeButtonClickListener = getDefaultNegativeButtonClickListener()
            val onSelectionChanged = object : SelectionChangedListener<IntArray?> {
                override fun onSelectionChanged(oldValue: IntArray?, newValue: IntArray?) {
                    //TODO write logic
                }
            }
            withSavedState(dialogSavedState)
            withTitle(title)
            withMessage(message)
            withPositiveButton(DialogButtonConfiguration(positiveButtonText), positiveButtonClickListener)
            withNegativeButton(DialogButtonConfiguration(negativeButtonText), negativeButtonClickListener)
            withSelectionCallback(onSelectionChanged)
        }.buildDialog()
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}