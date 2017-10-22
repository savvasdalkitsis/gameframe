package com.savvasdalkitsis.gameframe.feature.bmp.usecase

import io.reactivex.Single
import java.io.File

interface BitmapFileUseCase<in BitmapSource> {

    fun getFileFor(bitmapSource: BitmapSource, name: String): Single<File>
}