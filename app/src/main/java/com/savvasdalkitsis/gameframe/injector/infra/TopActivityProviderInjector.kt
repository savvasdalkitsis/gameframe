package com.savvasdalkitsis.gameframe.injector.infra

import com.savvasdalkitsis.gameframe.infra.ApplicationTopActivityProvider
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider

object TopActivityProviderInjector {

    private val topActivityProvider = ApplicationTopActivityProvider()

    fun topActivityProvider(): TopActivityProvider = topActivityProvider

    fun applicationTopActivityProvider(): ApplicationTopActivityProvider = topActivityProvider
}
