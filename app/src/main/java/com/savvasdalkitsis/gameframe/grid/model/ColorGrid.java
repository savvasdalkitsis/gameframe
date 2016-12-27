package com.savvasdalkitsis.gameframe.grid.model;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class ColorGrid {

    public static final int SIDE = 16;
    private final int[][] colors = new int[SIDE][SIDE];
    private int transientTranslateCol;
    private int transientTranslateRow;
    private int translateCol;
    private int translateRow;

    public ColorGrid() {
        fill(Color.TRANSPARENT);
    }

    public void setColor(@ColorInt int color, int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        int c = column - translateCol();
        int r = row - translateRow();
        if (!isOutOfBounds(c, r)) {
            colors[c - 1][r - 1] = color;
        }
    }

    public void fill(int color) {
        for (int i = 1; i <= SIDE; i++) {
            for (int j = 1; j <= SIDE; j++) {
                setColor(color, i, j);
            }
        }
    }

    @ColorInt
    public int getColor(int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        int c = column - translateCol();
        int r = row - translateRow();
        if (isOutOfBounds(c, r)) {
            return Color.TRANSPARENT;
        }
        return colors[c - 1][r - 1];
    }

    private void checkValue(int value, final String valueName) {
        if (value < 1 || value > SIDE) {
            throw new IllegalArgumentException(valueName + " value should be between 1 and 16");
        }
    }

    public boolean isOutOfBounds(int column, int row) {
        return row < 1 || column < 1 || row > SIDE || column > SIDE;
    }

    public void translate(int translateCol, int translateRow) {
        this.transientTranslateCol = translateCol;
        this.transientTranslateRow = translateRow;
    }

    public void freezeTranslation() {
        translateCol = translateCol();
        translateRow = translateRow();
        transientTranslateCol = 0;
        transientTranslateRow = 0;
    }

    private int translateRow() {
        return translateRow + transientTranslateRow;
    }

    private int translateCol() {
        return translateCol + transientTranslateCol;
    }
}
