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
package com.savvasdalkitsis.gameframe.infra.navigation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import kotlin.reflect.KClass

open class AndroidNavigator(private val topActivityProvider: TopActivityProvider, private val application: Application) {

    fun intentForUrl(uri: String) = wrap(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

    fun <T : Activity> intentForClass(clazz: KClass<T>) = Intent(context, clazz.java)

    fun wrap(intent: Intent) = intent.apply {
        val context = context
        if (context !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    val context: Context
        get() = topActivityProvider.topActivity ?: application
}
