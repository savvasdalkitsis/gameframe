package com.savvasdalkitsis.gameframe.model;

public enum ClockFace {

    COLOR("f1", 0),
    MUTED("f2", 1),
    SHADOW("f3", 2),
    BINARY_1("f4", 3),
    BINARY_2("f5", 4);

    private String queryParamName;
    private int level;

    ClockFace(String queryParamName, int level) {
        this.queryParamName = queryParamName;
        this.level = level;
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public static ClockFace from(int level) {
        for (ClockFace brightness : values()) {
            if (brightness.level == level) {
                return brightness;
            }
        }
        return level < 0 ? COLOR : BINARY_2;
    }
}
