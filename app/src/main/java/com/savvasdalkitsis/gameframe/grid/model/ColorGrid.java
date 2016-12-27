package com.savvasdalkitsis.gameframe.grid.model;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.composition.usecase.BlendUseCase;
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector;

public class ColorGrid implements Grid {

    public static final int SIDE = 16;
    private final int[][] colors = new int[SIDE][SIDE];
    private final BlendUseCase blendUseCase = UseCaseInjector.blendUseCase();
    private int transientTranslateCol;
    private int transientTranslateRow;
    private int translateCol;
    private int translateRow;
    private ColorGrid scratch;

    public ColorGrid() {
        fill(Color.TRANSPARENT);
    }

    @Override
    public void setColor(@ColorInt int color, int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        int c = column - translateCol();
        int r = row - translateRow();
        if (!isOutOfBounds(c, r)) {
            colors[c - 1][r - 1] = color;
        }
    }

    @Override
    public void fill(int color) {
        for (int i = 1; i <= SIDE; i++) {
            for (int j = 1; j <= SIDE; j++) {
                setColor(color, i, j);
            }
        }
    }

    @Override
    @ColorInt
    public int getColor(int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        int c = column - translateCol();
        int r = row - translateRow();
        if (isOutOfBounds(c, r)) {
            return Color.TRANSPARENT;
        }
        int color = colors[c - 1][r - 1];
        if (scratch == null) {
            return color;
        } else {
            return blendUseCase.mix(scratch.getColor(column, row), color, AvailableBlendMode.NORMAL, AvailablePorterDuffOperator.SOURCE_OVER, 1).color();
        }
    }

    private void checkValue(int value, final String valueName) {
        if (value < 1 || value > SIDE) {
            throw new IllegalArgumentException(valueName + " value should be between 1 and 16 but was " + value);
        }
    }

    public static boolean isOutOfBounds(int column, int row) {
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

    public Grid initializeScratch() {
        scratch = new ColorGrid();
        return scratch;
    }

    public void rasterScratch() {
        if (scratch != null) {
            ColorGrid scratch = this.scratch;
            this.scratch = null;
            Grid grid = blendUseCase.compose(this, scratch, AvailableBlendMode.NORMAL, AvailablePorterDuffOperator.SOURCE_OVER, 1);
            copyFrom(grid);
        }
    }

    private void copyFrom(Grid grid) {
        for (int i = 1; i <= SIDE; i++) {
            for (int j = 1; j <= SIDE; j++) {
                setColor(grid.getColor(i, j), i, j);
            }
        }
    }

}
