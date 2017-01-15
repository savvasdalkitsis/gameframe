package com.savvasdalkitsis.gameframe.grid.model;

import android.support.annotation.ColorInt;

import com.savvasdalkitsis.gameframe.draw.model.Moment;

public interface Grid extends Moment<Grid> {

    void setColor(@ColorInt int color, int column, int row);

    void fill(int color);

    @ColorInt
    int getColor(int column, int row);
}
