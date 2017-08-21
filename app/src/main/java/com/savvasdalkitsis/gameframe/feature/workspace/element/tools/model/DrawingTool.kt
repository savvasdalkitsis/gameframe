package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

interface DrawingTool {

    fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int)

    fun finishStroke(selectedLayer: Layer)
}
