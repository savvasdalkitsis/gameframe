package com.savvasdalkitsis.gameframe.widget.view

import com.savvasdalkitsis.gameframe.R

class MenuWidgetProvider : ClickableWidgetProvider() {

    override fun layoutResId() = R.layout.widget_menu

    override fun onClick() {
        presenter.menu()
    }
}
