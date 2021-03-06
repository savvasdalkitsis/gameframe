/**
 * Copyright 2018 Savvas Dalkitsis
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
package com.savvasdalkitsis.gameframe.feature.analytics

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseBackedAnalytics(private val application: Application) : Analytics {

    private val analytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(application) }

    override fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        analytics.logEvent(eventName, Bundle().apply {
            params.forEach { (key, value) ->
                putString(key, value)
            }
        })
    }
}

