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
package com.savvasdalkitsis.gameframe.feature.control.presenter

import com.savvasdalkitsis.gameframe.feature.control.model.*
import com.savvasdalkitsis.gameframe.feature.control.view.ControlView
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.navigation.Navigator
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import com.savvasdalkitsis.gameframe.base.BasePresenter
import io.reactivex.Completable

class ControlPresenter(private val gameFrameUseCase: GameFrameUseCase,
                       private val ipRepository: IpRepository,
                       private val navigator: Navigator): BasePresenter<ControlView>() {

    fun loadIpAddress() = stream {
        ipRepository.ipAddress
                .compose<IpAddress>(RxTransformers.schedulers<IpAddress>())
                .subscribe({ view?.ipAddressLoaded(it) }, { view?.ipCouldNotBeFound(it) })
    }

    fun togglePower() = runCommandAndNotifyView(gameFrameUseCase.togglePower())

    fun menu() = runCommandAndNotifyView(gameFrameUseCase.menu())

    fun next() = runCommandAndNotifyView(gameFrameUseCase.next())

    fun changeBrightness(brightness: Brightness) = runCommandAndIgnoreResult(gameFrameUseCase.setBrightness(brightness))

    fun changePlaybackMode(playbackMode: PlaybackMode) = runCommandAndIgnoreResult(gameFrameUseCase.setPlaybackMode(playbackMode))

    fun changeCycleInterval(cycleInterval: CycleInterval) = runCommandAndIgnoreResult(gameFrameUseCase.setCycleInterval(cycleInterval))

    fun changeDisplayMode(displayMode: DisplayMode) = runCommandAndIgnoreResult(gameFrameUseCase.setDisplayMode(displayMode))

    fun changeClockFace(clockFace: ClockFace) = runCommandAndIgnoreResult(gameFrameUseCase.setClockFace(clockFace))

    fun setup() = navigator.navigateToIpSetup()

    private fun runCommand(command: Completable) =
            command.compose(RxTransformers.interceptIpMissingException())
                    .compose(RxTransformers.schedulers())

    private fun runCommandAndNotifyView(command: Completable) = stream {
        runCommand(command).subscribe({ view?.operationSuccess() }, { e -> view?.operationFailure(e) })
    }

    private fun runCommandAndIgnoreResult(command: Completable) = stream {
        runCommand(command).subscribe({ }, { })
    }
}