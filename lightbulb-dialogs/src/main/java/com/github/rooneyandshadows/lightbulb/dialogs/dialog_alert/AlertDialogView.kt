package com.github.rooneyandshadows.lightbulb.dialogs.dialog_alert

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.R
import com.github.rooneyandshadows.lightbulb.dialogs.base.BaseDialogFragment
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogAnimationTypes.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogButtonConfiguration
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.DialogTypes.*
import com.github.rooneyandshadows.lightbulb.dialogs.base.internal.callbacks.DialogButtonClickListener

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class AlertDialogView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private lateinit var dialog: AlertDialog
    private lateinit var fragmentManager: FragmentManager
    var dialogTag: String = ""
        private set
    var dialogTitle: String? = null
        private set
    var dialogMessage: String? = null
        private set
    var dialogPositiveButtonText: String? = null
        private set
    var dialogNegativeButtonText: String? = null
        private set
    var dialogCancelable = false
        private set
    var dialogType: DialogTypes = NORMAL
        private set
    var dialogAnimationType: DialogAnimationTypes = NO_ANIMATION
        private set
    val isDialogShown: Boolean
        get() = dialog.isDialogShown

    init {
        isSaveEnabled = true
        if (!isInEditMode) {
            fragmentManager = getFragmentManager(context)!!
        }
        readAttributes(context, attrs)
        initializeDialog()
    }

    fun initializeDialog() {
        dialog = AlertDialogBuilder(null, fragmentManager, dialogTag).apply {
            dialogTitle?.apply { withTitle(this) }
            dialogMessage?.apply { withMessage(this) }
            dialogPositiveButtonText?.apply {
                withPositiveButton(generateButtonConfig(this)!!, object : DialogButtonClickListener {
                    override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {}
                })
            }
            dialogNegativeButtonText?.apply {
                withNegativeButton(generateButtonConfig(this)!!, object : DialogButtonClickListener {
                    override fun doOnClick(buttonView: View?, dialogFragment: BaseDialogFragment) {}
                })
            }
            withCancelOnClickOutside(dialogCancelable)
            withDialogType(dialogType)
            withAnimations(dialogAnimationType)
        }.buildDialog()
    }

    fun show() {
        dialog.show()
    }

    fun hide() {
        dialog.dismiss()
    }

    fun setDialogTag(dialogTag: String) {
        this.dialogTag = dialogTag
    }

    fun setDialogTitle(dialogTitle: String?) {
        this.dialogTitle = dialogTitle
        dialog.setDialogTitle(dialogTitle)
    }

    fun setDialogMessage(dialogMessage: String?) {
        this.dialogMessage = dialogMessage
        dialog.setDialogMessage(dialogMessage)
    }

    fun setPositiveButtonText(buttonText: String?) {
        this.dialogPositiveButtonText = buttonText
        dialog.setDialogPositiveButton(generateButtonConfig(buttonText))
    }

    fun setNegativeButtonText(buttonText: String?) {
        this.dialogNegativeButtonText = buttonText
        dialog.setDialogNegativeButton(generateButtonConfig(buttonText))
    }

    fun setCancelable(isCancelable: Boolean) {
        this.dialogCancelable = isCancelable
        dialog.isCancelable = isCancelable
    }

    fun setDialogType(dialogType: DialogTypes) {
        this.dialogType = dialogType
        dialog.dialogType = dialogType
    }

    fun setDialogAnimationType(dialogAnimationType: DialogAnimationTypes) {
        this.dialogAnimationType = dialogAnimationType
        dialog.dialogAnimationType = dialogAnimationType
    }

    private fun getFragmentManager(context: Context?): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getFragmentManager(context.baseContext)
            else -> null
        }
    }

    private fun generateButtonConfig(buttonText: String?): DialogButtonConfiguration? {
        if (buttonText.isNullOrBlank()) return null
        return DialogButtonConfiguration(buttonTitle = buttonText, buttonEnabled = true, closeDialogOnClick = true)
    }


    private fun readAttributes(context: Context, attrs: AttributeSet?) {
        val attrTypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AlertDialogView, 0, 0)
        try {
            attrTypedArray.apply {
                //Must be the first attribute to be read in order to instantiate dialog with tag.
                getString(R.styleable.AlertDialogView_alert_dialog_tag).apply {
                    dialogTag = let {
                        val default = ResourceUtils.getPhrase(context, R.string.picker_default_dialog_tag_text)
                        return@let if (it.isNullOrBlank()) default else it
                    }
                }
                getString(R.styleable.AlertDialogView_alert_dialog_title).apply {
                    val default = ""
                    dialogTitle = this ?: default
                }
                getString(R.styleable.AlertDialogView_alert_dialog_message).apply {
                    val default = ""
                    dialogMessage = this ?: default
                }
                getString(R.styleable.AlertDialogView_alert_dialog_button_positive_text).apply {
                    dialogPositiveButtonText = let {
                        val default = ResourceUtils.getPhrase(context, R.string.picker_default_positive_button_text)
                        return@let if (it.isNullOrBlank()) default else it
                    }
                }
                getString(R.styleable.AlertDialogView_alert_dialog_button_negative_text).apply {
                    dialogNegativeButtonText = let {
                        val default = ResourceUtils.getPhrase(context, R.string.picker_default_negative_button_text)
                        return@let if (it.isNullOrBlank()) default else it
                    }
                }
                getInt(R.styleable.AlertDialogView_alert_dialog_type, NORMAL.value).apply {
                    dialogType = DialogTypes.valueOf(this)
                }
                getInt(R.styleable.AlertDialogView_alert_dialog_animation, NO_ANIMATION.value).apply {
                    dialogAnimationType = DialogAnimationTypes.valueOf(this)
                }
                dialogCancelable = getBoolean(R.styleable.AlertDialogView_alert_dialog_cancelable, true)
            }
        } finally {
            attrTypedArray.recycle()
        }
    }

    @Override
    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val myState = SavedState(superState)
        myState.apply {
            dialogState = dialog.saveDialogState()
        }
        return myState
    }

    @Override
    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        dialog.restoreDialogState(savedState.dialogState)
    }

    private class SavedState : BaseSavedState {
        var dialogState: Bundle? = null

        constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            parcel.apply {
                dialogState = readBundle(Bundle::class.java.classLoader)
            }
        }

        @Override
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.apply {
                writeBundle(dialogState)
            }
        }

        @Override
        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}