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
package com.savvasdalkitsis.gameframe.feature.device.model

enum class Brightness(val queryParamName: String, private val level: Int) {

    LEVEL_0("b0", 0),
    LEVEL_1("b1", 1),
    LEVEL_2("b2", 2),
    LEVEL_3("b3", 3),
    LEVEL_4("b4", 4),
    LEVEL_5("b5", 5),
    LEVEL_6("b6", 6),
    LEVEL_7("b7", 7);

    companion object {

        fun from(level: Int): Brightness {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) LEVEL_0 else LEVEL_7
        }
    }
}
