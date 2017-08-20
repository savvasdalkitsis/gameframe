package com.savvasdalkitsis.gameframe.feature.draw.model

import com.savvasdalkitsis.gameframe.feature.grid.model.Grid

class FillRectangleTool : ScratchTool() {

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        for (c in Math.min(startColumn, column)..Math.max(startColumn, column)) {
            for (r in Math.min(startRow, row)..Math.max(startRow, row)) {
                scratch.setColor(color, c, r)
            }
        }
    }
}
