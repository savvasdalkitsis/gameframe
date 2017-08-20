package com.savvasdalkitsis.gameframe.control.model

enum class DisplayMode(val queryParamName: String, private val level: Int) {

    GALLERY("m0", 0),
    CLOCK("m1", 1);


    companion object {

        fun from(level: Int): DisplayMode {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) GALLERY else CLOCK
        }
    }
}
