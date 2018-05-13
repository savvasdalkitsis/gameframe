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
package com.savvasdalkitsis.gameframe.feature.ip.usecase

import android.util.Log
import com.savvasdalkitsis.gameframe.feature.ip.model.IpNotFoundException
import com.savvasdalkitsis.gameframe.feature.networking.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import okhttp3.*
import java.io.IOException

class IpDiscoveryUseCase(private val wifiUseCase: WifiUseCase, private val okHttpClient: OkHttpClient) {

    private val processor = BehaviorProcessor.create<IpAddress>()

    fun monitoredIps(): Flowable<IpAddress> = processor

    fun discoverGameFrameIp(): Single<IpAddress> = wifiUseCase.getDeviceIp()
            .flattenAsFlowable(wholePart4Subrange())
            .concatMap { ip ->
                emitMonitoredAddress(ip)
                var result: Flowable<IpAddress> = Flowable.empty<IpAddress>()
                try {
                    if (isFromGameFrame(ping(ip))) {
                        result = Flowable.just(ip)
                    }
                } catch (e: IOException) {
                    Log.w(IpDiscoveryUseCase::class.java.name, "Error trying to call $ip", e)
                }
                result
            }
            .firstOrError()
            .onErrorResumeNext { e -> Single.error<IpAddress>(IpNotFoundException("Game Frame IP not found", e)) }

    private fun emitMonitoredAddress(ip: IpAddress) {
        processor.onNext(ip)
    }

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
}
