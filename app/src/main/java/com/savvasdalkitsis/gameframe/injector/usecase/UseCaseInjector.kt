package com.savvasdalkitsis.gameframe.injector.usecase

import com.savvasdalkitsis.gameframe.raster.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.composition.usecase.BlendUseCase
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.saves.usecase.SavedDrawingUseCase

import com.savvasdalkitsis.gameframe.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.injector.gameframe.api.GameFrameApiInjector.gameFrameApi
import com.savvasdalkitsis.gameframe.injector.infra.android.AndroidInjector.wifiManager
import com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient

object UseCaseInjector {

    private val IP_DISCOVERY_USE_CASE = IpDiscoveryUseCase()

    fun gameFrameUseCase() =
            GameFrameUseCase(okHttpClient(1).build(), wifiManager(), gameFrameApi(), ipDiscoveryUseCase())

    fun ipDiscoveryUseCase() = IP_DISCOVERY_USE_CASE

    private fun bmpUseCase() = BmpUseCase()

    fun savedDrawingUseCase() =
            SavedDrawingUseCase(bmpUseCase(), application())

    fun blendUseCase() = BlendUseCase()
}
