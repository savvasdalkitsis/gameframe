package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

class FillTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        fill(layer.colorGrid, column, row, color, layer.colorGrid.getColor(column, row))
    }

    fun fill(grid: Grid, column: Int, row: Int, newColor: Int, target: Int) {
        if (ColorGrid.isOutOfBounds(column, row)
                || target == newColor
                || grid.getColor(column, row) != target) {
            return
        }
        grid.setColor(newColor, column, row)
        fill(grid, column, row + 1, newColor, target)
        fill(grid, column, row - 1, newColor, target)
        fill(grid, column + 1, row, newColor, target)
        fill(grid, column - 1, row, newColor, target)
    }
}
