package com.savvasdalkitsis.gameframe.injector.gameframe.api;

import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi;

import static com.savvasdalkitsis.gameframe.injector.infra.network.RetrofitInjector.retrofit;

public class GameFrameApiInjector {

    public static GameFrameApi gameFrameApi() {
        return retrofit().create(GameFrameApi.class);
    }

}
