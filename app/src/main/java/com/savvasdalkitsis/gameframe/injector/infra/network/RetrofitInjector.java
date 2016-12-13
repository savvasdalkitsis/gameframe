package com.savvasdalkitsis.gameframe.injector.infra.network;

import android.support.annotation.Nullable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.savvasdalkitsis.gameframe.injector.infra.network.InterceptorInjector.ipBaseHostInterceptor;
import static com.savvasdalkitsis.gameframe.injector.infra.network.OkHttpClientInjector.okHttpClient;

public class RetrofitInjector {

    @Nullable
    public static Retrofit retrofit() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://nothing")
                .client(okHttpClient(3)
                        .addInterceptor(ipBaseHostInterceptor())
                        .build())
                .build();
    }
}
