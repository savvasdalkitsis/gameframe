package com.savvasdalkitsis.gameframe.feature.control.model

enum class CycleInterval(val queryParamName: String, private val level: Int) {

    SECONDS_10("c1", 0),
    SECONDS_30("c2", 1),
    MINUTE_1("c3", 2),
    MINUTES_5("c4", 3),
    MINUTES_15("c5", 4),
    MINUTES_30("c6", 5),
    HOUR_1("c7", 6),
    INFINITE("c8", 7);


    companion object {

        fun from(level: Int): CycleInterval {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) SECONDS_10 else INFINITE
        }
    }
}
