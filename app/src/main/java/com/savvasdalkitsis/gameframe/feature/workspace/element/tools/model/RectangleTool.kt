package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid

class RectangleTool : ScratchTool() {

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        for (c in Math.min(startColumn, column)..Math.max(startColumn, column)) {
            scratch.setColor(color, c, startRow)
            scratch.setColor(color, c, row)
        }
        for (r in Math.min(startRow, row)..Math.max(startRow, row)) {
            scratch.setColor(color, startColumn, r)
            scratch.setColor(color, column, r)
        }
    }
}
