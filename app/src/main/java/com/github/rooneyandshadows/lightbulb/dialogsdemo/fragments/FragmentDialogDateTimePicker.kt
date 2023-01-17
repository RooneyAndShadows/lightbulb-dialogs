package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.drawable.ShowMenuDrawable
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewDataBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment.SelectionChangedListener
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime.DateTimePickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_datetime.DateTimePickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogDateTimePickerBinding
import java.time.OffsetDateTime

@FragmentScreen(screenName = "DateTime", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_date_time_picker", hasLeftDrawer = true)
class FragmentDialogDateTimePicker : BaseFragmentWithViewDataBinding<FragmentDemoDialogDateTimePickerBinding>() {
    private lateinit var dateTimePickerDialog: DateTimePickerDialog

    companion object {
        private const val DIALOG_TAG = "DIALOG_DATE_TIME_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "DIALOG_DATE_TIME_PICKER_STATE_TAG"
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
        outState.putParcelable(DIALOG_STATE_TAG, dateTimePickerDialog.saveDialogState())
    }

    @Override
    override fun onViewBound(viewBinding: FragmentDemoDialogDateTimePickerBinding) {
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogDateTimePicker)
            dialog = dateTimePickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogDateTimePicker)
            dialog = dateTimePickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = dateTimePickerDialog
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
        val onSelectionChanged = object : SelectionChangedListener<OffsetDateTime?> {
            override fun onSelectionChanged(oldValue: OffsetDateTime?, newValue: OffsetDateTime?) {
                //TODO write logic
            }
        }
        dateTimePickerDialog = DateTimePickerDialogBuilder(this, childFragmentManager, DIALOG_TAG).apply {
            withSavedState(dialogSavedState)
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