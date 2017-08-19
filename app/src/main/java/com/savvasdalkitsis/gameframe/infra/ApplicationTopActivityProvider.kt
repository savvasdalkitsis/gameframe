package com.savvasdalkitsis.gameframe.infra

import android.app.Activity
import android.app.Application
import android.os.Bundle

import java.lang.ref.WeakReference

class ApplicationTopActivityProvider : TopActivityProvider, Application.ActivityLifecycleCallbacks {

    private var activity: WeakReference<Activity>? = null

    override val topActivity: Activity?
        get() =  activity?.get()

    override fun onActivityStarted(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
