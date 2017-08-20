package com.savvasdalkitsis.gameframe.feature.history.model

interface Moment<T> {

    fun replicateMoment(): T

    fun isIdenticalTo(moment: T): Boolean
}
