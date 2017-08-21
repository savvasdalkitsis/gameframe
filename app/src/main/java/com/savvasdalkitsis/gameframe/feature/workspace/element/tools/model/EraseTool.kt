package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import android.graphics.Color
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

class EraseTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        val erase = if (layer.isBackground) Color.GRAY else Color.TRANSPARENT
        layer.colorGrid.setColor(erase, column, row)
    }
}
