package com.savvasdalkitsis.gameframe.widget.view

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
