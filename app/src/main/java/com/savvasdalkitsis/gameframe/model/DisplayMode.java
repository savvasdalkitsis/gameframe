package com.savvasdalkitsis.gameframe.model;

public enum DisplayMode {

    GALLERY("m0", 0),
    CLOCK("m1", 1);

    private String queryParamName;
    private int level;

    DisplayMode(String queryParamName, int level) {
        this.queryParamName = queryParamName;
        this.level = level;
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public static DisplayMode from(int level) {
        for (DisplayMode brightness : values()) {
            if (brightness.level == level) {
                return brightness;
            }
        }
        return level < 0 ? GALLERY : CLOCK;
    }
}
