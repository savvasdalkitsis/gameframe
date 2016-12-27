package com.savvasdalkitsis.gameframe.draw.model;

public class MoveTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        if (layer.isBackground()) {
            return;
        }
        layer.getColorGrid().translate(column - startColumn, row - startRow);
    }

    @Override
    public void finishStroke(Layer layer) {
        layer.getColorGrid().freezeTranslation();
    }
}
