package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

class MoveTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        if (!layer.isBackground) {
            layer.colorGrid.translate(column - startColumn, row - startRow)
        }
    }

    override fun finishStroke(selectedLayer: Layer) {
        selectedLayer.colorGrid.freezeTranslation()
    }
}
