package com.savvasdalkitsis.gameframe.feature.ip

import com.savvasdalkitsis.gameframe.feature.ip.navigation.AndroidIpNavigator
import com.savvasdalkitsis.gameframe.feature.ip.navigation.IpNavigator
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application

object IpInjector {

    fun ipNavigator(): IpNavigator = AndroidIpNavigator(topActivityProvider(), application())
}