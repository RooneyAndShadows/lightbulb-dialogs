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
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialog.Time
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_time.TimePickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogTimePickerBinding
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultNegativeButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultNegativeButtonText
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultPositiveButtonClickListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultPositiveButtonText
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getDefaultSelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getHomeDrawable

@FragmentScreen(screenName = "Time", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_time_picker", hasLeftDrawer = true)
class FragmentDialogTimePicker : BaseFragmentWithViewBinding<FragmentDemoDialogTimePickerBinding>() {
    private lateinit var timePickerDialog: TimePickerDialog

    companion object {
        private const val DIALOG_TAG = "DIALOG_TIME_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "DIALOG_TIME_PICKER_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_time_title)
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
        outState.putParcelable(DIALOG_STATE_TAG, timePickerDialog.saveDialogState())
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogTimePickerBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogTimePicker)
            dialog = timePickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogTimePicker)
            dialog = timePickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = timePickerDialog
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        val ctx = requireContext()
        val positiveButtonText = getDefaultPositiveButtonText(ctx)
        val negativeButtonText = getDefaultNegativeButtonText(ctx)
        val positiveButtonClickListener = getDefaultPositiveButtonClickListener()
        val negativeButtonClickListener = getDefaultNegativeButtonClickListener()
        val onSelectionChanged = getDefaultSelectionChangedListener<Time>(ctx)
        timePickerDialog = TimePickerDialogBuilder(
            DIALOG_TAG,
            this,
            dialogSavedState,
        ).apply {
            withButton(cancelSelectionButton(negativeButtonText, negativeButtonClickListener))
            withButton(confirmSelectionButton(positiveButtonText, positiveButtonClickListener))
            withOnDateSelectedEvent(onSelectionChanged)
        }.buildDialog()
    }
}