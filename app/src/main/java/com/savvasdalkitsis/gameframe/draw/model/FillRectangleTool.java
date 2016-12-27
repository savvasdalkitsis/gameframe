package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

public class FillRectangleTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        Grid scratch = layer.getColorGrid().initializeScratch();
        for (int c = Math.min(startColumn, column); c <= Math.max(startColumn, column); c++) {
            for (int r = Math.min(startRow, row); r <= Math.max(startRow, row); r++) {
                scratch.setColor(color, c, r);
            }
        }
    }

    @Override
    public void finishStroke(Layer layer) {
        layer.getColorGrid().rasterScratch();
    }
}
