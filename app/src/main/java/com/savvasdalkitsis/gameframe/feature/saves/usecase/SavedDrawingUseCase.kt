package com.savvasdalkitsis.gameframe.feature.saves.usecase

import com.savvasdalkitsis.gameframe.GameFrameApplication
import com.savvasdalkitsis.gameframe.feature.raster.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.feature.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.saves.model.SavedDrawingAlreadyExistsException
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SavedDrawingUseCase(private val bmpUseCase: BmpUseCase, private val application: GameFrameApplication) {

    fun saveDrawing(name: String, colorGrid: Grid): Single<File> {
        return file(name)
                .flatMap { dir ->
                    when {
                        dir.exists() -> Single.error<File>(SavedDrawingAlreadyExistsException("The directory '$name' already exists"))
                        !dir.mkdirs() -> Single.error<File>(IOException("Could not create directory " + dir.absolutePath))
                        else -> Single.just(File(dir, "0.bmp"))
                    }
                }
                .zipWith<ByteArray, Pair<File, ByteArray>>(bmpUseCase.rasterizeToBmp(colorGrid), BiFunction { a , b -> Pair(a, b) })
                .flatMap { (file, bmp) ->
                    try {
                        val stream = FileOutputStream(file)
                        stream.write(bmp)
                        stream.close()
                        Single.just(file)
                    } catch (e: IOException ) {
                        Single.error<File>(e)
                    }
                }
    }

    fun deleteDrawing(name: String): Completable {
        return file(name)
                .flatMapCompletable { dir ->
                    if (!dir.exists()) {
                        Completable.complete()
                    } else try {
                        FileUtils.deleteDirectory(dir)
                        Completable.complete()
                    } catch (e: IOException) {
                        Completable.error(e)
                    }
                }
    }

    private fun file(name: String): Single<File> {
        return Single.just(File(application.getExternalFilesDir(null), name))
    }
}
