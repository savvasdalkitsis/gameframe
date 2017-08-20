package com.savvasdalkitsis.gameframe.injector.feature.navigation

import com.savvasdalkitsis.gameframe.feature.navigation.ApplicationNavigator
import com.savvasdalkitsis.gameframe.feature.navigation.Navigator
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector
import com.savvasdalkitsis.gameframe.injector.infra.TopActivityProviderInjector

object NavigatorInjector {

    fun navigator(): Navigator =
            ApplicationNavigator(TopActivityProviderInjector.topActivityProvider(),
                    ApplicationInjector.application())
}
