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
package com.savvasdalkitsis.gameframe.feature.widget.view

import android.annotation.TargetApi
import android.os.Build
import android.service.quicksettings.TileService
import com.savvasdalkitsis.gameframe.feature.widget.R
import com.savvasdalkitsis.gameframe.feature.injector.WidgetInjector
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector

@TargetApi(Build.VERSION_CODES.N)
class PowerTileService : TileService(), WidgetView {

    private val presenter = WidgetInjector.widgetPresenter()
    private val messageDisplay = MessageDisplayInjector.toastMessageDisplay()

    override fun onCreate() {
        super.onCreate()
        presenter.bindView(this)
    }

    override fun onClick() {
        super.onClick()
        presenter.power()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stop()
    }

    override fun operationError() {
        messageDisplay.show(R.string.error_communicating)
    }
}
