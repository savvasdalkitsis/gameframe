package com.savvasdalkitsis.gameframe.widget.view;

import com.savvasdalkitsis.gameframe.R;

public class PowerWidgetProvider extends ClickableWidgetProvider {

    @Override
    protected int layoutResId() {
        return R.layout.widget_power;
    }

    @Override
    protected void onClick() {
        presenter.power();
    }
}
