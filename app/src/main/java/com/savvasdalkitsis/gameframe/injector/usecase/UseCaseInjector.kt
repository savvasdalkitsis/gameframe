package com.savvasdalkitsis.gameframe.injector.usecase

import com.savvasdalkitsis.gameframe.feature.composition.usecase.BlendUseCase
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.raster.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.feature.saves.usecase.FileUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.infra.android.StringUseCase
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.injector.feature.gameframe.api.GameFrameApiInjector.gameFrameApi
import com.savvasdalkitsis.gameframe.injector.infra.android.AndroidInjector.wifiManager
import com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient
import com.savvasdalkitsis.gameframe.injector.infra.parsing.GsonInjector.gson

object UseCaseInjector {

    private val IP_DISCOVERY_USE_CASE = IpDiscoveryUseCase()

    fun gameFrameUseCase() = GameFrameUseCase(
            okHttpClient(1).build(),
            wifiManager(),
            gameFrameApi(),
            ipDiscoveryUseCase()
    )

    fun ipDiscoveryUseCase() = IP_DISCOVERY_USE_CASE

    fun bmpUseCase() = BmpUseCase()

    fun fileUseCase() = FileUseCase(application())

    fun blendUseCase() = BlendUseCase()

    fun workspaceUseCase() = WorkspaceUseCase(gson(), fileUseCase())

    fun stringUseCase() = StringUseCase(application())
}
