/**
 * Copyright 2018 Savvas Dalkitsis
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
package com.savvasdalkitsis.gameframe.feature.device.injector

import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector.bmpUseCase
import com.savvasdalkitsis.gameframe.feature.device.api.DeviceApi
import com.savvasdalkitsis.gameframe.feature.device.usecase.DeviceUseCase
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipBaseHostInterceptor
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipRepository
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.wifiUseCase
import com.savvasdalkitsis.gameframe.feature.storage.injector.StorageInjector.localStorageUseCase
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DeviceInjector {

    fun deviceCase() = DeviceUseCase(
            gameFrameApi(),
            localStorageUseCase(),
            bmpUseCase(),
            ipRepository(),
            wifiUseCase()
    )

    private fun gameFrameApi(): DeviceApi = retrofit().create(DeviceApi::class.java)

    private fun retrofit(): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://nothing")
            .client(NetworkingInjector.okHttpClient(3, ipBaseHostInterceptor())
                    .build())
            .build()
}