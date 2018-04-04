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

import com.savvasdalkitsis.gameframe.feature.account.usecase.FirebaseAuthenticationUseCase
import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector.bmpUseCase
import com.savvasdalkitsis.gameframe.feature.device.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipRepository
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.wifiUseCase
import com.savvasdalkitsis.gameframe.feature.storage.injector.StorageInjector
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.infra.android.StringUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider
import com.savvasdalkitsis.gameframe.injector.feature.gameframe.api.GameFrameApiInjector.gameFrameApi
import com.savvasdalkitsis.gameframe.injector.feature.workspace.WorkspaceStorageInjector.localWorkspaceStorage
import com.savvasdalkitsis.gameframe.injector.feature.workspace.WorkspaceStorageInjector.workspaceStorage
import com.savvasdalkitsis.gameframe.injector.infra.parsing.GsonInjector.gson

object UseCaseInjector {

    fun gameFrameUseCase() = GameFrameUseCase(
            gameFrameApi(),
            StorageInjector.localStorageUseCase(),
            bmpUseCase(),
            ipRepository(),
            wifiUseCase()
    )

    fun workspaceUseCase() = WorkspaceUseCase(gson(), workspaceStorage(), localWorkspaceStorage())

    fun stringUseCase() = StringUseCase(application())

    fun authenticationUseCase() = FirebaseAuthenticationUseCase(topActivityProvider())
}
