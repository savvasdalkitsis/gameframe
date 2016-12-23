package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

public interface DrawingTool {

    void drawOn(ColorGrid colorGrid, int column, int row, int color);
}
