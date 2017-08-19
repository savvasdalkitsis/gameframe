package com.savvasdalkitsis.gameframe.infra.kotlin

import io.reactivex.Maybe

fun <T> T?.toMaybe(): Maybe<T> = if (this != null) Maybe.just(this) else Maybe.empty()