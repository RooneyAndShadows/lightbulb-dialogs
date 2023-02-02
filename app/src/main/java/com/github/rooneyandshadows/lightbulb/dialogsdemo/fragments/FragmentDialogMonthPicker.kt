package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialog.Month
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_month.MonthPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogMonthPickerBinding

@FragmentScreen(screenName = "Month", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_month_picker", hasLeftDrawer = true)
class FragmentDialogMonthPicker : BaseFragmentWithViewBinding<FragmentDemoDialogMonthPickerBinding>() {
    private lateinit var monthPickerDialog: MonthPickerDialog

    companion object {
        private const val DIALOG_TAG = "DIALOG_MONTH_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "DIALOG_MONTH_PICKER_STATE_TAG"
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
        outState.putParcelable(DIALOG_STATE_TAG, monthPickerDialog.saveDialogState())
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogMonthPickerBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogMonthPicker)
            dialog = monthPickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogMonthPicker)
            dialog = monthPickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = monthPickerDialog
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_date_range_title)
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
        val ctx = requireContext()
        val positiveButtonText = getDefaultPositiveButtonText(ctx)
        val negativeButtonText = getDefaultNegativeButtonText(ctx)
        val positiveButtonClickListener = getDefaultPositiveButtonClickListener()
        val negativeButtonClickListener = getDefaultNegativeButtonClickListener()
        val onSelectionChanged = getDefaultSelectionChangedListener<Month>(ctx)
        monthPickerDialog = MonthPickerDialogBuilder(this, childFragmentManager, DIALOG_TAG).apply {
            withInitialDialogState(dialogSavedState)
            withPositiveButton(DialogButtonConfiguration(positiveButtonText), positiveButtonClickListener)
            withNegativeButton(DialogButtonConfiguration(negativeButtonText), negativeButtonClickListener)
            withOnDateSelectedEvent(onSelectionChanged)
        }.buildDialog()
    }

    private fun setupDrawerButton() {
        val actionBarDrawable = ShowMenuDrawable(requireContext())
        actionBarDrawable.setEnabled(false)
        actionBarDrawable.backgroundColor = ResourceUtils.getColorByAttribute(requireContext(), R.attr.colorError)
        actionBarManager.setHomeIcon(actionBarDrawable)
    }
}