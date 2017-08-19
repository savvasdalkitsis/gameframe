package com.savvasdalkitsis.gameframe.control.view

import android.annotation.TargetApi
import android.os.Build
import android.service.quicksettings.TileService

import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector
import com.savvasdalkitsis.gameframe.widget.presenter.WidgetPresenter
import com.savvasdalkitsis.gameframe.widget.view.WidgetView

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
