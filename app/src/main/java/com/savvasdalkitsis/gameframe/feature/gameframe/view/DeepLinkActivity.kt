package com.savvasdalkitsis.gameframe.feature.gameframe.view

import android.app.Activity
import android.os.Bundle
import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.widget.view.WidgetView
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector

class DeepLinkActivity : Activity(), WidgetView {

    private val presenter = PresenterInjector.widgetPresenter()

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
        ToastBuilder(this)
                .message(R.string.error_communicating)
                .build()
                .show()
    }
}