package com.savvasdalkitsis.gameframe.gameframe.usecase;

import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Log;

import com.savvasdalkitsis.gameframe.gameframe.api.CommandResponse;
import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.model.IpNotFoundException;
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase;
import com.savvasdalkitsis.gameframe.model.Brightness;
import com.savvasdalkitsis.gameframe.model.ClockFace;
import com.savvasdalkitsis.gameframe.model.CycleInterval;
import com.savvasdalkitsis.gameframe.model.DisplayMode;
import com.savvasdalkitsis.gameframe.model.PlaybackMode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private final IpDiscoveryUseCase ipDiscoveryUseCase;

    public GameFrameUseCase(OkHttpClient okHttpClient, WifiManager wifiManager, GameFrameApi gameFrameApi, IpDiscoveryUseCase ipDiscoveryUseCase) {
        this.okHttpClient = okHttpClient;
        this.wifiManager = wifiManager;
        this.gameFrameApi = gameFrameApi;
        this.ipDiscoveryUseCase = ipDiscoveryUseCase;
    }

    public Observable<Void> togglePower() {
        return gameFrameApi.command(param("power")).compose(mapResponse());
    }

    public Observable<Void> menu() {
        return gameFrameApi.command(param("menu")).compose(mapResponse());
    }

    public Observable<Void> next() {
        return gameFrameApi.command(param("next")).compose(mapResponse());
    }

    public Observable<Void> setBrightness(Brightness brightness) {
        return gameFrameApi.set(param(brightness.getQueryParamName()));
    }

    public Observable<Void> setPlaybackMode(PlaybackMode playbackMode) {
        return gameFrameApi.set(param(playbackMode.getQueryParamName()));
    }

    public Observable<Void> setCycleInterval(CycleInterval cycleInterval) {
        return gameFrameApi.set(param(cycleInterval.getQueryParamName()));
    }

    public Observable<Void> setDisplayMode(DisplayMode displayMode) {
        return gameFrameApi.set(param(displayMode.getQueryParamName()));
    }

    public Observable<Void> setClockFace(ClockFace clockFace) {
        return gameFrameApi.set(param(clockFace.getQueryParamName()));
    }

    public Observable<CreateFolderResponse> createFolder(String name) {
        return gameFrameApi.command(singletonMap("mkdir", name))
                .flatMap(response -> {
                    if (isSuccess(response)) {
                        return Observable.just(CreateFolderResponse.SUCCESS);
                    } else if (response.getMessage().contains("exists")) {
                        return Observable.just(CreateFolderResponse.ALREADY_EXISTS);
                    } else {
                        return Observable.error(wrap(response));
                    }
                });
    }

    public Observable<Void> removeFolder(String name) {
        return gameFrameApi.command(singletonMap("rmdir", name)).compose(mapResponse());
    }

    public Observable<Void> uploadFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/bmp"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("my_file[]", "0.bmp", requestFile);
        return gameFrameApi.upload(filePart).compose(mapResponse());
    }

    public Observable<IpAddress> discoverGameFrameIp() {
        return Observable.defer(this::getDeviceIp)
                .flatMap(wholePart4Subrange())
                .concatMap(ip -> {
                    ipDiscoveryUseCase.emitMonitoredAddress(ip);
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

    private Observable.Transformer<CommandResponse, Void> mapResponse() {
        return o -> o
                .flatMap(response -> {
                    if (isSuccess(response)) {
                        return Observable.just(null);
                    } else {
                        return Observable.error(wrap(response));
                    }
                });
    }

    private GameFrameCommandError wrap(CommandResponse response) {
        return new GameFrameCommandError("Command was not successful. response: " + response);
    }

    private boolean isSuccess(CommandResponse response) {
        return "status".equals(response.getStatus());
    }
}
