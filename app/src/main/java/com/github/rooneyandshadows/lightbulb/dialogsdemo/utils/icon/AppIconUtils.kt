package com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import com.github.rooneyandshadows.lightbulb.commons.utils.IconUtils
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconModel
import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter.IconSet.*
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.icons.DemoIcons
import com.mikepenz.iconics.IconicsDrawable

@Suppress("unused")
object AppIconUtils {
    fun getIconWithResolvedColor(
        context: Context,
        icon: IDemoIcon,
        colorRef: Int,
        @DimenRes dimenId: Int
    ): IconicsDrawable {
        val sizeInPx: Int = ResourceUtils.getDimenPxById(context, dimenId)
        return IconUtils.getIconWithResolvedColor(context, icon.icon, colorRef, sizeInPx)
    }

    fun getIconWithResolvedColor(
        context: Context,
        icon: IDemoIcon,
        colorRef: Int
    ): IconicsDrawable {
        val sizeInPx: Int = ResourceUtils.getDimenPxById(context, R.dimen.ICON_SIZE_MEDIUM)
        return IconUtils.getIconWithResolvedColor(context, icon.icon, colorRef, sizeInPx)
    }

    fun getIconWithAttributeColor(
        context: Context,
        icon: IDemoIcon,
        @AttrRes colorRef: Int,
        @DimenRes dimenId: Int
    ): IconicsDrawable {
        val sizeInPx: Int = ResourceUtils.getDimenPxById(context, dimenId)
        return IconUtils.getIconWithAttributeColor(context, icon.icon, colorRef, sizeInPx)
    }

    fun getIconWithAttributeColor(
        context: Context,
        icon: IDemoIcon,
        @AttrRes colorRef: Int
    ): IconicsDrawable {
        val sizeInPx: Int = ResourceUtils.getDimenPxById(context, R.dimen.ICON_SIZE_MEDIUM)
        return IconUtils.getIconWithAttributeColor(context, icon.icon, colorRef, sizeInPx)
    }

    fun getIconWithAttributeColor(
        context: Context,
        icon: IDemoIcon
    ): IconicsDrawable {
        val sizeInPx: Int = ResourceUtils.getDimenPxById(context, R.dimen.ICON_SIZE_MEDIUM)
        return IconUtils.getIconWithAttributeColor(context, icon.icon, R.attr.colorPrimary, sizeInPx)
    }

    val allForPicker: List<IconModel>
        get() {
            val result: MutableList<IconModel> = mutableListOf()
            for (demoIcon in DemoIcons.values())
                result.add(IconModel(demoIcon.icon.name, FONTAWESOME))
            return result
        }
}