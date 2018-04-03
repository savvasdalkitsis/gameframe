package com.savvasdalkitsis.gameframe.feature.account.injector

import com.savvasdalkitsis.gameframe.feature.account.navigation.AccountNavigator
import com.savvasdalkitsis.gameframe.feature.account.navigation.AndroidAccountNavigator
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application

object AccountInjector {

    fun accountNavigator(): AccountNavigator = AndroidAccountNavigator(topActivityProvider(), application())
}