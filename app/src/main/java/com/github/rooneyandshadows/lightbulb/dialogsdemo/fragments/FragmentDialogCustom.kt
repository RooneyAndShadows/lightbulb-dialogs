package com.github.rooneyandshadows.lightbulb.dialogsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentConfiguration
import com.github.rooneyandshadows.lightbulb.annotation_processors.annotations.FragmentScreen
import com.github.rooneyandshadows.lightbulb.application.fragment.base.BaseFragmentWithViewBinding
import com.github.rooneyandshadows.lightbulb.application.fragment.cofiguration.ActionBarConfiguration
import com.github.rooneyandshadows.lightbulb.commons.utils.BundleUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialog
import com.github.rooneyandshadows.lightbulb.dialogs.dialog_custom.CustomDialogBuilder
import com.github.rooneyandshadows.lightbulb.dialogsdemo.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.databinding.FragmentDemoDialogCustomBinding

@FragmentScreen(screenName = "Custom", screenGroup = "Demo")
@FragmentConfiguration(layoutName = "fragment_demo_dialog_custom", hasLeftDrawer = true)
class FragmentDialogCustom : BaseFragmentWithViewBinding<FragmentDemoDialogCustomBinding>() {
    private lateinit var customDialog: CustomDialog

    companion object {
        private const val DIALOG_TAG = "CUSTOM_DIALOG_TAG"
        private const val DIALOG_STATE_TAG = "CUSTOM_DIALOG_STATE_TAG"
    }

    @Override
    override fun configureActionBar(): ActionBarConfiguration {
        val title = ResourceUtils.getPhrase(requireContext(), R.string.demo_custom_title)
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
        val dialogState = customDialog.saveDialogState()
        BundleUtils.putParcelable(DIALOG_STATE_TAG, outState, dialogState)
    }

    @Override
    override fun doOnViewBound(viewBinding: FragmentDemoDialogCustomBinding, savedInstanceState: Bundle?) {
        super.doOnViewBound(viewBinding, savedInstanceState)
        viewBinding.dialogTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogCustom)
            dialog = customDialog
            animationTypeSpinner = viewBinding.dialogAnimationTypeDropdown
        }
        viewBinding.dialogAnimationTypeDropdown.apply {
            setLifecycleOwner(this@FragmentDialogCustom)
            dialog = customDialog
            typeSpinner = viewBinding.dialogTypeDropdown
        }
        viewBinding.dialog = customDialog
    }

    private fun createDialog(dialogSavedState: Bundle?) {
        customDialog = CustomDialogBuilder(
            this,
            childFragmentManager,
            DIALOG_TAG,
            object : CustomDialogBuilder.CustomDialogInitializer<CustomDialog> {
                override fun initialize(): CustomDialog {
                    return CustomDialog.newCustomDialogInstance()
                }
            },
            object : CustomDialog.CustomDialogInflater {
                override fun inflateView(dialog: CustomDialog, layoutInflater: LayoutInflater): View? {
                    return View.inflate(requireContext(), R.layout.dialog_demo_custom, null)
                }
            }
        ).apply {
            val ctx = requireContext()
            val title = getDefaultDialogTitle(ctx)
            val message = getDefaultDialogMessage(ctx)
            val positiveButtonText = getDefaultPositiveButtonText(ctx)
            val negativeButtonText = getDefaultNegativeButtonText(ctx)
            val positiveButtonClickListener = getDefaultPositiveButtonClickListener()
            val negativeButtonClickListener = getDefaultNegativeButtonClickListener()
            withInitialDialogState(dialogSavedState)
            withTitle(title)
            withMessage(message)
            withPositiveButton(DialogButtonConfiguration(positiveButtonText), positiveButtonClickListener)
            withNegativeButton(DialogButtonConfiguration(negativeButtonText), negativeButtonClickListener)
        }.buildDialog()
    }
}