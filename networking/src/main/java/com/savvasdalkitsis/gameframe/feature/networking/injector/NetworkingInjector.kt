package com.savvasdalkitsis.gameframe.feature.networking.injector

import android.content.Context
import android.net.wifi.WifiManager
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector

object NetworkingInjector {

    fun wifiUseCase() = WifiUseCase(wifiManager())

    private fun wifiManager() =
            ApplicationInjector.application().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

}