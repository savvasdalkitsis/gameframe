package com.savvasdalkitsis.gameframe;

import android.app.Application;

import com.savvasdalkitsis.gameframe.infra.ApplicationTopActivityProvider;
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.savvasdalkitsis.gameframe.injector.infra.TopActivityProviderInjector.applicationTopActivityProvider;

public class GameFrameApplication extends Application {

    private final ApplicationTopActivityProvider topActivityProvider = applicationTopActivityProvider();

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInjector.injectApplication(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PressStart2P.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        registerActivityLifecycleCallbacks(topActivityProvider);
    }
}
