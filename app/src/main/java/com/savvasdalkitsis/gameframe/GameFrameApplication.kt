package com.savvasdalkitsis.gameframe

import android.app.Application

import com.savvasdalkitsis.gameframe.infra.ApplicationTopActivityProvider
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector

import uk.co.chrisjenx.calligraphy.CalligraphyConfig

import com.savvasdalkitsis.gameframe.injector.infra.TopActivityProviderInjector.applicationTopActivityProvider

class GameFrameApplication : Application() {

    private val topActivityProvider = applicationTopActivityProvider()

    override fun onCreate() {
        super.onCreate()
        ApplicationInjector.injectApplication(this)
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PressStart2P.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
        registerActivityLifecycleCallbacks(topActivityProvider)
    }
}
