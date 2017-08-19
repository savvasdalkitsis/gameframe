package com.savvasdalkitsis.gameframe.draw.model

import com.savvasdalkitsis.gameframe.grid.model.Grid

class LineTool : ScratchTool() {

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        var x = startColumn
        var y = startRow

        val dx = Math.abs(column - x)
        val dy = Math.abs(row - y)

        val sx = if (x < column) 1 else -1
        val sy = if (y < row) 1 else -1

        var err = dx - dy
        var e2: Int

        while (true) {
            scratch.setColor(color, x, y)

            if (x == column && y == row) {
                break
            }

            e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x += sx
            }

            if (e2 < dx) {
                err += dx
                y += sy
            }
        }
    }
}
