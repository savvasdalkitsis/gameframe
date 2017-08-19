package com.savvasdalkitsis.gameframe.injector.infra.android

import android.content.Context
import android.net.wifi.WifiManager

import com.savvasdalkitsis.gameframe.injector.ApplicationInjector

object AndroidInjector {

    fun wifiManager() =
            ApplicationInjector.application().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
}
