package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.Buttons.Companion.cancelSelectionButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.Buttons.Companion.confirmSelectionButton
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButton
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialog.DateRange
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_date_range.DateRangePickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogDateRangePickerBinding

@FragmentScreen(screenName = "DateRange", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_date_range_picker", hasLeftDrawer = true)
class FragmentDialogDateRangePicker : BaseFragmentWithViewBinding<FragmentDemoDialogDateRangePickerBinding>() {
    private lateinit var dateRangePickerDialog: DateRangePickerDialog

    companion object {
        private const val DIALOG_TAG = "DIALOG_DATE_RANGE_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "DIALOG_DATE_RANGE_PICKER_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_date_range_title)
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
        outState.putParcelable(DIALOG_STATE_TAG, dateRangePickerDialog.saveDialogState())
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogDateRangePickerBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogDateRangePicker)
            dialog = dateRangePickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogDateRangePicker)
            dialog = dateRangePickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = dateRangePickerDialog
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        val ctx = requireContext()
        val positiveButtonText = getDefaultPositiveButtonText(ctx)
        val negativeButtonText = getDefaultNegativeButtonText(ctx)
        val positiveButtonClickListener = getDefaultPositiveButtonClickListener()
        val negativeButtonClickListener = getDefaultNegativeButtonClickListener()
        val onSelectionChanged = getDefaultSelectionChangedListener<DateRange>(ctx)
        dateRangePickerDialog = DateRangePickerDialogBuilder(
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