package com.savvasdalkitsis.gameframe.widget.view;

import com.savvasdalkitsis.gameframe.R;

public class NextWidgetProvider extends ClickableWidgetProvider {

    @Override
    protected int layoutResId() {
        return R.layout.widget_next;
    }

    @Override
    protected void onClick() {
        presenter.next();
    }
}
