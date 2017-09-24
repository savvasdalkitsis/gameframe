package com.savvasdalkitsis.gameframe.feature.saves.usecase

import com.savvasdalkitsis.gameframe.GameFrameApplication
import com.savvasdalkitsis.gameframe.feature.saves.model.FileAlreadyExistsException
import com.savvasdalkitsis.gameframe.infra.kotlin.InputStreamProvider
import io.reactivex.Completable
import io.reactivex.Single
import org.apache.commons.io.FileUtils
import java.io.*

class FileUseCase(private val application: GameFrameApplication) {

    fun saveFile(dirName: String, fileName: String,
                 fileContentsProvider: InputStreamProvider,
                 overwriteFile: Boolean = false): Single<File> =
            file(dirName)
                    .flatMap { dir ->
                        when {
                            dir.exists() -> Single.just(File(dir, fileName))
                            !dir.mkdirs() -> Single.error<File>(IOException("Could not create directory " + dir.absolutePath))
                            else -> Single.just(File(dir, fileName))
                        }
                    }
                    .flatMap { file -> if (file.exists() && !overwriteFile) {
                        Single.error<File>(FileAlreadyExistsException("The file '${file.absolutePath}' already exists"))
                    } else {
                        Single.just(file)
                    }}
                    .flatMap { file ->
                        try {
                            FileUtils.copyInputStreamToFile(fileContentsProvider(), file)
                            Single.just(file)
                        } catch (e: Throwable ) {
                            Single.error<File>(e)
                        }
                    }

    fun readFile(dirName: String, fileName: String): Single<Reader> = file(dirName)
            .map { File(it, fileName) }
            .map { BufferedReader(FileReader(it)) }


    fun listFilesIn(dirName: String): Single<List<File>> = file(dirName)
            .map { it.listFiles().orEmpty().toList() }

    fun deleteFile(dirName: String, fileName: String): Completable = file(dirName)
            .map { File(it, fileName) }
            .flatMapCompletable {
                if (it.delete()) {
                    Completable.complete()
                } else {
                    Completable.error(IOException("Could not delete file ${it.absolutePath}"))
                }
            }

    private fun file(name: String) =
            Single.just(File(application.getExternalFilesDir(null), name))
}
