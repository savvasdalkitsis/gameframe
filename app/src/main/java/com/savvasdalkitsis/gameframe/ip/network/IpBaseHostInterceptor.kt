package com.savvasdalkitsis.gameframe.ip.network

import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response
import rx.Observable

class IpBaseHostInterceptor(private val ipRepository: IpRepository) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val ipAddress = ipRepository.ipAddress
                .onErrorResumeNext(Observable.just<IpAddress>(null))
                .toBlocking()
                .first() ?: return chain.proceed(chain.request())
        val request = chain.request()
        return chain.proceed(request
                .newBuilder()
                .url(request.url().newBuilder()
                        .host(ipAddress.toString())
                        .build())
                .build())
    }
}
