package com.savvasdalkitsis.gameframe.feature.draw.model

internal class PencilTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        layer.colorGrid.setColor(color, column, row)
    }
}
