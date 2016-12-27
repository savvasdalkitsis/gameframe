package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

public class RectangleTool extends ScratchTool {

    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        for (int c = Math.min(startColumn, column); c <= Math.max(startColumn, column); c++) {
            scratch.setColor(color, c, startRow);
            scratch.setColor(color, c, row);
        }
        for (int r = Math.min(startRow, row); r <= Math.max(startRow, row); r++) {
            scratch.setColor(color, startColumn, r);
            scratch.setColor(color, column, r);
        }
    }
}
