package com.savvasdalkitsis.gameframe.draw.model;

import android.graphics.Color;

public class EraseTool implements DrawingTool {

    @Override
    public void drawOn(Layer layer, int column, int row, int color) {
        int erase = layer.isBackground() ? Color.GRAY : Color.TRANSPARENT;
        layer.getColorGrid().setColor(erase, column, row);
    }
}
