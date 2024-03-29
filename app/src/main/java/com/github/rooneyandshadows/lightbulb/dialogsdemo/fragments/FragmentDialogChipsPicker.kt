package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.Buttons.Companion.cancelSelectionButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.Buttons.Companion.confirmSelectionButton
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips.ChipsPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogChipsPickerBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.generateChips
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultDialogMessage
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultDialogTitle
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultNegativeButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultNegativeButtonText
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultPositiveButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultPositiveButtonText
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultSelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getHomeDrawable

@FragmentScreen(screenName = "ChipsPicker", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_chips_picker", hasLeftDrawer = true)
class FragmentDialogChipsPicker : BaseFragmentWithViewBinding<FragmentDemoDialogChipsPickerBinding>() {
    private lateinit var chipsPickerDialog: ChipsPickerDialog

    companion object {
        private const val DIALOG_TAG = "CHIPS_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "CHIPS_PICKER_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_chips_title)
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
        val setInitialValues = savedInstanceState == null
        var dialogSavedState: Bundle? = null
        savedInstanceState?.apply {
            dialogSavedState = BundleUtils.getParcelable(DIALOG_STATE_TAG, this, Bundle::class.java)
        }
        createDialog(dialogSavedState)
        if (setInitialValues) chipsPickerDialog.setData(generateChips())
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.putParcelable(DIALOG_STATE_TAG, chipsPickerDialog.saveDialogState())
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogChipsPickerBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogChipsPicker)
            dialog = chipsPickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogChipsPicker)
            dialog = chipsPickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = chipsPickerDialog
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        chipsPickerDialog = ChipsPickerDialogBuilder(DIALOG_TAG, this, dialogSavedState).apply {
            val ctx = requireContext()
            val title = getDefaultDialogTitle(ctx)
            val message = getDefaultDialogMessage(ctx)
            val positiveButtonText = getDefaultPositiveButtonText(ctx)
            val negativeButtonText = getDefaultNegativeButtonText(ctx)
            val positiveButtonClickListener = getDefaultPositiveButtonClickListener()
            val negativeButtonClickListener = getDefaultNegativeButtonClickListener()
            val onSelectionChanged = getDefaultSelectionChangedListener<IntArray>(ctx)
            withTitle(title)
            withMessage(message)
            withButton(cancelSelectionButton(negativeButtonText, positiveButtonClickListener))
            withButton(confirmSelectionButton(positiveButtonText, negativeButtonClickListener))
            withSelectionCallback(onSelectionChanged)
        }.buildDialog()
    }
}