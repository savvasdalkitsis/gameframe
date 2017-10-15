package com.savvasdalkitsis.gameframe.feature.message

import android.support.annotation.StringRes

interface MessageDisplay {

    fun show(@StringRes message: Int)
}