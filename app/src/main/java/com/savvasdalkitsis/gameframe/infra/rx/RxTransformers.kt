/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.infra.rx

import android.util.Log
import com.savvasdalkitsis.gameframe.feature.ip.IpInjector
import com.savvasdalkitsis.gameframe.feature.ip.model.IpBaseHostMissingException
import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxTransformers {

    private val navigator = IpInjector.ipNavigator()

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
