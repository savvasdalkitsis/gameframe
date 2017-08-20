package com.savvasdalkitsis.gameframe.feature.ip.network

import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class IpBaseHostInterceptor(private val ipRepository: IpRepository) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val ipAddress = ipRepository.ipAddress
                .toMaybe()
                .onErrorComplete()
                .blockingGet() ?: return chain.proceed(chain.request())
        val request = chain.request()
        return chain.proceed(request
                .newBuilder()
                .url(request.url().newBuilder()
                        .host(ipAddress.toString())
                        .build())
                .build())
    }
}
