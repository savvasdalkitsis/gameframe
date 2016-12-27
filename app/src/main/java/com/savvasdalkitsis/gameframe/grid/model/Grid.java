package com.savvasdalkitsis.gameframe.grid.model;

import android.support.annotation.ColorInt;

public interface Grid {

    void setColor(@ColorInt int color, int column, int row);

    void fill(int color);

    @ColorInt
    int getColor(int column, int row);
}
