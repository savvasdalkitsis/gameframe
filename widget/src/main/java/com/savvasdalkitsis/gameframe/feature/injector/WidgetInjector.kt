package com.savvasdalkitsis.gameframe.feature.injector

import com.savvasdalkitsis.gameframe.feature.device.injector.DeviceInjector.gameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipNavigator
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipRepository
import com.savvasdalkitsis.gameframe.feature.widget.presenter.WidgetPresenter

object WidgetInjector {

    fun widgetPresenter() =
            WidgetPresenter(gameFrameUseCase(), ipRepository(), ipNavigator())
}