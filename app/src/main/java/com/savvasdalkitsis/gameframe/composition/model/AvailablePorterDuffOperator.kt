package com.savvasdalkitsis.gameframe.composition.model

/**
 * Implementing Porter Duff operators described at https://www.w3.org/TR/compositing-1/
 */
enum class AvailablePorterDuffOperator(private val key: String, private val fa: (Float) -> Float, private val fb: (Float) -> Float) : PorterDuffOperator {

    CLEAR("clear", { 0f }, { 0f }),
    COPY("copy", { 1f }, { 0f }),
    DESTINATION("destination", { 0f }, { 1f }),
    SOURCE_OVER("source_over", { 1f }, { a -> 1 - a }),
    DESTINATION_OVER("destination_over", { a -> 1 - a }, { 1f }),
    SOURCE_IN("source_in", { a -> a }, { 0f }),
    DESTINATION_IN("destination_in", { 0f }, { a -> a }),
    SOURCE_OUT("source_out", { a -> 1 - a }, { 0f }),
    DESTINATION_OUT("destination_out", { 0f }, { a -> 1 - a }),
    SOURCE_ATOP("source_atop", { a -> a }, { a -> 1 - a }),
    DESTINATION_ATOP("destination_atop", { a -> 1 - a }, { a -> a }),
    XOR("xor", { a -> 1 - a }, { a -> 1 - a }),
    LIGHTER("lighter", { 1f }, { 1f });

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
