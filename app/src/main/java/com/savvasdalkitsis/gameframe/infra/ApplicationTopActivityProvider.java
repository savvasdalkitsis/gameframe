package com.savvasdalkitsis.gameframe.infra;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class ApplicationTopActivityProvider implements TopActivityProvider, Application.ActivityLifecycleCallbacks {

    private WeakReference<Activity> activity;

    @Nullable
    @Override
    public Activity getTopActivity() {
        return activity.get();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
