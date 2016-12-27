package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

public class FillOvalTool extends OvalTool {

    private final FillTool fillTool = new FillTool();

    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        super.drawOnScratch(scratch, startColumn, startRow, column, row, color);

        int rx = Math.abs(startColumn - column) / 2;
        int ry = Math.abs(startRow - row) / 2;
        int cx = Math.min(startColumn, column) + rx;
        int cy = Math.min(startRow, row) + ry;
        fillTool.fill(scratch, cx, cy, color, scratch.getColor(column, row));
    }
}
