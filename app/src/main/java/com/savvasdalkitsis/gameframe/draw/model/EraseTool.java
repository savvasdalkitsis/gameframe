package com.savvasdalkitsis.gameframe.draw.model;

import android.graphics.Color;

public class EraseTool extends AbstractDrawingTool {

    @Override
    public void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color) {
        int erase = layer.isBackground() ? Color.GRAY : Color.TRANSPARENT;
        layer.getColorGrid().setColor(erase, column, row);
    }
}
