package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid

class FillOvalTool : OvalTool() {

    private val fillTool = FillTool()

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        super.drawOnScratch(scratch, startColumn, startRow, column, row, color)

        val rx = Math.abs(startColumn - column) / 2
        val ry = Math.abs(startRow - row) / 2
        val cx = Math.min(startColumn, column) + rx
        val cy = Math.min(startRow, row) + ry
        fillTool.fill(scratch, cx, cy, color, scratch.getColor(column, row))
    }
}
