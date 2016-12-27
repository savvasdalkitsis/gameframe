package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

public class FillRectangleTool extends ScratchTool {

    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        for (int c = Math.min(startColumn, column); c <= Math.max(startColumn, column); c++) {
            for (int r = Math.min(startRow, row); r <= Math.max(startRow, row); r++) {
                scratch.setColor(color, c, r);
            }
        }
    }
}
