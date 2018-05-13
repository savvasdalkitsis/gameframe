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
package com.savvasdalkitsis.gameframe.feature.ip.injector

import com.savvasdalkitsis.gameframe.feature.ip.navigation.AndroidIpNavigator
import com.savvasdalkitsis.gameframe.feature.ip.navigation.IpNavigator
import com.savvasdalkitsis.gameframe.feature.ip.network.IpBaseHostInterceptor
import com.savvasdalkitsis.gameframe.feature.ip.presenter.IpSetupPresenter
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.ip.repository.PreferenceIpRepository
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.okHttpClient
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.infra.injector.RxSharedPreferencesInjector
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider
import okhttp3.Interceptor

object IpInjector {

    private val IP_DISCOVERY_USE_CASE = IpDiscoveryUseCase(NetworkingInjector.wifiUseCase(), okHttpClient(1).build())

    fun ipNavigator(): IpNavigator = AndroidIpNavigator(topActivityProvider(), application())

    fun ipRepository(): IpRepository = PreferenceIpRepository(RxSharedPreferencesInjector.rxSharedPreferences())

    fun ipSetupPresenter() =
            IpSetupPresenter(ipRepository(), IP_DISCOVERY_USE_CASE, NetworkingInjector.wifiUseCase())

    fun ipBaseHostInterceptor(): Interceptor = IpBaseHostInterceptor(ipRepository())
}