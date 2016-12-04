package com.savvasdalkitsis.gameframe.model;

public enum CycleInterval {

    SECONDS_10("c1", 0),
    SECONDS_30("c2", 1),
    MINUTE_1("c3", 2),
    MINUTES_5("c4", 3),
    MINUTES_15("c5", 4),
    MINUTES_30("c6", 5),
    HOUR_1("c7", 6),
    INFINITE("c8", 7);

    private String queryParamName;
    private int level;

    CycleInterval(String queryParamName, int level) {
        this.queryParamName = queryParamName;
        this.level = level;
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public static CycleInterval from(int level) {
        for (CycleInterval brightness : values()) {
            if (brightness.level == level) {
                return brightness;
            }
        }
        return level < 0 ? SECONDS_10 : INFINITE;
    }
}
