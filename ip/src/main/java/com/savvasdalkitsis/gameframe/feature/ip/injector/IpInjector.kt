package com.savvasdalkitsis.gameframe.feature.ip.injector

import com.savvasdalkitsis.gameframe.feature.ip.navigation.AndroidIpNavigator
import com.savvasdalkitsis.gameframe.feature.ip.navigation.IpNavigator
import com.savvasdalkitsis.gameframe.feature.ip.presenter.IpSetupPresenter
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.ip.repository.PreferenceIpRepository
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.okHttpClient
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.infra.injector.RxSharedPreferencesInjector
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider

object IpInjector {

    private val IP_DISCOVERY_USE_CASE = IpDiscoveryUseCase(NetworkingInjector.wifiUseCase(), okHttpClient(1).build())

    fun ipNavigator(): IpNavigator = AndroidIpNavigator(topActivityProvider(), application())

    fun ipRepository(): IpRepository = PreferenceIpRepository(RxSharedPreferencesInjector.rxSharedPreferences())

    fun ipSetupPresenter() =
            IpSetupPresenter(ipRepository(), IP_DISCOVERY_USE_CASE, NetworkingInjector.wifiUseCase())
}