package com.savvasdalkitsis.gameframe.draw.model;

public class PencilTool implements DrawingTool {

    @Override
    public void drawOn(Layer layer, int column, int row, int color) {
        layer.getColorGrid().setColor(color, column, row);
    }

}
