package com.github.rooneyandshadows.lightbulb.dialogs.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import com.github.rooneyandshadows.lightbulb.dialogs.R

class DialogHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : NestedScrollView(context, attrs, defStyleAttr) {
    val titleTextView: TextView by lazy {
        return@lazy findViewById<TextView>(R.id.dialog_title_text_view)!!
    }
    val messageTextView: TextView by lazy {
        return@lazy findViewById<TextView>(R.id.dialog_message_text_view)!!
    }
    val titleAndMessageContainer: LinearLayoutCompat by lazy {
        return@lazy findViewById<LinearLayoutCompat>(R.id.dialog_title_and_message_container)!!
    }
    var title: String? = null
        set(value) {
            field = value
            titleTextView.apply {
                text = field
                visibility = if (field.isNullOrBlank()) View.GONE
                else View.VISIBLE
            }
            syncVisibility()
        }
    var message: String? = null
        set(value) {
            field = value
            messageTextView.apply {
                text = field
                visibility = if (field.isNullOrBlank()) View.GONE
                else View.VISIBLE
            }
            syncVisibility()
        }

    init {
        inflate(context, R.layout.layout_dialog_header_view, this)
    }

    fun setTitleAndMessage(title: String?, message: String?) {
        this.title = title
        this.message = message
        syncVisibility()
    }

    private fun syncVisibility() {
        visibility = if (title.isNullOrBlank() && message.isNullOrBlank()) GONE
        else VISIBLE
    }
}