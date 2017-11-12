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
package com.savvasdalkitsis.gameframe.injector.usecase

import com.savvasdalkitsis.gameframe.feature.bmp.usecase.AndroidViewBitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.composition.usecase.BlendUseCase
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.bmp.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.feature.changelog.usecase.ChangeLogUseCase
import com.savvasdalkitsis.gameframe.feature.saves.usecase.FileUseCase
import com.savvasdalkitsis.gameframe.feature.wifi.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.infra.android.StringUseCase
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.injector.feature.gameframe.api.GameFrameApiInjector.gameFrameApi
import com.savvasdalkitsis.gameframe.injector.feature.ip.repository.IpRepositoryInjector.ipRepository
import com.savvasdalkitsis.gameframe.injector.infra.android.AndroidInjector.wifiManager
import com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient
import com.savvasdalkitsis.gameframe.injector.infra.parsing.GsonInjector.gson
import com.savvasdalkitsis.gameframe.injector.infra.rx.RxSharedPreferencesInjector.rxSharedPreferences

object UseCaseInjector {

    private val IP_DISCOVERY_USE_CASE = IpDiscoveryUseCase()

    fun gameFrameUseCase() = GameFrameUseCase(
            okHttpClient(1).build(),
            wifiManager(),
            gameFrameApi(),
            ipDiscoveryUseCase(),
            fileUseCase(),
            bmpUseCase(),
            ipRepository(),
            wifiUseCase()
    )

    fun ipDiscoveryUseCase() = IP_DISCOVERY_USE_CASE

    fun blendUseCase() = BlendUseCase()

    fun workspaceUseCase() = WorkspaceUseCase(gson(), fileUseCase())

    fun stringUseCase() = StringUseCase(application())

    private fun bmpUseCase() = BmpUseCase()

    private fun fileUseCase() = FileUseCase(application())

    fun bitmapFileUseCase() = AndroidViewBitmapFileUseCase(application())

    fun changeLogUseCase() = ChangeLogUseCase(rxSharedPreferences())

    fun wifiUseCase() = WifiUseCase(application())
}
