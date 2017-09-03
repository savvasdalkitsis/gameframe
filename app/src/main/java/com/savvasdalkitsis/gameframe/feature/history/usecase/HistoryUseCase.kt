package com.savvasdalkitsis.gameframe.feature.history.usecase

import com.savvasdalkitsis.gameframe.feature.history.model.InMemoryUnboundedTimeLine
import com.savvasdalkitsis.gameframe.feature.history.model.Moment
import com.savvasdalkitsis.gameframe.feature.history.model.TimeLine
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class HistoryUseCase<T : Moment<T>>(present: T,
                                    private val past: TimeLine<T> = InMemoryUnboundedTimeLine(),
                                    private val future: TimeLine<T> = InMemoryUnboundedTimeLine()) {

    var present: T = present
        private set

    private val processor = BehaviorProcessor.create<T>()

    init {
        announcePresent()
    }

    fun observe(): Flowable<T> = processor

    fun progressTimeWithoutAnnouncing() {
        past.addMoment(present)
        present = present.replicateMoment()
        future.clearTimeline()
    }

    fun progressTime() {
        progressTimeWithoutAnnouncing()
        announcePresent()
    }

    fun stepBackInTime() {
        hasPast()
                .filter(hasMoments())
                .doOnSuccess { future.addMoment(present) }
                .flatMapSingleElement { past.removeLatestMoment() }
                .doOnSuccess { present = it }
                .doOnSuccess { announcePresent() }
                .subscribe()
    }

    fun stepForwardInTime() {
        hasFuture()
                .filter(hasMoments())
                .doOnSuccess { past.addMoment(present) }
                .flatMapSingleElement { future.removeLatestMoment() }
                .doOnSuccess { present = it }
                .doOnSuccess { announcePresent() }
                .subscribe()
    }

    fun announcePresent() {
        processor.onNext(present)
    }

    fun hasPast() = past.hasMoments()

    fun hasFuture() = future.hasMoments()

    fun collapsePresentWithPastIfTheSame() {
        hasPast()
                .filter(hasMoments())
                .flatMapSingleElement { past.latestMoment() }
                .filter { present.isIdenticalTo(it) }
                .doOnSuccess { forgetPresent() }
    }

    fun restartFrom(moment: T) {
        past.clearTimeline()
        future.clearTimeline()
        present = moment
        announcePresent()
    }

    private fun forgetPresent() {
        hasPast()
                .filter(hasMoments())
                .flatMapSingleElement { past.removeLatestMoment() }
                .doOnSuccess { present = it }
                .doOnSuccess { announcePresent() }
    }

    private fun hasMoments() = { hasMoments: Boolean -> hasMoments }
}