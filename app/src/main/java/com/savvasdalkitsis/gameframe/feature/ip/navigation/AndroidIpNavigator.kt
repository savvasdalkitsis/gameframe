package com.savvasdalkitsis.gameframe.feature.ip.navigation

import android.app.Application
import com.savvasdalkitsis.gameframe.feature.ip.view.IpSetupActivity
import com.savvasdalkitsis.gameframe.infra.navigation.AndroidNavigator
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider

class AndroidIpNavigator(topActivityProvider: TopActivityProvider, application: Application) : IpNavigator, AndroidNavigator(topActivityProvider, application) {

    override fun navigateToIpSetup() {
        context.startActivity(wrap(intentForClass(IpSetupActivity::class)))
    }
}