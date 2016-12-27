package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

abstract class ScratchTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        Grid scratch = layer.getColorGrid().initializeScratch();
        drawOnScratch(scratch, startColumn, startRow, column, row, color);
    }

    abstract void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color);

    @Override
    public void finishStroke(Layer layer) {
        layer.getColorGrid().rasterScratch();
    }
}
