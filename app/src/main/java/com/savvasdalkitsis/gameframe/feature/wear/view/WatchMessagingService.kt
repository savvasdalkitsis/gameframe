package com.savvasdalkitsis.gameframe.feature.wear.view

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.widget.view.WidgetView
import com.savvasdalkitsis.gameframe.injector.feature.message.MessageDisplayInjector
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector

class WatchMessagingService: WearableListenerService(), WidgetView {

    private val presenter = PresenterInjector.widgetPresenter()
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