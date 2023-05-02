package com.github.rooneyandshadows.lightbulb.dialogsdemo.dialogs

import android.content.Context
import android.graphics.drawable.Drawable
import com.github.rooneyandshadows.lightbulb.commons.utils.DrawableUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerAdapter
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerRadioButtonAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.AppIconUtils

class DemoSingleSelectionDialog : AdapterPickerDialog<DemoModel>() {
    override val adapter: DialogPickerAdapter<DemoModel>
        get() = super.adapter as DialogPickerRadioButtonAdapter<DemoModel>
    override val adapterCreator: AdapterCreator<DemoModel>
        get() = object : AdapterCreator<DemoModel> {
            @Override
            override fun createAdapter(): DialogPickerRadioButtonAdapter<DemoModel> {
                return object : DialogPickerRadioButtonAdapter<DemoModel>() {
                    @Override
                    override fun getItemIcon(context: Context, item: DemoModel): Drawable {
                        return AppIconUtils.getIconWithAttributeColor(
                            context,
                            item.icon,
                            R.attr.colorOnSurface,
                            R.dimen.ICON_SIZE_RECYCLER_ITEM
                        )
                    }

                    @Override
                    override fun getItemIconBackground(context: Context, item: DemoModel): Drawable {
                        return DrawableUtils.getRoundedCornersDrawable(
                            item.iconBackgroundColor.color,
                            ResourceUtils.dpToPx(100)
                        )
                    }
                }
            }
        }

    companion object {
        fun newInstance(): DemoSingleSelectionDialog {
            return DemoSingleSelectionDialog()
        }
    }
}