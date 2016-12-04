package com.savvasdalkitsis.gameframe.injector.infra.network;

import android.support.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static com.savvasdalkitsis.gameframe.injector.infra.network.InterceptorInjector.ipBaseHostInterceptor;

public class RetrofitInjector {

    @Nullable
    public static Retrofit retrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://nothing")
                .client(new OkHttpClient.Builder()
                        .addInterceptor(ipBaseHostInterceptor())
                        .addInterceptor(httpLoggingInterceptor)
                        .build())
                .build();
    }

}
