package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.composition.model.ARGB;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.model.Grid;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

class AntialiasLineTool extends ScratchTool {

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    void drawOnScratch(Grid scratch, int startColumn, int startRow, int column, int row, int color) {
        ARGB argb = new ARGB(color);
        int x0 = startColumn;
        int y0 = startRow;
        int x1 = column;
        int y1 = row;

        boolean steep = abs(y1 - y0) > abs(x1 - x0);

        if (steep) {
            int t = x0;
            x0 = y0;
            y0 = t;
            t = x1;
            x1 = y1;
            y1 = t;
        }
        if (x0 > x1) {
            int t = x0;
            x0 = x1;
            x1 = t;
            t = y0;
            y0 = y1;
            y1 = t;
        }

        int dx = x1 - x0;
        int dy = y1 - y0;
        float gradient = dy / (float)dx;
        if (dx == 0.0) {
            gradient = 1f;
        }

        int xend = round(x0);
        float yend = y0 + gradient * (xend - x0);
        float xgap = rfpart(x0 + 0.5f);
        int xpxl1 = xend;
        int ypxl1 = ipart(yend);
        if (steep) {
            plot(scratch, ypxl1, xpxl1, rfpart(yend) * xgap, argb);
            plot(scratch, ypxl1 + 1, xpxl1, fpart(yend) * xgap, argb);
        } else {
            plot(scratch, xpxl1, ypxl1, rfpart(yend) * xgap, argb);
            plot(scratch, xpxl1, ypxl1 + 1, fpart(yend) * xgap, argb);
        }
        float intery = yend + gradient;

        xend = round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5f);
        int xpxl2 = xend;
        int ypxl2 = ipart(yend);
        if (steep) {
            plot(scratch, ypxl2, xpxl2, rfpart(yend) * xgap, argb);
            plot(scratch, ypxl2 + 1, xpxl2, fpart(yend) * xgap, argb);
        } else {
            plot(scratch, xpxl2, ypxl2, rfpart(yend) * xgap, argb);
            plot(scratch, xpxl2, ypxl2 + 1, fpart(yend) * xgap, argb);
        }

        if (steep) {
            for (int x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
                plot(scratch, ipart(intery), x, rfpart(intery), argb);
                plot(scratch, ipart(intery) + 1, x, fpart(intery), argb);
                intery = intery + gradient;
            }
        } else {
            for (int x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
                plot(scratch, x, ipart(intery), rfpart(intery), argb);
                plot(scratch, x, ipart(intery) + 1, fpart(intery), argb);
                intery = intery + gradient;
            }
        }
    }

    private void plot(Grid scratch, int x, int y, float c, ARGB color) {
        if (!ColorGrid.isOutOfBounds(x, y)) {
            scratch.setColor(color.multiplyAlpha((float) Math.sqrt(c)).color(), x, y);
        }
    }

    private int ipart(float x) {
        return (int) x;
    }

    private int round(float x) {
        return ipart(x + 0.5f);
    }

    private float fpart(float x) {
        if (x< 0) {
            return 1 - (x - (float)floor(x));
        }
        return x - (float)floor(x);
    }

    private float rfpart(float x) {
        return 1 - fpart(x);
    }
}
