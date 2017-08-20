package com.savvasdalkitsis.gameframe.control.model

enum class PlaybackMode(val queryParamName: String, private val level: Int) {

    SEQUENTIAL("p0", 0),
    SHUFFLE("p1", 1),
    SHUFFLE_NO_ANIMATION("p2", 2);


    companion object {

        fun from(level: Int): PlaybackMode {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) SEQUENTIAL else SHUFFLE_NO_ANIMATION
        }
    }
}
