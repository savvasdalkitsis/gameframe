package com.savvasdalkitsis.gameframe.injector.feature.message

import android.content.Context
import android.support.annotation.StringRes
import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder
import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay

class ToastMessageDisplay(private val context: Context) : MessageDisplay {

    override fun show(@StringRes message: Int) {
        ToastBuilder(context)
                .message(message)
                .build()
                .show()
    }

}