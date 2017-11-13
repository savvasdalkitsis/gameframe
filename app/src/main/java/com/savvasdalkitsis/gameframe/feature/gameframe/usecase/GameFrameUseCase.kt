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
package com.savvasdalkitsis.gameframe.feature.gameframe.usecase

import android.util.Log
import com.savvasdalkitsis.gameframe.feature.bmp.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.feature.control.model.*
import com.savvasdalkitsis.gameframe.feature.gameframe.api.CommandResponse
import com.savvasdalkitsis.gameframe.feature.gameframe.api.GameFrameApi
import com.savvasdalkitsis.gameframe.feature.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.feature.gameframe.model.GameFrameCommandError
import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.ip.model.IpNotFoundException
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.saves.usecase.FileUseCase
import com.savvasdalkitsis.gameframe.feature.wifi.model.WifiNotEnabledException
import com.savvasdalkitsis.gameframe.feature.wifi.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.Collections.singletonMap

class GameFrameUseCase(private val okHttpClient: OkHttpClient,
                       private val gameFrameApi: GameFrameApi,
                       private val ipDiscoveryUseCase: IpDiscoveryUseCase,
                       private val fileUseCase: FileUseCase,
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

    fun discoverGameFrameIp(): Single<IpAddress> = wifiUseCase.getDeviceIp()
            .flattenAsFlowable(wholePart4Subrange())
            .concatMap { ip ->
                ipDiscoveryUseCase.emitMonitoredAddress(ip)
                var result: Flowable<IpAddress> = Flowable.empty<IpAddress>()
                try {
                    if (isFromGameFrame(ping(ip))) {
                        result = Flowable.just(ip)
                    }
                } catch (e: IOException) {
                    Log.w(GameFrameUseCase::class.java.name, "Error trying to call " + ip, e)
                }
                result
            }
            .firstOrError()
            .onErrorResumeNext { e -> Single.error<IpAddress>(IpNotFoundException("Game Frame IP not found", e)) }

    fun uploadAndDisplay(name: String, colorGrid: Grid): Completable =
            fileUseCase.saveFile(dirName = "bmp/$name", fileName = "0.bmp",
                    fileContentsProvider = { bmpUseCase.rasterizeToBmp(colorGrid) },
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
    private fun wholePart4Subrange() = { ip: IpAddress ->
        (0..255).map { it.toString() }.map { ip.copy(part4 = it) }
    }

    private fun isFromGameFrame(response: Response) =
            response.isSuccessful && response.headers().get("Server")?.startsWith("Webduino/") ?: false

    @Throws(IOException::class)
    private fun ping(ip: IpAddress) = okHttpClient.newCall(Request.Builder()
            .url("http://" + ip.toString() + "/command")
            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "next="))
            .build()).execute()

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