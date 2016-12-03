package com.savvasdalkitsis.gameframe.view.grid;

import android.support.annotation.ColorInt;

public class ColorGrid {

    private final int[][] colors = new int[16][16];

    public void setColor(@ColorInt int color, int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        colors[column - 1][row -1] = color;
    }

    @ColorInt
    public int getColor(int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        return colors[column - 1][row - 1];
    }

    private void checkValue(int value, final String valueName) {
        if (value < 1 || value > 16) {
            throw new IllegalArgumentException(valueName + " value should be between 1 and 16");
        }
    }
}
