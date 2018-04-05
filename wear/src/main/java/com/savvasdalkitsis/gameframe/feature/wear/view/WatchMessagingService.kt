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