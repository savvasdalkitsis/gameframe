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
package com.savvasdalkitsis.gameframe.feature.ip.presenter

import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.ip.view.IpSetupView
import com.savvasdalkitsis.gameframe.feature.networking.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.infra.base.BasePresenter
import com.savvasdalkitsis.gameframe.infra.base.plusAssign
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers

class IpSetupPresenter(private val gameFrameUseCase: GameFrameUseCase,
                       private val ipRepository: IpRepository,
                       private val ipDiscoveryUseCase: IpDiscoveryUseCase,
                       private val wifiUseCase: WifiUseCase): BasePresenter<IpSetupView>() {
    
    fun start() {
        loadStoredIp()
        managedStreams += wifiUseCase.isWifiEnabled()
                .filter { !it }
                .subscribe( { view?.displayWifiNotEnabled() } )
    }

    fun setup(ipAddress: IpAddress) {
        ipRepository.saveIpAddress(ipAddress)
        view?.addressSaved(ipAddress)
    }

    fun discoverIp() {
        view?.displayDiscovering()
        managedStreams += ipDiscoveryUseCase.monitoredIps()
                .compose(RxTransformers.schedulersFlowable())
                .subscribe( { view?.tryingAddress(it) }, { })
        managedStreams += gameFrameUseCase.discoverGameFrameIp()
                .compose(RxTransformers.schedulers<IpAddress>())
                .doOnSuccess { view?.displayIdleView() }
                .subscribe(
                        { view?.ipAddressDiscovered(it) },
                        { throwable ->
                            view?.errorDiscoveringIpAddress(throwable)
                            loadStoredIp()
                        }
                )
    }

    fun cancelDiscover() {
        clearStreams()
        loadStoredIp()
    }

    private fun loadStoredIp() {
        view?.displayIdleView()
        managedStreams += ipRepository.ipAddress
                .subscribe(
                        { view?.displayIpAddress(it) },
                        { view?.displayIpAddress(IpAddress()) }
                )
    }

    fun enableWifi() {
        wifiUseCase.enableWifi()
    }
}
