package com.savvasdalkitsis.gameframe.feature.draw.model

class ClearTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        layer.colorGrid.fill(color)
    }
}
