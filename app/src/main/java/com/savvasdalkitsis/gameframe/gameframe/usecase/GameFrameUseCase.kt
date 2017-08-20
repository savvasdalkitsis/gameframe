package com.savvasdalkitsis.gameframe.gameframe.usecase

import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import com.savvasdalkitsis.gameframe.control.model.*
import com.savvasdalkitsis.gameframe.gameframe.api.CommandResponse
import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi
import com.savvasdalkitsis.gameframe.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.model.IpNotFoundException
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.Collections.singletonMap

class GameFrameUseCase(private val okHttpClient: OkHttpClient,
                       private val wifiManager: WifiManager,
                       private val gameFrameApi: GameFrameApi,
                       private val ipDiscoveryUseCase: IpDiscoveryUseCase) {

    fun togglePower(): Completable =
            gameFrameApi.command(param("power")).to(mapResponse())

    fun menu(): Completable =
            gameFrameApi.command(param("menu")).to(mapResponse())

    fun next(): Completable  =
            gameFrameApi.command(param("next")).to(mapResponse())

    fun setBrightness(brightness: Brightness) =
            gameFrameApi.set(param(brightness.queryParamName))

    fun setPlaybackMode(playbackMode: PlaybackMode) =
            gameFrameApi.set(param(playbackMode.queryParamName))

    fun setCycleInterval(cycleInterval: CycleInterval) =
            gameFrameApi.set(param(cycleInterval.queryParamName))

    fun setDisplayMode(displayMode: DisplayMode) =
            gameFrameApi.set(param(displayMode.queryParamName))

    fun setClockFace(clockFace: ClockFace) =
            gameFrameApi.set(param(clockFace.queryParamName))

    fun createFolder(name: String): Completable  =
            gameFrameApi.command(singletonMap("mkdir", name))
                    .flatMapCompletable { response -> when {
                        isSuccess(response) ->
                            Completable.complete()
                        response.message?.contains("exists") ?: false ->
                            Completable.error(AlreadyExistsOnGameFrameException("Could not create folder on game frame with name '$name' as it already exists"))
                        else ->
                            Completable.error(wrap(response))
                    } }

    fun removeFolder(name: String): Completable =
            gameFrameApi.command(singletonMap("rmdir", name)).to(mapResponse())

    fun uploadFile(file: File): Completable {
        val requestFile = RequestBody.create(MediaType.parse("image/bmp"), file)
        val filePart = MultipartBody.Part.createFormData("my_file[]", "0.bmp", requestFile)
        return gameFrameApi.upload(filePart).to(mapResponse())
    }

    fun play(name: String): Completable =
            gameFrameApi.command(singletonMap("play", name)).to(mapResponse())

    fun discoverGameFrameIp(): Single<IpAddress> {
        return Single.defer(this::deviceIp)
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
    }

    private fun wholePart4Subrange() = { ip: IpAddress ->
        (0..256).map { it.toString() }.map { part -> ip.copy(part4 = part) }
    }

    private val deviceIp: Single<IpAddress>
        get() {
            @Suppress("DEPRECATION")
            val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
            return Single.defer { Single.just(IpAddress.parse(ip)) }
        }

    private fun isFromGameFrame(response: Response) =
            response.isSuccessful && response.headers().get("Server")?.startsWith("Webduino/") ?: false

    @Throws(IOException::class)
    private fun ping(ip: IpAddress) = okHttpClient.newCall(Request.Builder()
            .url("http://" + ip.toString() + "/command")
            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "next="))
            .build()).execute()

    private fun param(name: String) = singletonMap(name, "")

    private fun mapResponse(): Function<Single<CommandResponse>, Completable> = Function { s ->
        s.flatMapCompletable {
            when {
                isSuccess(it) -> Completable.complete()
                else -> Completable.error(wrap(it))
            }
        }
    }

    private fun wrap(response: CommandResponse?) =
            GameFrameCommandError("Command was not successful. response: " + response!!)

    private fun isSuccess(response: CommandResponse?) = "success" == response?.status
}
