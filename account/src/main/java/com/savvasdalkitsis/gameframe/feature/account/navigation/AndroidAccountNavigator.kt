package com.savvasdalkitsis.gameframe.feature.account.navigation

import android.app.Application
import com.savvasdalkitsis.gameframe.feature.account.view.AccountActivity
import com.savvasdalkitsis.gameframe.infra.navigation.AndroidNavigator
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider

class AndroidAccountNavigator(topActivityProvider: TopActivityProvider, application: Application): AccountNavigator, AndroidNavigator(topActivityProvider, application) {

    override fun navigateToAccount() {
        context.startActivity(wrap(intentForClass(AccountActivity::class)))
    }
}