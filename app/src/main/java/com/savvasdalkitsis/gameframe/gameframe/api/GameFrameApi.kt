package com.savvasdalkitsis.gameframe.gameframe.api

import okhttp3.MultipartBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.QueryMap
import rx.Observable

interface GameFrameApi {

    @FormUrlEncoded
    @POST("command")
    @Headers("Connection: close")
    fun command(@FieldMap(encoded = true) fields: Map<String, String>): Observable<CommandResponse>

    @GET("set")
    @Headers("Connection: close")
    fun set(@QueryMap params: Map<String, String>): Observable<Void>

    @Multipart
    @POST("upload")
    @Headers("Connection: close")
    fun upload(@Part file: MultipartBody.Part): Observable<CommandResponse>
}