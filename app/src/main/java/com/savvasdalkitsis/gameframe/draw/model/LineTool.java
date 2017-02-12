package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.model.Grid;

class LineTool extends ScratchTool {

    private static final int STEP = ColorGrid.SIDE * 2;

    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        float stepX = (column - startColumn) / (float) STEP;
        float stepY = (row - startRow) / (float) STEP;
        for (int i = 0; i < STEP; i++) {
            float x = startColumn + i * stepX;
            float y = startRow + i * stepY;
            scratch.setColor(color, (int) x, (int) y);
        }
        scratch.setColor(color, column, row);
    }
}
