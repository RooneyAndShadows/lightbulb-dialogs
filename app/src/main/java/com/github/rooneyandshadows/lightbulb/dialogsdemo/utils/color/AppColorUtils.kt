package com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.color

import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_color.ColorPickerAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.color.colors.DemoColors
import java.util.ArrayList

@Suppress("unused")
object AppColorUtils {
    fun getColor(desiredColor: IDemoColor): Int {
        return desiredColor.color
    }

    val allForPicker: ArrayList<ColorPickerAdapter.ColorModel>
        get() {
            val result = ArrayList<ColorPickerAdapter.ColorModel>()
            for (color in DemoColors.values())
                result.add(ColorPickerAdapter.ColorModel(color.hexCode, color.colorName))
            return result
        }
}