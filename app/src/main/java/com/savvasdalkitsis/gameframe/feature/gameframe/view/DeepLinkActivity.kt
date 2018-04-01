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
package com.savvasdalkitsis.gameframe.feature.gameframe.view

import android.app.Activity
import android.os.Bundle
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector
import com.savvasdalkitsis.gameframe.feature.widget.view.WidgetView
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector

class DeepLinkActivity : Activity(), WidgetView {

    private val presenter = PresenterInjector.widgetPresenter()
    private val messageDisplay = MessageDisplayInjector.toastMessageDisplay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.bindView(this)
        when (intent.data.host) {
            "power" -> presenter.power()
            "next" -> presenter.next()
            "menu" -> presenter.menu()
        }
        finish()
    }

    override fun operationError() {
        messageDisplay.show(R.string.error_communicating)
    }
}