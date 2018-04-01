/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.bitmap.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream

class AndroidViewBitmapFileUseCase(private val context: Context) : BitmapFileUseCase<View> {

    override fun getFileFor(bitmapSource: View, name: String): Single<File> = Single.create { emitter ->
        try {
            val bitmap = Bitmap.createBitmap(bitmapSource.width, bitmapSource.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            bitmapSource.draw(canvas)
            val file = File(context.externalCacheDir, "$name.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
            emitter.onSuccess(file)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }
}