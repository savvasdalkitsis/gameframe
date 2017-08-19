package com.savvasdalkitsis.gameframe.model

interface Moment<T> {

    fun replicateMoment(): T

    fun isIdenticalTo(moment: T): Boolean
}
