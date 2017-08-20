package com.savvasdalkitsis.gameframe.feature.widget.view

import com.savvasdalkitsis.gameframe.R

class PowerWidgetProvider : ClickableWidgetProvider() {

    override fun layoutResId() = R.layout.widget_power

    override fun onClick() {
        presenter.power()
    }
}
