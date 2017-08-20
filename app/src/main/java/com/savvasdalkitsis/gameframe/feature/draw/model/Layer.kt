package com.savvasdalkitsis.gameframe.feature.draw.model

import com.savvasdalkitsis.gameframe.feature.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.history.model.Moment

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
