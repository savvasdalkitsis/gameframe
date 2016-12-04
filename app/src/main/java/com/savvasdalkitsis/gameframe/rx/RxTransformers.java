package com.savvasdalkitsis.gameframe.rx;

import android.util.Log;

import com.savvasdalkitsis.gameframe.infra.navigation.Navigator;
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector;
import com.savvasdalkitsis.gameframe.ip.model.IpBaseHostMissingException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxTransformers {

    private static final Navigator navigator = NavigatorInjector.navigator();

    public static <T> Observable.Transformer<T, T> interceptIpMissingException() {
        return o -> o
                .doOnError(error -> {
                    if (error instanceof IpBaseHostMissingException) {
                        Log.e(RxTransformers.class.getName(), "Error: ", error);
                        navigator.navigateToIpSetup();
                    }
                });
    }

    public static <T> Observable.Transformer<T, T> schedulers() {
        return o -> o
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
