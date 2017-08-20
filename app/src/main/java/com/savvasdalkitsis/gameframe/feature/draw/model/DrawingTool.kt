package com.savvasdalkitsis.gameframe.feature.draw.model

interface DrawingTool {

    fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int)

    fun finishStroke(selectedLayer: Layer)
}
