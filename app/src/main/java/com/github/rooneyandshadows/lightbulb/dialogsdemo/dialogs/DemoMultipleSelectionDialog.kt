package com.github.rooneyandshadows.lightbulb.dialogsdemo.dialogs

import com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getCheckboxAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.getRadioButtonAdapter
import com.github.rooneyandshadows.lightbulb.dialogsdemo.models.DemoModel
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter

class DemoMultipleSelectionDialog : AdapterPickerDialog<DemoModel>() {
    override val adapterCreator: AdapterCreator<DemoModel>
        get() = object : AdapterCreator<DemoModel> {
            @Override
            override fun createAdapter(): EasyRecyclerAdapter<DemoModel> {
                return getCheckboxAdapter()
            }
        }

    companion object {
        fun newInstance(): DemoMultipleSelectionDialog {
            return DemoMultipleSelectionDialog()
        }
    }
}