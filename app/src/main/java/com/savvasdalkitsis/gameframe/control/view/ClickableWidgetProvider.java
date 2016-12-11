package com.savvasdalkitsis.gameframe.control.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.widget.RemoteViews;

import com.savvasdalkitsis.gameframe.R;

public abstract class ClickableWidgetProvider extends AppWidgetProvider {

    private static final String CLICKED = "click";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layoutResId());
        ComponentName watchWidget = new ComponentName(context, getClass());

        remoteViews.setOnClickPendingIntent(R.id.view_widget_next, getPendingSelfIntent(context, CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
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
}
