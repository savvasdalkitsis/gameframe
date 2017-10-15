package com.savvasdalkitsis.gameframe.feature.message

import com.savvasdalkitsis.gameframe.infra.android.Snackbars

class TopSnackbarMessageDisplay : MessageDisplay {

    override fun show(message: Int) {
        Snackbars.topInfo(message)
    }
}