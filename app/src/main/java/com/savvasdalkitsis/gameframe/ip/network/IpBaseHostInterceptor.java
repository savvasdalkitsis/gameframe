package com.savvasdalkitsis.gameframe.ip.network;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.model.IpBaseHostMissingException;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

public class IpBaseHostInterceptor implements Interceptor {

    private final IpRepository ipRepository;

    public IpBaseHostInterceptor(IpRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        IpAddress ipAddress = ipRepository.getIpAddress()
                .onErrorResumeNext(Observable.just(null))
                .toBlocking()
                .first();
        if (ipAddress == null) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request();
        return chain.proceed(request
                .newBuilder()
                .url(request.url().newBuilder()
                        .host(ipAddress.toString())
                        .build())
                .build());
    }
}
