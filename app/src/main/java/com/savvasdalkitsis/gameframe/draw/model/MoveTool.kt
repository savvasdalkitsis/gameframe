package com.savvasdalkitsis.gameframe.draw.model

class MoveTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        if (layer.isBackground) {
            return
        }
        layer.colorGrid.translate(column - startColumn, row - startRow)
    }

    override fun finishStroke(selectedLayer: Layer) {
        selectedLayer.colorGrid.freezeTranslation()
    }
}
