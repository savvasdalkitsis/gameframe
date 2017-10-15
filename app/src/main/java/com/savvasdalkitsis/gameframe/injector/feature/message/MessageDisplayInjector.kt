package com.savvasdalkitsis.gameframe.injector.feature.message

import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay
import com.savvasdalkitsis.gameframe.feature.message.ToastMessageDisplay
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector

object MessageDisplayInjector {

    fun messageDisplay(): MessageDisplay = ToastMessageDisplay(ApplicationInjector.application())
}