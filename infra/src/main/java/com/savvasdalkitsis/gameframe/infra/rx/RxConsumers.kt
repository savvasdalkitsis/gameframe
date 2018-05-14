package com.savvasdalkitsis.gameframe.infra.rx

import android.util.Log

fun logErrors(): (Throwable) -> Unit = {
    Log.w("RxConsumers", "Error", it)
}