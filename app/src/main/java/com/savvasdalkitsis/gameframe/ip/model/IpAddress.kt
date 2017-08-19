package com.savvasdalkitsis.gameframe.ip.model

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