package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.model.Grid;

/**
 * Modified from http://stackoverflow.com/questions/15474122/is-there-a-midpoint-ellipse-algorithm
 */
public class OvalTool extends ScratchTool {

    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        float rx = Math.abs(startColumn - column) / 2f;
        float ry = Math.abs(startRow - row) / 2f;
        float cx = Math.min(startColumn, column) + rx;
        float cy = Math.min(startRow, row) + ry;

        float a2 = rx * rx;
        float b2 = ry * ry;
        float twoA2 = 2 * a2;
        float twoB2 = 2 * b2;
        float p;
        float x = 0;
        float y = ry;
        float px = 0;
        float py = twoA2 * y;

        plot(cx, cy, x, y, scratch, color);

        p = Math.round(b2 - (a2 * ry) + (0.25 * a2));
        while (px < py) {
            x++;
            px += twoB2;
            if (p < 0)
                p += b2 + px;
            else {
                y--;
                py -= twoA2;
                p += b2 + px - py;
            }
            plot(cx, cy, x, y, scratch, color);
        }

        p = Math.round(b2 * (x + 0.5) * (x + 0.5) + a2 * (y - 1) * (y - 1) - a2 * b2);
        while (y > 0) {
            y--;
            py -= twoA2;
            if (p > 0)
                p += a2 - py;
            else {
                x++;
                px += twoB2;
                p += a2 - py + px;
            }
            plot(cx, cy, x, y, scratch, color);
        }
    }

    private void plot(float xc, float yc, float x, float y, Grid scratch, int color) {
        draw(xc + x, yc + y, scratch, color);
        draw(xc - x, yc + y, scratch, color);
        draw(xc + x, yc - y, scratch, color);
        draw(xc - x, yc - y, scratch, color);
    }

    private void draw(float x, float y, Grid scratch, int color) {
        int c = (int) x;
        int r = (int) y;
        if (!ColorGrid.isOutOfBounds(c, r)) {
            scratch.setColor(color, c, r);
        }
    }
}
