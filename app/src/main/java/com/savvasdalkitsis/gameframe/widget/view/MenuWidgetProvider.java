package com.savvasdalkitsis.gameframe.widget.view;

import com.savvasdalkitsis.gameframe.R;

public class MenuWidgetProvider extends ClickableWidgetProvider {

    @Override
    protected int layoutResId() {
        return R.layout.widget_menu;
    }

    @Override
    protected void onClick() {
        presenter.menu();
    }
}
