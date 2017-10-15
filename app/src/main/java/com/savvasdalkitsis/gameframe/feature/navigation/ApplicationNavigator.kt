package com.savvasdalkitsis.gameframe.feature.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.savvasdalkitsis.gameframe.GameFrameApplication
import com.savvasdalkitsis.gameframe.feature.ip.view.IpSetupActivity
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider

class ApplicationNavigator(private val topActivityProvider: TopActivityProvider, private val application: GameFrameApplication) : Navigator {

    override fun navigateToIpSetup() {
        context.startActivity(wrap(intentForClass()))
    }

    override fun navigateToPlayStore() {
        val appPackageName = context.packageName
        try {
            context.startActivity(intentForUrl("market://details?id=$appPackageName"))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(intentForUrl("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
    }

    private fun intentForUrl(uri: String) = wrap(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

    private fun intentForClass() = Intent(context, IpSetupActivity::class.java)

    private fun wrap(intent: Intent) = intent.apply {
        val context = context
        if (context !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private val context: Context
        get() = topActivityProvider.topActivity ?: application
}
