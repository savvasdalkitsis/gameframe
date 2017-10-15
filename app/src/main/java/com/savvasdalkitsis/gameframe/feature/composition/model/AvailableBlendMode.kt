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

import java.lang.Math.*

/**
 * Implementing blending modes described at https://www.w3.org/TR/compositing-1/
 */
enum class AvailableBlendMode(private val blendComponent: (Float, Float) -> Float) : BlendMode {

    NORMAL({ source, _ -> source }),

    MULTIPLY({ source, dest -> source * dest }),

    SCREEN({ source, dest -> dest + source - dest * source }),

    HARD_LIGHT({ source, dest ->
        if (source <= 0.5) {
            MULTIPLY.blendComponent(2 * source, dest)
        } else {
            SCREEN.blendComponent(2 * source - 1, dest)
        }
    }),

    OVERLAY({ source, dest -> HARD_LIGHT.blendComponent(dest, source) }),

    DARKEN({ source, dest -> Math.min(source, dest) }),

    LIGHTEN({ source, dest -> Math.max(source, dest) }),

    COLOR_DOGE({ source, dest ->
        when {
            dest == 0f -> 0f
            source == 1f -> 1f
            else -> min(1f, dest / (1 - source))
        }
    }),

    COLOR_BURN({ source, dest ->
        when {
            dest == 1f -> 1f
            source == 0f -> 0f
            else -> 1 - min(1f, (1 - dest) / source)
        }
    }),

    SOFT_LIGHT({ source, dest ->
        if (source <= 0.5) {
            dest - (1 - 2 * source) * dest * (1 - dest)
        } else {
            dest + (2 * source - 1) * (d(dest) - dest)
        }
    }),

    DIFFERENCE({ source, dest -> abs(dest - source) }),

    EXCLUSION({ source, dest -> dest + source - 2f * dest * source });

    override fun blend(source: ARGB, dest: ARGB) = ARGB(
            blendWith(blendComponent, source.a, dest.a),
            blendWith(blendComponent, source.r, dest.r),
            blendWith(blendComponent, source.g, dest.g),
            blendWith(blendComponent, source.b, dest.b)
    )

    companion object {

        fun defaultMode() = NORMAL

        fun from(blendMode: BlendMode) =
                values().firstOrNull { it == blendMode } ?: defaultMode()

        fun fromName(name: String) =
                values().firstOrNull { it.name == name } ?: defaultMode()

        fun indexOf(blendMode: BlendMode): Int = values()
                .mapIndexed { i, mode -> Pair(i, mode) }
                .firstOrNull { (_, mode) -> mode == blendMode }?.first
                ?: indexOf(defaultMode())

        private fun blendWith(blendComponent: (Float, Float) -> Float, source: Int, dest: Int) =
                toRGB(blendComponent(toFloat(source), toFloat(dest)))

        private fun toRGB(inRange: Float) = (inRange * 255).toInt()

        private fun d(value: Float) = if (value <= 0.25) {
            ((16 * value - 12) * value + 4) * value
        } else {
            sqrt(value.toDouble()).toFloat()
        }

        private fun toFloat(inBytes: Int) = inBytes / 255f
    }
}
