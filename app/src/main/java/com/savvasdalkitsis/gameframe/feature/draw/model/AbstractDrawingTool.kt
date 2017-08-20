package com.savvasdalkitsis.gameframe.feature.draw.model

open class AbstractDrawingTool : DrawingTool {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {}

    override fun finishStroke(selectedLayer: Layer) {}
}
