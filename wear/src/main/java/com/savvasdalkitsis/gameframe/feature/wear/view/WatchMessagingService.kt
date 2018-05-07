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
package com.savvasdalkitsis.gameframe.feature.wear.view

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.savvasdalkitsis.gameframe.feature.wear.R
import com.savvasdalkitsis.gameframe.feature.injector.WidgetInjector
import com.savvasdalkitsis.gameframe.feature.widget.view.WidgetView
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector

class WatchMessagingService: WearableListenerService(), WidgetView {

    private val presenter = WidgetInjector.widgetPresenter()
    private val messageDisplay = MessageDisplayInjector.toastMessageDisplay()

    override fun onCreate() {
        super.onCreate()
        presenter.bindView(this)
    }

    override fun onMessageReceived(message: MessageEvent) {
        when (String(message.data)) {
            "power" -> presenter.power()
            "menu" -> presenter.menu()
            "next" -> presenter.next()
        }
    }

    override fun operationError() {
        messageDisplay.show(R.string.error_communicating)
    }
}