package com.savvasdalkitsis.gameframe.draw.model;

class AbstractDrawingTool implements DrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
    }

    @Override
    public void finishStroke(Layer selectedLayer) {
    }
}
