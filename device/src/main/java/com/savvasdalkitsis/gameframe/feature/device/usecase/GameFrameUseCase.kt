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
package com.savvasdalkitsis.gameframe.feature.device.usecase

import com.savvasdalkitsis.gameframe.feature.bitmap.model.Bitmap
import com.savvasdalkitsis.gameframe.feature.bitmap.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.feature.device.api.CommandResponse
import com.savvasdalkitsis.gameframe.feature.device.api.GameFrameApi
import com.savvasdalkitsis.gameframe.feature.device.model.*
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.networking.model.WifiNotEnabledException
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.feature.storage.usecase.LocalStorageUseCase
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.functions.Function
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Collections.singletonMap

class GameFrameUseCase(private val gameFrameApi: GameFrameApi,
                       private val localStorageUseCase: LocalStorageUseCase,
                       private val bmpUseCase: BmpUseCase,
                       private val ipRepository: IpRepository,
                       private val wifiUseCase: WifiUseCase) {

    fun togglePower() = issueCommand("power")

    fun menu() = issueCommand("menu")

    fun next()  = issueCommand("next")

    fun setBrightness(brightness: Brightness) = setParam(brightness.queryParamName)

    fun setPlaybackMode(playbackMode: PlaybackMode) = setParam(playbackMode.queryParamName)

    fun setCycleInterval(cycleInterval: CycleInterval) = setParam(cycleInterval.queryParamName)

    fun setDisplayMode(displayMode: DisplayMode) = setParam(displayMode.queryParamName)

    fun setClockFace(clockFace: ClockFace) = setParam(clockFace.queryParamName)

    fun removeFolder(name: String) = issueCommand("rmdir", name)

    fun uploadAndDisplay(name: String, bitmap: Bitmap): Completable =
            localStorageUseCase.saveFile(dirName = "bmp/$name", fileName = "0.bmp",
                    fileContentsProvider = { bmpUseCase.rasterizeToBmp(bitmap) },
                    overwriteFile = true)
                    .flatMap<File> { file -> createFolder(name).toSingleDefault<File>(file) }
                    .flatMapCompletable { uploadFile(it) }
                    .andThen(play(name))

    private fun createFolder(name: String): Completable  = issueCommand("mkdir", name)
            .onErrorResumeNext {
                if (it is GameFrameCommandError && (it.response.message?.contains("exists")) == true) {
                    Completable.error(AlreadyExistsOnGameFrameException("Could not create folder on game frame with name '$name' as it already exists", it))
                } else {
                    Completable.error(it)
                }
            }

    private fun uploadFile(file: File): Completable {
        val requestFile = RequestBody.create(MediaType.parse("image/bmp"), file)
        val filePart = MultipartBody.Part.createFormData("my_file[]", "0.bmp", requestFile)
        return gameFrameApi.upload(filePart)
                .to(mapResponse())
                .onErrorResumeNext { error -> wifiUseCase.isWifiEnabled()
                        .flatMapCompletable { enabled ->
                            if (!enabled) Completable.error(WifiNotEnabledException())
                            else Completable.error(error)
                        } }
    }

    private fun play(name: String) = issueCommand("play", name)

    private fun param(key: String, value: String = "") = singletonMap(key, value)

    private fun mapResponse(): Function<Maybe<CommandResponse>, Completable> = Function { s ->
        s.flatMapCompletable {
            when {
                isSuccess(it) -> Completable.complete()
                else -> Completable.error(wrap(it))
            }
        }
    }

    private fun issueCommand(key: String, value: String = ""): Completable =
            ipRepository.ipAddress
                    .flatMapCompletable { gameFrameApi.command(param(key, value)).to(mapResponse()) }
                    .onErrorResumeNext(changeErrorToWifiIfNotEnabled())

    private fun changeErrorToWifiIfNotEnabled(): (Throwable) -> Completable = { error ->
        wifiUseCase.isWifiEnabled().flatMapCompletable { enabled ->
            if (!enabled) Completable.error(WifiNotEnabledException())
            else Completable.error(error)
        }
    }

    private fun setParam(key: String): Completable = gameFrameApi.set(param(key))

    private fun wrap(response: CommandResponse) =
            GameFrameCommandError("Command was not successful. response: $response", response)

    private fun isSuccess(response: CommandResponse?) = "success" == response?.status

}