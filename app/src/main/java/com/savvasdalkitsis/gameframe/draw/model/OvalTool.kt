package com.savvasdalkitsis.gameframe.draw.model

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.grid.model.Grid

/**
 * Modified from http://stackoverflow.com/questions/15474122/is-there-a-midpoint-ellipse-algorithm
 */
open class OvalTool : ScratchTool() {

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        val rx = Math.abs(startColumn - column) / 2f
        val ry = Math.abs(startRow - row) / 2f
        val cx = Math.min(startColumn, column) + rx
        val cy = Math.min(startRow, row) + ry

        val a2 = rx * rx
        val b2 = ry * ry
        val twoA2 = 2 * a2
        val twoB2 = 2 * b2
        var p: Float
        var x = 0f
        var y = ry
        var px = 0f
        var py = twoA2 * y

        plot(cx, cy, x, y, scratch, color)

        p = Math.round(b2 - a2 * ry + 0.25 * a2).toFloat()
        while (px < py) {
            x++
            px += twoB2
            if (p < 0)
                p += b2 + px
            else {
                y--
                py -= twoA2
                p += b2 + px - py
            }
            plot(cx, cy, x, y, scratch, color)
        }

        p = Math.round(b2.toDouble() * (x + 0.5) * (x + 0.5) + a2 * (y - 1) * (y - 1) - a2 * b2).toFloat()
        while (y > 0) {
            y--
            py -= twoA2
            if (p > 0)
                p += a2 - py
            else {
                x++
                px += twoB2
                p += a2 - py + px
            }
            plot(cx, cy, x, y, scratch, color)
        }
    }

    private fun plot(xc: Float, yc: Float, x: Float, y: Float, scratch: Grid, color: Int) {
        draw(xc + x, yc + y, scratch, color)
        draw(xc - x, yc + y, scratch, color)
        draw(xc + x, yc - y, scratch, color)
        draw(xc - x, yc - y, scratch, color)
    }

    private fun draw(x: Float, y: Float, scratch: Grid, color: Int) {
        val c = x.toInt()
        val r = y.toInt()
        if (!ColorGrid.isOutOfBounds(c, r)) {
            scratch.setColor(color, c, r)
        }
    }
}
