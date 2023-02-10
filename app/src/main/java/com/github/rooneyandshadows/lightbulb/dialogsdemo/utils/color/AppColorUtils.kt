package com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.color

import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter.ColorModel
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.color.colors.DemoColors

@Suppress("unused")
object AppColorUtils {
    fun getColor(desiredColor: IDemoColor): Int {
        return desiredColor.color
    }

    val allForPicker: ArrayList<ColorModel>
        get() {
            val result = ArrayList<ColorModel>()
            for (color in DemoColors.values())
                result.add(ColorModel(color.hexCode, color.colorName))
            return result
        }
}