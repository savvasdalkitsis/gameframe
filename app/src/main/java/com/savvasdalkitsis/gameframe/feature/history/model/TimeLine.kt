package com.savvasdalkitsis.gameframe.feature.history.model

import io.reactivex.Single

interface TimeLine<T> {

    fun addMoment(moment: T)
    fun clearTimeline()
    fun removeLatestMoment(): Single<T>
    fun hasMoments(): Single<Boolean>
    fun latestMoment(): Single<T>
}