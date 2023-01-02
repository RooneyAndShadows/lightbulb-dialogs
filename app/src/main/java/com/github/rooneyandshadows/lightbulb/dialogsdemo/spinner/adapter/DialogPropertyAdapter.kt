package com.github.rooneyandshadows.lightbulb.dialogsdemo.spinner.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DialogPropertyAdapter(
    context: Context,
    private var values: Array<DialogPropertyItem> = arrayOf(),
    textViewResourceId: Int = android.R.layout.simple_spinner_item,
) : ArrayAdapter<DialogPropertyItem>(context, textViewResourceId, values) {

    @Override
    override fun getCount(): Int {
        return values.size
    }

    @Override
    override fun getItem(position: Int): DialogPropertyItem {
        return values[position]
    }

    @Override
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @Override
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].name
        return label
    }

    @Override
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = values[position].name
        return label
    }

    fun setProperties(properties: Array<DialogPropertyItem>) {
        values = properties
        notifyDataSetChanged()
    }

    fun saveInstanceState(): Bundle {
        return Bundle().apply {
            this.putParcelableArray("adapter_items", values)
        }
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    fun restoreInstanceState(state: Bundle) {
        values = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            state.getParcelableArray("adapter_items", DialogPropertyItem::class.java)!!
        else
            state.getParcelableArray("adapter_items") as Array<DialogPropertyItem>
        notifyDataSetChanged()
    }
}