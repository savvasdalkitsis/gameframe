package com.savvasdalkitsis.gameframe.draw.model;

public interface DrawingTool {

    void drawOn(Layer layer, int startColumn, int startRow, int column, int row, int color);

    void finishStroke(Layer selectedLayer);
}
