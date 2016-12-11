package com.savvasdalkitsis.gameframe.gameframe.api;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface GameFrameApi {

    @FormUrlEncoded
    @POST("command")
    @Headers("Connection: close")
    Observable<Void> command(@FieldMap(encoded = true) Map<String, String> fields);

    @GET("set")
    @Headers("Connection: close")
    Observable<Void> set(@QueryMap() Map<String, String> params);
}