package com.savvasdalkitsis.gameframe.injector.usecase;

import com.savvasdalkitsis.gameframe.bmp.usecase.BmpUseCase;
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase;
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase;
import com.savvasdalkitsis.gameframe.saves.usecase.SavedDrawingUseCase;

import static com.savvasdalkitsis.gameframe.injector.ApplicationInjector.application;
import static com.savvasdalkitsis.gameframe.injector.gameframe.api.GameFrameApiInjector.gameFrameApi;
import static com.savvasdalkitsis.gameframe.injector.infra.android.AndroidInjector.wifiManager;
import static com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient;

public class UseCaseInjector {

    private static final IpDiscoveryUseCase IP_DISCOVERY_USE_CASE = new IpDiscoveryUseCase();

    public static GameFrameUseCase gameFrameUseCase() {
        return new GameFrameUseCase(okHttpClient(1).build(), wifiManager(), gameFrameApi(), ipDiscoveryUseCase());
    }

    public static IpDiscoveryUseCase ipDiscoveryUseCase() {
        return IP_DISCOVERY_USE_CASE;
    }

    public static BmpUseCase bmpUseCase() {
        return new BmpUseCase();
    }

    public static SavedDrawingUseCase savedDrawingUseCase() {
        return new SavedDrawingUseCase(bmpUseCase(), application());
    }
}
