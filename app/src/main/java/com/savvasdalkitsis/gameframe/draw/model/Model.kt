package com.savvasdalkitsis.gameframe.draw.model

import android.graphics.Color

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.model.Moment
import com.savvasdalkitsis.gameframe.model.MomentList

import rx.Observable

class Model constructor(val layers: MomentList<Layer> = newLayers(),
                                val palettes: MomentList<Palette> = newPalettes()) : Moment<Model> {

    val selectedPalette: Palette
        get() = Observable.from(palettes)
                .first { it.isSelected }
                .toBlocking()
                .first()

    val selectedLayer: Layer
        get() = Observable.from(layers)
                .first{ it.isSelected }
                .toBlocking()
                .first()

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
