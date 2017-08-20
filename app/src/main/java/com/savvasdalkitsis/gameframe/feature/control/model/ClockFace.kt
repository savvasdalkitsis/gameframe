package com.savvasdalkitsis.gameframe.feature.control.model

enum class ClockFace(val queryParamName: String, private val level: Int) {

    COLOR("f1", 0),
    MUTED("f2", 1),
    SHADOW("f3", 2),
    BINARY_1("f4", 3),
    BINARY_2("f5", 4);


    companion object {

        fun from(level: Int): ClockFace {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) COLOR else BINARY_2
        }
    }
}
