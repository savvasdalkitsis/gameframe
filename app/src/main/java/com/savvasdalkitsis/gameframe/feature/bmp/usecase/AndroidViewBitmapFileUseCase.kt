package com.savvasdalkitsis.gameframe.feature.bmp.usecase

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