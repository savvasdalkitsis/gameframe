package com.savvasdalkitsis.gameframe.feature.draw.model

import com.savvasdalkitsis.gameframe.feature.grid.model.Grid

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
