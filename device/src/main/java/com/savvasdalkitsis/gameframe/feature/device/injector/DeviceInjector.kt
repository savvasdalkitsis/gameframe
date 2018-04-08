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