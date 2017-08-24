package com.savvasdalkitsis.gameframe.feature.history.model

import io.reactivex.Single
import java.util.*

class InMemoryUnboundedTimeLine<T> : TimeLine<T> {

    private val moments: Deque<T> = LinkedList()

    override fun addMoment(moment: T) {
        moments.add(moment)
    }

    override fun clearTimeline() {
        moments.clear()
    }

    override fun removeLatestMoment(): Single<T> = Single.just(moments.removeLast())

    override fun latestMoment(): Single<T> = Single.just(moments.peekLast())

    override fun hasMoments(): Single<Boolean> = Single.just(moments.isNotEmpty())
}