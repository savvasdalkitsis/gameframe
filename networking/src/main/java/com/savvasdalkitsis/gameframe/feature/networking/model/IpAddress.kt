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
package com.savvasdalkitsis.gameframe.feature.networking.model

data class IpAddress(
        val part1: String? = null,
        val part2: String? = null,
        val part3: String? = null,
        val part4: String? = null) {

    override fun toString() = "$part1.$part2.$part3.$part4"

    fun isValid() = listOf(part1, part2, part3, part4).all { isPartValid(it) }

    private fun isPartValid(part: String?) =
            try {
                part?.toInt() in 0..255
            } catch (e: Exception) {
                false
            }

    companion object {

        fun parse(string: String): IpAddress {
            val parts = string.split(".")
            return IpAddress(
                    part1 = parts[0],
                    part2 = parts[1],
                    part3 = parts[2],
                    part4 = parts[3])
        }
    }
}