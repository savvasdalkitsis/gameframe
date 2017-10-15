package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

class MoveTool(private val messageDisplay: MessageDisplay) : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        if (!layer.isBackground) {
            layer.colorGrid.translate(column - startColumn, row - startRow)
        }
    }

    override fun finishStroke(selectedLayer: Layer) {
        selectedLayer.colorGrid.freezeTranslation()
        if (selectedLayer.isBackground) {
            messageDisplay.show(R.string.tool_not_for_background)
        }
    }
}
