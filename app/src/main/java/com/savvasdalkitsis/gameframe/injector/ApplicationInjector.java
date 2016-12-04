package com.savvasdalkitsis.gameframe.injector;

import com.savvasdalkitsis.gameframe.GameFrameApplication;

public class ApplicationInjector {

    private static GameFrameApplication gameFrameApplication;

    public static void injectApplication(GameFrameApplication gameFrameApplication) {
        ApplicationInjector.gameFrameApplication = gameFrameApplication;
    }

    public static GameFrameApplication application() {
        return gameFrameApplication;
    }
}
