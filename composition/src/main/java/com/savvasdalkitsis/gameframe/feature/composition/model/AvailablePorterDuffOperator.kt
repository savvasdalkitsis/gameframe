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
package com.savvasdalkitsis.gameframe.feature.composition.model

/**
 * Implementing Porter Duff operators described at https://www.w3.org/TR/compositing-1/
 */
enum class AvailablePorterDuffOperator(private val fa: (Float) -> Float, private val fb: (Float) -> Float) : PorterDuffOperator {

    CLEAR({ 0f }, { 0f }),
    COPY({ 1f }, { 0f }),
    DESTINATION({ 0f }, { 1f }),
    SOURCE_OVER({ 1f }, { a -> 1 - a }),
    DESTINATION_OVER({ a -> 1 - a }, { 1f }),
    SOURCE_IN({ a -> a }, { 0f }),
    DESTINATION_IN({ 0f }, { a -> a }),
    SOURCE_OUT({ a -> 1 - a }, { 0f }),
    DESTINATION_OUT({ 0f }, { a -> 1 - a }),
    SOURCE_ATOP({ a -> a }, { a -> 1 - a }),
    DESTINATION_ATOP({ a -> 1 - a }, { a -> a }),
    XOR({ a -> 1 - a }, { a -> 1 - a }),
    LIGHTER({ 1f }, { 1f });

    override fun fa(argb: ARGB): Float {
        return fa(toFloat(argb.a))
    }

    override fun fb(argb: ARGB): Float {
        return fb(toFloat(argb.a))
    }

    companion object {

        fun from(porterDuffOperator: PorterDuffOperator) =
                AvailablePorterDuffOperator.values().firstOrNull { it == porterDuffOperator } ?: defaultOperator()

        fun fromName(name: String) =
                AvailablePorterDuffOperator.values().firstOrNull { it.name == name } ?: defaultOperator()

        fun indexOf(porterDuffOperator: PorterDuffOperator): Int {
            var i = values().indexOfFirst { mode -> mode == porterDuffOperator }
            if (i < 0) {
                i = indexOf(defaultOperator())
            }
            return i
        }

        fun defaultOperator() = SOURCE_OVER

        private fun toFloat(inBytes: Int) = inBytes / 255f
    }
}
