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
package com.savvasdalkitsis.gameframe.feature.navigation

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.savvasdalkitsis.gameframe.GameFrameApplication
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.account.view.AccountActivity
import com.savvasdalkitsis.gameframe.feature.ip.view.IpSetupActivity
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import io.reactivex.Completable
import org.rm3l.maoni.Maoni
import java.io.File
import kotlin.reflect.KClass

class ApplicationNavigator(private val topActivityProvider: TopActivityProvider, private val application: Application) : Navigator {

    override fun navigateToIpSetup() {
        context.startActivity(wrap(intentForClass(IpSetupActivity::class)))
    }

    override fun navigateToPlayStore() {
        val appPackageName = context.packageName
        try {
            context.startActivity(intentForUrl("market://details?id=$appPackageName"))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(intentForUrl("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
    }

    override fun navigateToShareImageFile(file: File, name: String): Completable = Completable.create { emitter ->
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            context.startActivity(Intent.createChooser(intent, name))
            emitter.onComplete()
        } catch (e: android.content.ActivityNotFoundException) {
            Log.w(ApplicationNavigator::class.java.name, "Could not share image", e)
            emitter.onError(e)
        }
    }

    override fun navigateToFeedback() {
        context.let {
            if (it is Activity) {
                Maoni.Builder(it, "$it.fileProvider")
                        .withDefaultToEmailAddress("feedback.gameframe@gmail.com")
                        .withHeader(R.drawable.feedback_header)
                        .build()
                        .start(it)
            } else {
                Toast.makeText(it, R.string.unknown_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun navigateToAccount() {
        context.startActivity(wrap(intentForClass(AccountActivity::class)))
    }

    private fun intentForUrl(uri: String) = wrap(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

    private fun <T : Activity> intentForClass(clazz: KClass<T>) = Intent(context, clazz.java)

    private fun wrap(intent: Intent) = intent.apply {
        val context = context
        if (context !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private val context: Context
        get() = topActivityProvider.topActivity ?: application
}
