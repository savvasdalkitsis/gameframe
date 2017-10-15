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
package com.savvasdalkitsis.gameframe.feature.control.model

enum class CycleInterval(val queryParamName: String, private val level: Int) {

    SECONDS_10("c1", 0),
    SECONDS_30("c2", 1),
    MINUTE_1("c3", 2),
    MINUTES_5("c4", 3),
    MINUTES_15("c5", 4),
    MINUTES_30("c6", 5),
    HOUR_1("c7", 6),
    INFINITE("c8", 7);

    companion object {

        fun from(level: Int): CycleInterval {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) SECONDS_10 else INFINITE
        }
    }
}
