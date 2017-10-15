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

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.annotation.LayoutRes
import android.widget.RemoteViews

import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector

abstract class ClickableWidgetProvider : AppWidgetProvider(), WidgetView {

    private val application = ApplicationInjector.application()
    protected val presenter = PresenterInjector.widgetPresenter()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val remoteViews = RemoteViews(context.packageName, layoutResId())
        val watchWidget = ComponentName(context, javaClass)

        remoteViews.setOnClickPendingIntent(R.id.view_widget_action, getPendingSelfIntent(context, CLICKED))
        appWidgetManager.updateAppWidget(watchWidget, remoteViews)
        presenter.bindView(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (CLICKED == intent.action) {
            onClick()
        }
    }

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun onClick()

    private fun getPendingSelfIntent(context: Context, intentAction: String) =
            PendingIntent.getBroadcast(context, 0,
                    Intent(context, javaClass).apply { action = intentAction }, 0)

    override fun operationError() {
        ToastBuilder(application)
                .message(R.string.error_communicating)
                .build()
                .show()
    }

    companion object {

        private val CLICKED = "click"
    }
}
