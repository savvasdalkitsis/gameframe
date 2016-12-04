package com.savvasdalkitsis.gameframe.usecase;

import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;

import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.model.IpNotFoundException;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

import static com.savvasdalkitsis.gameframe.ip.model.IpAddress.Builder.ipAddress;
import static java.util.Collections.singletonMap;

public class GameFrameUseCase {

    private final OkHttpClient okHttpClient;
    private final WifiManager wifiManager;
    private final GameFrameApi gameFrameApi;

    public GameFrameUseCase(OkHttpClient okHttpClient, WifiManager wifiManager, GameFrameApi gameFrameApi) {
        this.okHttpClient = okHttpClient;
        this.wifiManager = wifiManager;
        this.gameFrameApi = gameFrameApi;
    }

    public Observable<Void> togglePower() {
        return gameFrameApi.command(param("power"));
    }

    public Observable<Void> menu() {
        return gameFrameApi.command(param("menu"));
    }

    public Observable<Void> next() {
        return gameFrameApi.command(param("next"));
    }

    public Observable<IpAddress> discoverGameFrameIp() {
        return Observable.defer(this::getDeviceIp)
                .flatMap(wholePart4Subrange())
                .concatMap(ip -> {
                    try {
                        if (isFromGameFrame(ping(ip))) {
                            return Observable.just(ip);
                        }
                    } catch (IOException e) {
                        Log.w(GameFrameUseCase.class.getName(), "Error trying to call " + ip, e);
                    }
                    return Observable.empty();
                })
                .first()
                .onErrorResumeNext(e -> Observable.error(new IpNotFoundException("Game Frame IP not found", e)));
    }

    private Func1<IpAddress, Observable<? extends IpAddress>> wholePart4Subrange() {
        return ip -> Observable.range(0, 256)
                .map(String::valueOf)
                .map(part -> ipAddress(ip).part4(part).build());
    }

    @SuppressWarnings("deprecation")
    private Observable<IpAddress> getDeviceIp() {
        String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        return Observable.just(IpAddress.parse(ip));
    }

    private boolean isFromGameFrame(Response response) {
        return response.isSuccessful()
                && response.headers().get("Server").startsWith("Webduino/");
    }

    private Response ping(IpAddress ip) throws IOException {
        return okHttpClient.newCall(new Request.Builder()
                .url("http://" + ip.toString() + "/command")
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "next="))
                .build()).execute();
    }

    @NonNull
    private Map<String, String> param(String name) {
        return singletonMap(name, "");
    }
}