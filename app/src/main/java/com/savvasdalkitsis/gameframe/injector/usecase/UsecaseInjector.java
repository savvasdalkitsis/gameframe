package com.savvasdalkitsis.gameframe.injector.usecase;

import android.support.annotation.NonNull;

import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase;
import com.savvasdalkitsis.gameframe.usecase.GameFrameUseCase;

import static com.savvasdalkitsis.gameframe.injector.gameframe.api.GameFrameApiInjector.gameFrameApi;
import static com.savvasdalkitsis.gameframe.injector.infra.android.AndroidInjector.wifiManager;
import static com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient;

public class UseCaseInjector {

    private static final IpDiscoveryUseCase IP_DISCOVERY_USE_CASE = new IpDiscoveryUseCase();

    @NonNull
    public static GameFrameUseCase gameFrameUseCase() {
        return new GameFrameUseCase(okHttpClient(1).build(), wifiManager(), gameFrameApi(), ipDiscoveryUseCase());
    }

    public static IpDiscoveryUseCase ipDiscoveryUseCase() {
        return IP_DISCOVERY_USE_CASE;
    }
}
