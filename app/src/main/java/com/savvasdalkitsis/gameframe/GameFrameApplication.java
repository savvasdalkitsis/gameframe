package com.savvasdalkitsis.gameframe;

import android.app.Application;

import com.savvasdalkitsis.gameframe.infra.ApplicationTopActivityProvider;
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector;

import static com.savvasdalkitsis.gameframe.injector.infra.TopActivityProviderInjector.applicationTopActivityProvider;

public class GameFrameApplication extends Application {

    private final ApplicationTopActivityProvider topActivityProvider = applicationTopActivityProvider();

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInjector.injectApplication(this);
        registerActivityLifecycleCallbacks(topActivityProvider);
    }
}
