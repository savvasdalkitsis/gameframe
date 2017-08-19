package com.savvasdalkitsis.gameframe.injector.infra.network

import com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector.ipRepository
import com.savvasdalkitsis.gameframe.ip.network.IpBaseHostInterceptor
import okhttp3.Interceptor

object InterceptorInjector {

    internal fun ipBaseHostInterceptor(): Interceptor = IpBaseHostInterceptor(ipRepository())
}
