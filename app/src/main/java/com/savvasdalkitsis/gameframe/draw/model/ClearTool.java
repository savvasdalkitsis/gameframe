package com.savvasdalkitsis.gameframe.draw.model;

public class ClearTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        layer.getColorGrid().fill(color);
    }
}
