package com.savvasdalkitsis.gameframe.injector.infra.network

import com.savvasdalkitsis.gameframe.injector.infra.network.InterceptorInjector.ipBaseHostInterceptor
import com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInjector {

    fun retrofit(): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://nothing")
            .client(okHttpClient(3)
                    .addInterceptor(ipBaseHostInterceptor())
                    .build())
            .build()
}
