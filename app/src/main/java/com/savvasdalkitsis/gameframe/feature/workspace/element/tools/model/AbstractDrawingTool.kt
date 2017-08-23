package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

open class AbstractDrawingTool : DrawingTool {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {}

    override fun finishStroke(selectedLayer: Layer) {}
}