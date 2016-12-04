package com.savvasdalkitsis.gameframe.injector.infra.network;

import com.savvasdalkitsis.gameframe.ip.network.IpBaseHostInterceptor;

import okhttp3.Interceptor;

import static com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector.ipRepository;

public class InterceptorInjector {

    static Interceptor ipBaseHostInterceptor() {
        return new IpBaseHostInterceptor(ipRepository());
    }
}
