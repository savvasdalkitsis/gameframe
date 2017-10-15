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
