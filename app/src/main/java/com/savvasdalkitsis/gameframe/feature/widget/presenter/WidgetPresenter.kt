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
package com.savvasdalkitsis.gameframe.feature.widget.presenter

import android.util.Log
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.navigation.IpNavigator
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.widget.view.PowerTileService
import com.savvasdalkitsis.gameframe.feature.widget.view.WidgetView
import com.savvasdalkitsis.gameframe.infra.base.BasePresenter
import com.savvasdalkitsis.gameframe.infra.base.plusAssign
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.Completable

class WidgetPresenter(private val gameFrameUseCase: GameFrameUseCase,
                      private val ipRepository: IpRepository,
                      private val navigator: IpNavigator): BasePresenter<WidgetView>() {

    fun menu() = perform(gameFrameUseCase.menu())

    fun next() = perform(gameFrameUseCase.next())

    fun power() = perform(gameFrameUseCase.togglePower())

    private fun perform(operation: Completable) {
        managedStreams += ipRepository.ipAddress
                .doOnError {
                    Log.e(PowerTileService::class.java.name, "IP address not found", it)
                    navigator.navigateToIpSetup()
                }
                .flatMapCompletable { operation }
                .compose(RxTransformers.schedulers())
                .subscribe({ }, {
                    Log.e(PowerTileService::class.java.name, "Error communicating with the GameFrame", it)
                    view?.operationError()
                })
    }
}
