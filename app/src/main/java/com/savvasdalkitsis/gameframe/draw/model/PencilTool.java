package com.savvasdalkitsis.gameframe.draw.model;

class PencilTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        layer.getColorGrid().setColor(color, column, row);
    }
}
