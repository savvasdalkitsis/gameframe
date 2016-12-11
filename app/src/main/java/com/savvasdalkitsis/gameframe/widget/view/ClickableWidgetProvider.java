package com.savvasdalkitsis.gameframe.widget.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.widget.RemoteViews;

import com.github.andrewlord1990.snackbarbuilder.toastbuilder.ToastBuilder;
import com.savvasdalkitsis.gameframe.GameFrameApplication;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector;
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector;
import com.savvasdalkitsis.gameframe.widget.presenter.WidgetPresenter;

public abstract class ClickableWidgetProvider extends AppWidgetProvider implements WidgetView {

    private static final String CLICKED = "click";
    private final GameFrameApplication application = ApplicationInjector.application();
    protected final WidgetPresenter presenter = PresenterInjector.widgetPresenter();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layoutResId());
        ComponentName watchWidget = new ComponentName(context, getClass());

        remoteViews.setOnClickPendingIntent(R.id.view_widget_action, getPendingSelfIntent(context, CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        presenter.bindView(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (CLICKED.equals(intent.getAction())) {
            onClick();
        }
    }

    @LayoutRes
    protected abstract int layoutResId();

    protected abstract void onClick();

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void operationError() {
        new ToastBuilder(application)
                .message(R.string.error_communicating)
                .build()
                .show();
    }
}
