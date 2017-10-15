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
import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector

@TargetApi(Build.VERSION_CODES.N)
class PowerTileService : TileService(), WidgetView {

    private val presenter = PresenterInjector.widgetPresenter()

    override fun onCreate() {
        super.onCreate()
        presenter.bindView(this)
    }

    override fun onClick() {
        super.onClick()
        presenter.power()
    }

    override fun operationError() = ToastBuilder(this)
                .message(R.string.error_communicating)
                .build()
                .show()
}
