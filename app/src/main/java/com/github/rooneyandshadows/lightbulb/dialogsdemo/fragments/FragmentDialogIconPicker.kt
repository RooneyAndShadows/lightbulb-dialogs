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
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.IconPickerDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconModel
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconSet.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogIconPickerBinding

@FragmentScreen(screenName = "IconPicker", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_icon_picker", hasLeftDrawer = true)
class FragmentDialogIconPicker : BaseFragmentWithViewBinding<FragmentDemoDialogIconPickerBinding>() {
    private lateinit var iconPickerDialog: IconPickerDialog

    companion object {
        private const val DIALOG_TAG = "ICON_PICKER_TAG"
        private const val DIALOG_STATE_TAG = "ICON_PICKER_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_icon_title)
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
        if (setInitialValues) iconPickerDialog.setData(IconModel.getAllForSet(FONTAWESOME))
    }

    @Override
    override fun doOnSaveInstanceState(outState: Bundle) {
        super.doOnSaveInstanceState(outState)
        outState.putParcelable(DIALOG_STATE_TAG, iconPickerDialog.saveDialogState())
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogIconPickerBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogIconPicker)
            dialog = iconPickerDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogIconPicker)
            dialog = iconPickerDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = iconPickerDialog
    }


    private fun createDialog(dialogSavedState: Bundle?) {
        iconPickerDialog = IconPickerDialogBuilder(
            DIALOG_TAG,
            this,
            dialogSavedState,
        ).apply {
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
            withButton(cancelSelectionButton(positiveButtonText, negativeButtonClickListener))
            withButton(confirmSelectionButton(negativeButtonText, positiveButtonClickListener))
            withSelectionCallback(onSelectionChanged)
        }.buildDialog()
    }
}