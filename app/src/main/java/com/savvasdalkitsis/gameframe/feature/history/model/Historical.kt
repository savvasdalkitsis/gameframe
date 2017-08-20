package com.savvasdalkitsis.gameframe.feature.history.model

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import java.util.*


class Historical<T : Moment<T>>(present: T) {

    var present: T = present
        private set

    private val past: Deque<T> = LinkedList()
    private val future: Deque<T> = LinkedList()
    private val processor = BehaviorProcessor.create<T>()

    init {
        announcePresent()
    }

    fun observe(): Flowable<T> = processor

    fun progressTimeWithoutAnnouncing() {
        past.add(present.replicateMoment())
        future.clear()
    }

    fun progressTime() {
        progressTimeWithoutAnnouncing()
        announcePresent()
    }

    fun stepBackInTime() {
        if (hasPast()) {
            future.add(present)
            present = past.removeLast()
            announcePresent()
        }
    }

    fun stepForwardInTime() {
        if (hasFuture()) {
            past.add(present)
            present = future.removeLast()
            announcePresent()
        }
    }

    fun announcePresent() {
        processor.onNext(present)
    }

    fun hasPast() = !past.isEmpty()

    fun hasFuture() = !future.isEmpty()

    fun collapsePresentWithPastIfTheSame() {
        if (hasPast()) {
            if (present.isIdenticalTo(past.peekLast())) {
                forgetPresent()
            }
        }
    }

    private fun forgetPresent() {
        if (hasPast()) {
            present = past.removeLast()
            announcePresent()
        }
    }
}
