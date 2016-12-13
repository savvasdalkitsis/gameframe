package com.savvasdalkitsis.gameframe.gameframe.api;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface GameFrameApi {

    @FormUrlEncoded
    @POST("command")
    @Headers("Connection: close")
    Observable<CommandResponse> command(@FieldMap(encoded = true) Map<String, String> fields);

    @GET("set")
    @Headers("Connection: close")
    Observable<Void> set(@QueryMap() Map<String, String> params);

    @Multipart
    @POST("upload")
    @Headers("Connection: close")
    Observable<CommandResponse> upload(@Part("my_file[]") MultipartBody.Part file);
}