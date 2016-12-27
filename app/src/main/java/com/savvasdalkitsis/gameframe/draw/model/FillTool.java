package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

public class FillTool implements DrawingTool {

    @Override
    public void drawOn(Layer layer, int column, int row, int color) {
        fill(layer.getColorGrid(), column, row, color, layer.getColorGrid().getColor(column, row));
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
