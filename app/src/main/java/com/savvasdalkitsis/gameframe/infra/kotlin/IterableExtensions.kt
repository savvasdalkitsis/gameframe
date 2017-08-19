package com.savvasdalkitsis.gameframe.infra.kotlin

fun <T> Iterable<T>.findIndexOrThrow(predicate: (T) -> Boolean, ifNotFound:() -> Throwable): Int {
    val i = this.indexOfFirst(predicate)
    if (i < 0) {
        throw ifNotFound()
    }
    return i
}