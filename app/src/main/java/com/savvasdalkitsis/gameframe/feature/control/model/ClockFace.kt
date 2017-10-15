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

enum class ClockFace(val queryParamName: String, private val level: Int) {

    COLOR("f1", 0),
    MUTED("f2", 1),
    SHADOW("f3", 2),
    BINARY_1("f4", 3),
    BINARY_2("f5", 4);

    companion object {

        fun from(level: Int): ClockFace {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) COLOR else BINARY_2
        }
    }
}
