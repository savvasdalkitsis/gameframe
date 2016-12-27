package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.model.Grid;

public class FillTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        fill(layer.getColorGrid(), column, row, color, layer.getColorGrid().getColor(column, row));
    }

    public void fill(Grid grid, int column, int row, int newColor, int target) {
        if (ColorGrid.isOutOfBounds(column, row)
                || target == newColor
                || grid.getColor(column, row) != target) {
            return;
        }
        grid.setColor(newColor, column, row);
        fill(grid, column, row + 1, newColor, target);
        fill(grid, column, row - 1, newColor, target);
        fill(grid, column + 1, row, newColor, target);
        fill(grid, column - 1, row, newColor, target);
    }
}
