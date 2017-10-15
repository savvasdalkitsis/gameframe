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