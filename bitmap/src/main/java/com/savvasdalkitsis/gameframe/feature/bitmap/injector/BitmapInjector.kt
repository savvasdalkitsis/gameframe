package com.savvasdalkitsis.gameframe.feature.bitmap.injector

import com.savvasdalkitsis.gameframe.feature.bitmap.usecase.AndroidViewBitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.bitmap.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application

object BitmapInjector {

    fun bitmapFileUseCase() = AndroidViewBitmapFileUseCase(application())
    fun bmpUseCase() = BmpUseCase()

}