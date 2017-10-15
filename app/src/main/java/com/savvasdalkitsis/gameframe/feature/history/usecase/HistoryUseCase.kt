/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
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