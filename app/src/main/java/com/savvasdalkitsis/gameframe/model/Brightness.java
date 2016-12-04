package com.savvasdalkitsis.gameframe.model;

public enum Brightness {

    LEVEL_0("b0", 0),
    LEVEL_1("b1", 1),
    LEVEL_2("b2", 2),
    LEVEL_3("b3", 3),
    LEVEL_4("b4", 4),
    LEVEL_5("b5", 5),
    LEVEL_6("b6", 6),
    LEVEL_7("b7", 7);

    private String queryParamName;
    private int level;

    Brightness(String queryParamName, int level) {
        this.queryParamName = queryParamName;
        this.level = level;
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public static Brightness from(int level) {
        for (Brightness brightness : values()) {
            if (brightness.level == level) {
                return brightness;
            }
        }
        return level < 0 ? LEVEL_0 : LEVEL_7;
    }
}
