package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_chips

import com.xiaofeng.flowlayoutmanager.FlowLayoutManager

class FlowLayoutMng : FlowLayoutManager() {
    @Override
    override fun isAutoMeasureEnabled(): Boolean {
        return false
    }
}