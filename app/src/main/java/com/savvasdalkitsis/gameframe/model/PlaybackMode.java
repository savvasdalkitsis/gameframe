package com.savvasdalkitsis.gameframe.model;

public enum PlaybackMode {

    SEQUENTIAL("p0", 0),
    SHUFFLE("p1", 1),
    SHUFFLE_NO_ANIMATION("p2", 2);

    private String queryParamName;
    private int level;

    PlaybackMode(String queryParamName, int level) {
        this.queryParamName = queryParamName;
        this.level = level;
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public static PlaybackMode from(int level) {
        for (PlaybackMode brightness : values()) {
            if (brightness.level == level) {
                return brightness;
            }
        }
        return level < 0 ? SEQUENTIAL : SHUFFLE_NO_ANIMATION;
    }
}
