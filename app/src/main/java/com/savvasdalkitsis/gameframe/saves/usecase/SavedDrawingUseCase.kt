package com.savvasdalkitsis.gameframe.saves.usecase

import com.savvasdalkitsis.gameframe.GameFrameApplication
import com.savvasdalkitsis.gameframe.bmp.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.grid.model.Grid
import com.savvasdalkitsis.gameframe.saves.model.SavedDrawingAlreadyExistsException
import org.apache.commons.io.FileUtils
import rx.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SavedDrawingUseCase(private val bmpUseCase: BmpUseCase, private val application: GameFrameApplication) {

    fun saveDrawing(name: String, colorGrid: Grid): Observable<File> {
        return file(name)
                .flatMap { dir ->
                    when {
                        dir.exists() -> Observable.error<File>(SavedDrawingAlreadyExistsException("The directory '$name' already exists"))
                        !dir.mkdirs() -> Observable.error<File>(IOException("Could not create directory " + dir.absolutePath))
                        else -> Observable.just(File(dir, "0.bmp"))
                    }
                }
                .zipWith<ByteArray, Pair<File, ByteArray>>(bmpUseCase.rasterizeToBmp(colorGrid), { a, b -> Pair(a, b) })
                .flatMap { (file, bmp) ->
                    try {
                        val stream = FileOutputStream(file)
                        stream.write(bmp)
                        stream.close()
                        Observable.just(file)
                    } catch (e: IOException ) {
                        Observable.error<File>(e)
                    }
                }
    }

    fun deleteDrawing(name: String): Observable<Void> {
        return file(name)
                .flatMap { dir ->
                    if (!dir.exists()) {
                        Observable.just<Void>(null)
                    } else try {
                        FileUtils.deleteDirectory(dir)
                        Observable.just<Void>(null)
                    } catch (e: IOException) {
                        Observable.error<Void>(e)
                    }
                }
    }

    private fun file(name: String): Observable<File> {
        return Observable.just(File(application.getExternalFilesDir(null), name))
    }
}
