package com.savvasdalkitsis.gameframe.feature.draw.model

import android.graphics.Color

import com.savvasdalkitsis.gameframe.feature.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.history.model.Moment
import com.savvasdalkitsis.gameframe.feature.history.model.MomentList


class Model constructor(val layers: MomentList<Layer> = newLayers(),
                                val palettes: MomentList<Palette> = newPalettes()) : Moment<Model> {

    val selectedPalette: Palette
        get() = palettes.first { it.isSelected }

    val selectedLayer: Layer
        get() = layers.first { it.isSelected }

    override fun replicateMoment(): Model {
        return Model(layers.replicateMoment(), palettes.replicateMoment())
    }

    override fun isIdenticalTo(moment: Model): Boolean {
        return layers.isIdenticalTo(moment.layers) && palettes.isIdenticalTo(moment.palettes)
    }

    companion object {
        private fun newLayers() = MomentList(Layer(layerSettings = LayerSettings(
                        title = "Background"),
                        isBackground = true,
                        isSelected = true,
                        colorGrid = ColorGrid().fill(Color.GRAY),
                        isVisible = true)
        )

        private fun newPalettes() = MomentList(Palettes.defaultPalette().copy(isSelected = true))
    }
}
