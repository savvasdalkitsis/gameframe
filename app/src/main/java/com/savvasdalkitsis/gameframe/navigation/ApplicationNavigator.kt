package com.savvasdalkitsis.gameframe.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent

import com.savvasdalkitsis.gameframe.GameFrameApplication
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import com.savvasdalkitsis.gameframe.ip.view.IpSetupActivity

class ApplicationNavigator(private val topActivityProvider: TopActivityProvider, private val application: GameFrameApplication) : Navigator {

    override fun navigateToIpSetup() {
        context.startActivity(createIntent(IpSetupActivity::class.java))
    }

    private fun createIntent(cls: Class<*>): Intent {
        val context = context
        val intent = Intent(context, cls)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return intent
    }

    private val context: Context
        get() = topActivityProvider.topActivity ?: application
}
