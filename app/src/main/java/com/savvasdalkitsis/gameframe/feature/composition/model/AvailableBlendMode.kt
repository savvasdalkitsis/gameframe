package com.savvasdalkitsis.gameframe.feature.composition.model

import java.lang.Math.*

/**
 * Implementing blending modes described at https://www.w3.org/TR/compositing-1/
 */
enum class AvailableBlendMode(private val key: String, private val blendComponent: (Float, Float) -> Float) : BlendMode {

    NORMAL("normal", { source, _ -> source }),

    MULTIPLY("multiply", { source, dest -> source * dest }),

    SCREEN("screen", { source, dest -> dest + source - dest * source }),

    HARD_LIGHT("hardLight", { source, dest ->
        if (source <= 0.5) {
            MULTIPLY.blendComponent(2 * source, dest)
        } else {
            SCREEN.blendComponent(2 * source - 1, dest)
        }
    }),

    OVERLAY("overlay", { source, dest -> HARD_LIGHT.blendComponent(dest, source) }),

    DARKEN("darken", { a, b -> Math.min(a, b) }),

    LIGHTEN("lighten", { a, b -> Math.max(a, b) }),

    COLOR_DOGE("colorDodge", { source, dest ->
        when {
            dest == 0f -> 0f
            source == 1f -> 1f
            else -> min(1f, dest / (1 - source))
        }
    }),

    COLOR_BURN("colorBurn", { source, dest ->
        when {
            dest == 1f -> 1f
            source == 0f -> 0f
            else -> 1 - min(1f, (1 - dest) / source)
        }
    }),

    SOFT_LIGHT("softLight", { source, dest ->
        if (source <= 0.5) {
            dest - (1 - 2 * source) * dest * (1 - dest)
        } else {
            dest + (2 * source - 1) * (d(dest) - dest)
        }
    }),

    DIFFERENCE("difference", { source, dest -> abs(dest - source) }),

    EXCLUSION("exclusion", { source, dest -> dest + source - 2f * dest * source });

    override fun blend(source: ARGB, dest: ARGB) = ARGB(
            blendWith(blendComponent, source.a, dest.a),
            blendWith(blendComponent, source.r, dest.r),
            blendWith(blendComponent, source.g, dest.g),
            blendWith(blendComponent, source.b, dest.b)
    )

    companion object {

        fun defaultMode() = NORMAL

        fun from(key: String) = values().firstOrNull { it.key == key }
                ?: defaultMode()

        fun indexOf(blendMode: BlendMode): Int = values()
                .mapIndexed { i, mode -> Pair(i, mode) }
                .firstOrNull { (_, mode) -> mode == blendMode }?.first
                ?: indexOf(defaultMode())

        private fun blendWith(blendComponent: (Float, Float) -> Float, source: Int, dest: Int) =
                toRGB(blendComponent(toFloat(source), toFloat(dest)))

        private fun toRGB(inRange: Float) = (inRange * 255).toInt()

        internal fun d(value: Float) = if (value <= 0.25) {
            ((16 * value - 12) * value + 4) * value
        } else {
            sqrt(value.toDouble()).toFloat()
        }

        private fun toFloat(inBytes: Int) = inBytes / 255f
    }
}
