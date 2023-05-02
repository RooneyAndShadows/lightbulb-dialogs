package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_icon.adapter

enum class IconSet(val value: Int, val setName: String) {
    FONTAWESOME(1, "FontAwesome");

    companion object {
        private val mapValues: MutableMap<Int, IconSet> = HashMap()
        private val mapNames: MutableMap<String, IconSet> = HashMap()

        init {
            for (iconSet in values()) {
                mapValues[iconSet.value] = iconSet
                mapNames[iconSet.setName] = iconSet
            }
        }

        fun valueOf(value: Int): IconSet? {
            return mapValues[value]
        }

        fun byName(setName: String): IconSet? {
            return mapNames[setName]
        }

        fun has(setName: String): Boolean {
            return mapNames.containsKey(setName)
        }
    }
}