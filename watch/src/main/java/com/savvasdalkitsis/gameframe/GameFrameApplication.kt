package com.savvasdalkitsis.gameframe

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class GameFrameApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PressStart2P.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}
