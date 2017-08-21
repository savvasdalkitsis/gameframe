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
