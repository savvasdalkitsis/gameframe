package com.savvasdalkitsis.gameframe.draw.model;

public class ClearTool implements DrawingTool {

    @Override
    public void drawOn(Layer layer, int column, int row, int color) {
        layer.getColorGrid().fill(color);
    }
}
