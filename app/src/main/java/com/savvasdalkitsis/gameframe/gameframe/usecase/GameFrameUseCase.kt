package com.savvasdalkitsis.gameframe.gameframe.usecase

import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import com.savvasdalkitsis.gameframe.gameframe.api.CommandResponse
import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi
import com.savvasdalkitsis.gameframe.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.model.IpNotFoundException
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.model.*
import okhttp3.*
import rx.Observable
import java.io.File
import java.io.IOException
import java.util.Collections.singletonMap

class GameFrameUseCase(private val okHttpClient: OkHttpClient,
                       private val wifiManager: WifiManager,
                       private val gameFrameApi: GameFrameApi,
                       private val ipDiscoveryUseCase: IpDiscoveryUseCase) {

    fun togglePower(): Observable<Void> =
            gameFrameApi.command(param("power")).compose(mapResponse())

    fun menu(): Observable<Void> =
            gameFrameApi.command(param("menu")).compose(mapResponse())

    fun next(): Observable<Void> =
            gameFrameApi.command(param("next")).compose(mapResponse())

    fun setBrightness(brightness: Brightness): Observable<Void> =
            gameFrameApi.set(param(brightness.queryParamName))

    fun setPlaybackMode(playbackMode: PlaybackMode): Observable<Void> =
            gameFrameApi.set(param(playbackMode.queryParamName))

    fun setCycleInterval(cycleInterval: CycleInterval): Observable<Void> =
            gameFrameApi.set(param(cycleInterval.queryParamName))

    fun setDisplayMode(displayMode: DisplayMode): Observable<Void> =
            gameFrameApi.set(param(displayMode.queryParamName))

    fun setClockFace(clockFace: ClockFace): Observable<Void> =
            gameFrameApi.set(param(clockFace.queryParamName))

    fun createFolder(name: String): Observable<Void> =
            gameFrameApi.command(singletonMap("mkdir", name))
                    .flatMap { response -> when {
                        isSuccess(response) -> Observable.just<Void>(null)
                        response.message?.contains("exists") ?: false -> Observable.error(AlreadyExistsOnGameFrameException("Could not create folder on game frame with name '$name' as it already exists"))
                        else -> Observable.error(wrap(response))
                    } }

    fun removeFolder(name: String): Observable<Void> =
            gameFrameApi.command(singletonMap("rmdir", name)).compose(mapResponse())

    fun uploadFile(file: File): Observable<Void> {
        val requestFile = RequestBody.create(MediaType.parse("image/bmp"), file)
        val filePart = MultipartBody.Part.createFormData("my_file[]", "0.bmp", requestFile)
        return gameFrameApi.upload(filePart).compose(mapResponse())
    }

    fun play(name: String): Observable<Void> =
            gameFrameApi.command(singletonMap("play", name)).compose(mapResponse())

    fun discoverGameFrameIp(): Observable<IpAddress> {
        return Observable.defer(this::deviceIp)
                .flatMap(wholePart4Subrange())
                .concatMap { ip ->
                    ipDiscoveryUseCase.emitMonitoredAddress(ip)
                    var result: Observable<IpAddress> = Observable.empty<IpAddress>()
                    try {
                        if (isFromGameFrame(ping(ip))) {
                            result = Observable.just(ip)
                        }
                    } catch (e: IOException) {
                        Log.w(GameFrameUseCase::class.java.name, "Error trying to call " + ip, e)
                    }
                    result
                }
                .first()
                .onErrorResumeNext { e -> Observable.error(IpNotFoundException("Game Frame IP not found", e)) }
    }

    private fun wholePart4Subrange() = { ip: IpAddress ->
        Observable.range(0, 256)
                .map { it.toString() }
                .map { part -> ip.copy(part4 = part) }
    }

    private val deviceIp: Observable<IpAddress>
        get() {
            @Suppress("DEPRECATION")
            val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
            return Observable.just(IpAddress.parse(ip))
        }

    private fun isFromGameFrame(response: Response) =
            response.isSuccessful && response.headers().get("Server").startsWith("Webduino/")

    @Throws(IOException::class)
    private fun ping(ip: IpAddress) = okHttpClient.newCall(Request.Builder()
            .url("http://" + ip.toString() + "/command")
            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "next="))
            .build()).execute()

    private fun param(name: String) = singletonMap(name, "")

    private fun mapResponse() = { o: Observable<CommandResponse> ->
        o.flatMap { when {
            isSuccess(it) -> Observable.just<Void>(null)
            else -> Observable.error<Void>(wrap(it))
        } }
    }

    private fun wrap(response: CommandResponse?) =
            GameFrameCommandError("Command was not successful. response: " + response!!)

    private fun isSuccess(response: CommandResponse?) = "success" == response?.status
}
