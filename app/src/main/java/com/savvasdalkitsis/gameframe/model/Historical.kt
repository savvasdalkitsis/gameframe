package com.savvasdalkitsis.gameframe.model

import java.util.Deque
import java.util.LinkedList

import rx.Observable
import rx.subjects.BehaviorSubject

class Historical<T : Moment<T>>(present: T) {

    var present: T = present
        private set

    private val past: Deque<T> = LinkedList()
    private val future: Deque<T> = LinkedList()
    private val subject = BehaviorSubject.create<T>()

    init {
        announcePresent()
    }

    fun observe(): Observable<T> = subject

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
        subject.onNext(present)
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
