package com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model

import com.savvasdalkitsis.gameframe.feature.history.model.Moment
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid

data class Layer(
        var layerSettings: LayerSettings,
        val colorGrid: ColorGrid = ColorGrid(),
        val isBackground: Boolean = false,
        var isVisible: Boolean = true,
        var isSelected: Boolean = false): Moment<Layer> {

    override fun replicateMoment() = this.copy(
            layerSettings = layerSettings.replicateMoment(),
            colorGrid = colorGrid.replicateMoment()
    )

    override fun isIdenticalTo(moment: Layer) =
            isBackground == moment.isBackground
                    && isVisible == moment.isVisible
                    && isSelected == moment.isSelected
                    && colorGrid.isIdenticalTo(moment.colorGrid)
                    && layerSettings.isIdenticalTo(moment.layerSettings)
}
