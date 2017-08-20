package com.savvasdalkitsis.gameframe.history.model

interface Moment<T> {

    fun replicateMoment(): T

    fun isIdenticalTo(moment: T): Boolean
}
