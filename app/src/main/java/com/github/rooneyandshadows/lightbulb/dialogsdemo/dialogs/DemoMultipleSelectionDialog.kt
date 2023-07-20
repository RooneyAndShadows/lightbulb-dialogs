package com.github.rooneyandshadows.lightbulb.dialogsdemo.dialogs

import android.content.Context
import android.graphics.drawable.Drawable
import com.github.rooneyandshadows.lightbulb.commons.utils.DrawableUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.adapter.DialogPickerCheckBoxAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.AppIconUtils

class DemoMultipleSelectionDialog : AdapterPickerDialog<DemoModel>() {
    override val adapter: DialogPickerCheckBoxAdapter<DemoModel>
        get() = super.adapter as DialogPickerCheckBoxAdapter<DemoModel>

    companion object {
        fun newInstance(): DemoMultipleSelectionDialog {
            return DemoMultipleSelectionDialog()
        }
    }

    init {
        setAdapter(createAdapter())
    }

    private fun createAdapter(): DialogPickerCheckBoxAdapter<DemoModel> {
        return object : DialogPickerCheckBoxAdapter<DemoModel>() {
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