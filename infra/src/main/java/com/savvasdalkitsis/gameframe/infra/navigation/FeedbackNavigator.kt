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
import android.widget.Toast
import com.savvasdalkitsis.gameframe.infra.R
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import org.rm3l.maoni.Maoni
import org.rm3l.maoni.email.MaoniEmailListener

private const val FEEDBACK_EMAIL_ADDRESS = "feedback.gameframe@gmail.com"

class FeedbackNavigator(private val topActivityProvider: TopActivityProvider, private val application: Application) {

    private val context: Context
        get() = topActivityProvider.topActivity ?: application

    fun navigateToFeedback() {
        context.let {
            if (it is Activity) {
                Maoni.Builder(it, "${it.packageName}.maoniFileProvider")
                        .withDefaultToEmailAddress(FEEDBACK_EMAIL_ADDRESS)
                        .withHeader(R.drawable.feedback_header)
                        .withListener(MaoniEmailListener(it, FEEDBACK_EMAIL_ADDRESS))
                        .build()
                        .start(it)
            } else {
                Toast.makeText(it, R.string.unknown_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
