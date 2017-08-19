package com.savvasdalkitsis.gameframe.model

enum class Brightness(val queryParamName: String, private val level: Int) {

    LEVEL_0("b0", 0),
    LEVEL_1("b1", 1),
    LEVEL_2("b2", 2),
    LEVEL_3("b3", 3),
    LEVEL_4("b4", 4),
    LEVEL_5("b5", 5),
    LEVEL_6("b6", 6),
    LEVEL_7("b7", 7);


    companion object {

        fun from(level: Int): Brightness {
            return values().firstOrNull { it.level == level }
                    ?: if (level < 0) LEVEL_0 else LEVEL_7
        }
    }
}
