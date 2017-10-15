package com.savvasdalkitsis.gameframe.injector.feature.message

import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay
import com.savvasdalkitsis.gameframe.feature.message.TopSnackbarMessageDisplay

object MessageDisplayInjector {

    fun messageDisplay(): MessageDisplay = TopSnackbarMessageDisplay()
}