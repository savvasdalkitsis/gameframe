package com.savvasdalkitsis.gameframe.infra.kotlin

fun <T> T?.or(ifNullValue: T) = this ?: ifNullValue