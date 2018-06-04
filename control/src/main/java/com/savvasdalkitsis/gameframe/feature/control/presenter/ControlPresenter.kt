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

import android.util.Log
import com.savvasdalkitsis.gameframe.feature.analytics.Analytics
import com.savvasdalkitsis.gameframe.feature.control.view.ControlView
import com.savvasdalkitsis.gameframe.feature.device.model.*
import com.savvasdalkitsis.gameframe.feature.device.usecase.DeviceUseCase
import com.savvasdalkitsis.gameframe.feature.ip.model.IpBaseHostMissingException
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.navigation.usecase.Navigator
import com.savvasdalkitsis.gameframe.feature.networking.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.networking.model.WifiNotEnabledException
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.infra.base.BasePresenter
import com.savvasdalkitsis.gameframe.infra.base.plusAssign
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import com.savvasdalkitsis.gameframe.infra.rx.logErrors
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class ControlPresenter(private val deviceUseCase: DeviceUseCase,
                       private val ipRepository: IpRepository,
                       private val navigator: Navigator,
                       private val wifiUseCase: WifiUseCase,
                       private val analytics: Analytics): BasePresenter<ControlView>() {

    fun loadIpAddress() {
        managedStreams += ipRepository.ipAddress
                .compose<IpAddress>(RxTransformers.schedulers<IpAddress>())
                .subscribe({ view?.ipAddressLoaded(it) }, { view?.ipCouldNotBeFound(it) })
    }

    fun togglePower() = runCommandAndNotifyView(deviceUseCase.togglePower()).logEvent("toggle_power")

    fun menu() = runCommandAndNotifyView(deviceUseCase.menu()).logEvent("menu")

    fun next() = runCommandAndNotifyView(deviceUseCase.next()).logEvent("next")

    fun changeBrightness(brightness: Brightness) = runCommandAndIgnoreResult(deviceUseCase.setBrightness(brightness))
            .logEvent("brightness", "level" to brightness.name)

    fun changePlaybackMode(playbackMode: PlaybackMode) = runCommandAndIgnoreResult(deviceUseCase.setPlaybackMode(playbackMode))
            .logEvent("playback_mode", "mode" to playbackMode.name)

    fun changeCycleInterval(cycleInterval: CycleInterval) = runCommandAndIgnoreResult(deviceUseCase.setCycleInterval(cycleInterval))
            .logEvent("cycle_interval", "interval" to cycleInterval.name)

    fun changeDisplayMode(displayMode: DisplayMode) = runCommandAndIgnoreResult(deviceUseCase.setDisplayMode(displayMode))
            .logEvent("display_mode", "mode" to displayMode.name)

    fun changeClockFace(clockFace: ClockFace) = runCommandAndIgnoreResult(deviceUseCase.setClockFace(clockFace))
            .logEvent("clock_face", "face" to clockFace.name)

    fun setup() = navigator.navigateToIpSetup().logEvent("setup_from_control")

    private fun runCommand(command: Completable) =
            command.compose(interceptIpMissingException())
                    .compose(RxTransformers.schedulers())

    private fun runCommandAndNotifyView(command: Completable) {
        managedStreams += runCommand(command).subscribe({ view?.operationSuccess() }, { e ->
            when (e) {
                is WifiNotEnabledException -> view?.wifiNotEnabledError(e)
                else -> view?.operationFailure(e)
            }
        })
    }

    private fun runCommandAndIgnoreResult(command: Completable) {
        managedStreams += runCommand(command).subscribe({ }, logErrors())
    }

    fun enableWifi() {
        wifiUseCase.enableWifi()
    }

    private fun interceptIpMissingException() = CompletableTransformer { c ->
        c.doOnError {
            if (it is IpBaseHostMissingException) {
                Log.e(RxTransformers::class.java.name, "Error: ", it)
                navigator.navigateToIpSetup()
            }
        }
    }

    @Suppress("unused")
    private fun Unit.logEvent(name: String, vararg params: Pair<String, String>) {
        analytics.logEvent(name, *params)
    }
}