package com.savvasdalkitsis.gameframe.rx

import android.util.Log
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector
import com.savvasdalkitsis.gameframe.ip.model.IpBaseHostMissingException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object RxTransformers {

    private val navigator = NavigatorInjector.navigator()

    fun <T> interceptIpMissingException() = Observable.Transformer<T, T> { o ->
        o.doOnError {
            if (it is IpBaseHostMissingException) {
                Log.e(RxTransformers::class.java.name, "Error: ", it)
                navigator.navigateToIpSetup()
            }
        }
    }

    fun <T> schedulers() = Observable.Transformer<T, T> { o ->
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
