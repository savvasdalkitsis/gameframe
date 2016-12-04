package com.savvasdalkitsis.gameframe.injector.infra;

import com.savvasdalkitsis.gameframe.infra.ApplicationTopActivityProvider;
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider;

public class TopActivityProviderInjector {

    private static final ApplicationTopActivityProvider topActivityProvider = new ApplicationTopActivityProvider();

    public static TopActivityProvider topActivityProvider() {
        return topActivityProvider;
    }

    public static ApplicationTopActivityProvider applicationTopActivityProvider() {
        return topActivityProvider;
    }
}
