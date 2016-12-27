package com.savvasdalkitsis.gameframe.grid.model;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.savvasdalkitsis.gameframe.composition.model.ARGB;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;
import com.savvasdalkitsis.gameframe.composition.usecase.BlendUseCase;
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector;

public class ColorGrid {

    public static final int SIDE = 16;
    private final int[][] colors = new int[SIDE][SIDE];
    private final BlendUseCase blendUseCase = UseCaseInjector.blendUseCase();

    public ColorGrid() {
        fill(Color.TRANSPARENT);
    }

    public void setColor(@ColorInt int color, int column, int row) {
        checkValue(column, "Column");
        checkValue(row, "Row");
        colors[column - 1][row -1] = color;
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
        return colors[column - 1][row - 1];
    }

    private void checkValue(int value, final String valueName) {
        if (value < 1 || value > SIDE) {
            throw new IllegalArgumentException(valueName + " value should be between 1 and 16");
        }
    }

    public boolean isOutOfBounds(int column, int row) {
        return row < 1 || column < 1 || row > SIDE || column > SIDE;
    }

    public ColorGrid compose(ColorGrid dest, ColorGrid source, BlendMode blendMode, PorterDuffOperator porterDuffOperator, float alpha) {
        ColorGrid colorGrid = new ColorGrid();
        for (int col = 1; col <= SIDE; col++) {
            for (int row = 1; row <= SIDE; row++) {
                ARGB blend = blendUseCase.blend(source.getColor(col, row), dest.getColor(col, row),
                        blendMode, porterDuffOperator, alpha);
                colorGrid.setColor(blend.color(), col, row);
            }
        }
        return colorGrid;
    }
}
