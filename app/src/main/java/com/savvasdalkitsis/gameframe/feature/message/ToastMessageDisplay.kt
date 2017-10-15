package com.savvasdalkitsis.gameframe.feature.message

import android.content.Context
import android.widget.Toast

class ToastMessageDisplay(private val context: Context) : MessageDisplay {

    override fun show(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}