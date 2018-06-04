/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.navigation.usecase

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import android.util.Log
import com.savvasdalkitsis.gameframe.feature.analytics.injector.AnalyticsInjector
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import io.reactivex.Completable
import java.io.File

open class AndroidNavigator(private val topActivityProvider: TopActivityProvider, private val application: Application) : Navigator {

    private val analytics = AnalyticsInjector.analytics()

    override fun navigateToAccount() {
        analytics.logEvent("navigation", "target" to "account")
        context.startActivity(wrap(gameFrameNavigation("account")))
    }

    override fun navigateToIpSetup() {
        analytics.logEvent("navigation", "target" to "ip_setup")
        context.startActivity(wrap(gameFrameNavigation("ip_setup")))
    }

    override fun navigateToPlayStore() {
        analytics.logEvent("navigation", "target" to "play_store")
        val appPackageName = context.packageName
        try {
            context.startActivity(intentForUrl("market://details?id=$appPackageName"))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(intentForUrl("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
    }

    override fun navigateToShareImageFile(file: File, name: String): Completable = Completable.create { emitter ->
        analytics.logEvent("navigation", "target" to "share_image")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            context.startActivity(Intent.createChooser(intent, name))
            emitter.onComplete()
        } catch (e: android.content.ActivityNotFoundException) {
            Log.w(AndroidNavigator::class.java.name, "Could not share image", e)
            emitter.onError(e)
        }
    }

    private fun intentForUrl(uri: String) = wrap(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

    private fun wrap(intent: Intent) = intent.apply {
        val context = context
        if (context !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun gameFrameNavigation(navigationTarget: String) =
            Intent(Intent.ACTION_VIEW, Uri.parse("gameframe://navigation/$navigationTarget")).apply {
                `package` = context.packageName
            }

    private val context: Context
        get() = topActivityProvider.topActivity ?: application
}
