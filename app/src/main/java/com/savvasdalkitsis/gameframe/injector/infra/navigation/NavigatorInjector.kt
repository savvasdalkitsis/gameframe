package com.savvasdalkitsis.gameframe.injector.infra.navigation

import com.savvasdalkitsis.gameframe.infra.navigation.ApplicationNavigator
import com.savvasdalkitsis.gameframe.infra.navigation.Navigator
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector
import com.savvasdalkitsis.gameframe.injector.infra.TopActivityProviderInjector

object NavigatorInjector {

    fun navigator(): Navigator =
            ApplicationNavigator(TopActivityProviderInjector.topActivityProvider(),
                    ApplicationInjector.application())
}
