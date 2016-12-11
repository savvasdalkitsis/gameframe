package com.savvasdalkitsis.gameframe.control.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.quicksettings.TileService;

import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector;
import com.savvasdalkitsis.gameframe.widget.presenter.WidgetPresenter;
import com.savvasdalkitsis.gameframe.widget.view.WidgetView;

@TargetApi(Build.VERSION_CODES.N)
public class PowerTileService extends TileService implements WidgetView {

    private final WidgetPresenter widgetPresenter = PresenterInjector.widgetPresenter();

    @Override
    public void onCreate() {
        super.onCreate();
        widgetPresenter.bindView(this);
    }

    @Override
    public void onClick() {
        super.onClick();
        widgetPresenter.power();
    }

    @Override
    public void operationError() {
        new ToastBuilder(this)
                .message(R.string.error_communicating)
                .build()
                .show();
    }
}
