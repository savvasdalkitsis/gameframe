package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

public class FillTool implements DrawingTool {

    @Override
    public void drawOn(ColorGrid colorGrid, int column, int row, int color) {
        fill(colorGrid, column, row, color, colorGrid.getColor(column, row));
    }

    private void fill(ColorGrid colorGrid, int column, int row, int newColor, int target) {
        if (colorGrid.isOutOfBounds(column, row)
                || target == newColor
                || colorGrid.getColor(column, row) != target) {
            return;
        }
        colorGrid.setColor(newColor, column, row);
        fill(colorGrid, column, row + 1, newColor, target);
        fill(colorGrid, column, row - 1, newColor, target);
        fill(colorGrid, column + 1, row, newColor, target);
        fill(colorGrid, column - 1, row, newColor, target);
    }
}
