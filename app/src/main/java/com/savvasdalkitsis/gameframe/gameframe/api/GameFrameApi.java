package com.savvasdalkitsis.gameframe.gameframe.api;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface GameFrameApi {

    @FormUrlEncoded
    @POST("command")
    Observable<Void> togglePower(@Field("power") String empty);

    @FormUrlEncoded
    @POST("command")
    Observable<Void> menu(@Field("menu") String empty);

    @FormUrlEncoded
    @POST("command")
    Observable<Response<Void>> ping(@Field("next") String empty);
}
