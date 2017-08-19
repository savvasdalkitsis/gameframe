package com.savvasdalkitsis.gameframe.injector

import com.savvasdalkitsis.gameframe.GameFrameApplication

object ApplicationInjector {

    private var gameFrameApplication: GameFrameApplication? = null

    fun injectApplication(gameFrameApplication: GameFrameApplication) {
        ApplicationInjector.gameFrameApplication = gameFrameApplication
    }

    fun application(): GameFrameApplication = gameFrameApplication!!
}
