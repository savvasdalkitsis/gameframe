package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

abstract class ScratchTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        val scratch = layer.colorGrid.initializeScratch()
        drawOnScratch(scratch, startColumn, startRow, column, row, color)
    }

    internal abstract fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int)

    override fun finishStroke(selectedLayer: Layer) {
        selectedLayer.colorGrid.rasterScratch()
    }
}
