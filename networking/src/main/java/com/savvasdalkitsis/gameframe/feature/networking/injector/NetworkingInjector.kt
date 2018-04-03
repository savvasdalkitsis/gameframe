package com.savvasdalkitsis.gameframe.feature.networking.injector

import android.content.Context
import android.net.wifi.WifiManager
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object NetworkingInjector {

    fun wifiUseCase() = WifiUseCase(wifiManager())

    fun okHttpClient(timeout: Int, interceptor: Interceptor? = null): OkHttpClient.Builder {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(timeout.toLong(), TimeUnit.SECONDS).apply {
                    interceptor?.let { addInterceptor(it) }
                    addInterceptor(httpLoggingInterceptor)
                }
    }

    private fun wifiManager() =
            ApplicationInjector.application().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

}