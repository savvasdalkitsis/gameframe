package com.savvasdalkitsis.gameframe.feature.gameframe.api

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface GameFrameApi {

    @FormUrlEncoded
    @POST("command")
    @Headers("Connection: close")
    fun command(@FieldMap(encoded = true) fields: Map<String, String>): Single<CommandResponse>

    @GET("set")
    @Headers("Connection: close")
    fun set(@QueryMap params: Map<String, String>): Completable

    @Multipart
    @POST("upload")
    @Headers("Connection: close")
    fun upload(@Part file: MultipartBody.Part): Single<CommandResponse>
}