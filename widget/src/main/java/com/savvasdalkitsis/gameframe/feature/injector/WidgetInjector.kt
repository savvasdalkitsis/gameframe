package com.savvasdalkitsis.gameframe.feature.injector

import com.savvasdalkitsis.gameframe.feature.device.injector.DeviceInjector.deviceCase
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipNavigator
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipRepository
import com.savvasdalkitsis.gameframe.feature.widget.presenter.WidgetPresenter

object WidgetInjector {

    fun widgetPresenter() =
            WidgetPresenter(deviceCase(), ipRepository(), ipNavigator())
}