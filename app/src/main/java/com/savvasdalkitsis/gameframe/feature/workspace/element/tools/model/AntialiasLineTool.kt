package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.composition.model.ARGB
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid

import java.lang.Math.abs
import java.lang.Math.floor

internal class AntialiasLineTool : ScratchTool() {

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        val argb = ARGB(color)
        var x0 = startColumn
        var y0 = startRow
        var x1 = column
        var y1 = row

        val steep = abs(y1 - y0) > abs(x1 - x0)

        if (steep) {
            var t = x0
            x0 = y0
            y0 = t
            t = x1
            x1 = y1
            y1 = t
        }
        if (x0 > x1) {
            var t = x0
            x0 = x1
            x1 = t
            t = y0
            y0 = y1
            y1 = t
        }

        val dx = x1 - x0
        val dy = y1 - y0
        var gradient = dy / dx.toFloat()
        if (dx.toDouble() == 0.0) {
            gradient = 1f
        }

        var xend = round(x0.toFloat())
        var yend = y0 + gradient * (xend - x0)
        var xgap = rfpart(x0 + 0.5f)
        val xpxl1 = xend
        val ypxl1 = ipart(yend)
        if (steep) {
            plot(scratch, ypxl1, xpxl1, rfpart(yend) * xgap, argb)
            plot(scratch, ypxl1 + 1, xpxl1, fpart(yend) * xgap, argb)
        } else {
            plot(scratch, xpxl1, ypxl1, rfpart(yend) * xgap, argb)
            plot(scratch, xpxl1, ypxl1 + 1, fpart(yend) * xgap, argb)
        }
        var intery = yend + gradient

        xend = round(x1.toFloat())
        yend = y1 + gradient * (xend - x1)
        xgap = fpart(x1 + 0.5f)
        val xpxl2 = xend
        val ypxl2 = ipart(yend)
        if (steep) {
            plot(scratch, ypxl2, xpxl2, rfpart(yend) * xgap, argb)
            plot(scratch, ypxl2 + 1, xpxl2, fpart(yend) * xgap, argb)
        } else {
            plot(scratch, xpxl2, ypxl2, rfpart(yend) * xgap, argb)
            plot(scratch, xpxl2, ypxl2 + 1, fpart(yend) * xgap, argb)
        }

        if (steep) {
            for (x in xpxl1 + 1 until xpxl2) {
                plot(scratch, ipart(intery), x, rfpart(intery), argb)
                plot(scratch, ipart(intery) + 1, x, fpart(intery), argb)
                intery += gradient
            }
        } else {
            for (x in xpxl1 + 1 until xpxl2) {
                plot(scratch, x, ipart(intery), rfpart(intery), argb)
                plot(scratch, x, ipart(intery) + 1, fpart(intery), argb)
                intery += gradient
            }
        }
    }

    private fun plot(scratch: Grid, x: Int, y: Int, c: Float, color: ARGB) {
        if (!ColorGrid.isOutOfBounds(x, y)) {
            scratch.setColor(color.multiplyAlpha(Math.sqrt(c.toDouble()).toFloat()).color(), x, y)
        }
    }

    private fun ipart(x: Float) = x.toInt()

    private fun round(x: Float) = ipart(x + 0.5f)

    private fun fpart(x: Float) =
            if (x < 0) {
                1 - (x - floor(x.toDouble()).toFloat())
            } else {
                x - floor(x.toDouble()).toFloat()
            }

    private fun rfpart(x: Float) = 1 - fpart(x)
}
