package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

class LineTool extends ScratchTool {

    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        int x = startColumn;
        int y = startRow;

        int dx = Math.abs(column - x);
        int dy = Math.abs(row - y);

        int sx = x < column ? 1 : -1;
        int sy = y < row ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true) {
            scratch.setColor(color, x, y);

            if (x == column && y == row) {
                break;
            }

            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x = x + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y = y + sy;
            }
        }
    }
}
