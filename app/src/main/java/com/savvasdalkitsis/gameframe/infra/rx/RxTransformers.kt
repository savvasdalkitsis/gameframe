package com.savvasdalkitsis.gameframe.infra.rx

import android.util.Log
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector
import com.savvasdalkitsis.gameframe.ip.model.IpBaseHostMissingException
import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxTransformers {

    private val navigator = NavigatorInjector.navigator()

    fun interceptIpMissingException() = CompletableTransformer { c ->
        c.doOnError {
            if (it is IpBaseHostMissingException) {
                Log.e(RxTransformers::class.java.name, "Error: ", it)
                navigator.navigateToIpSetup()
            }
        }
    }

    fun schedulers() = CompletableTransformer { c ->
        c.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> schedulers() = SingleTransformer<T, T> { s ->
        s.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> schedulersFlowable() = FlowableTransformer<T, T> { f ->
        f.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
