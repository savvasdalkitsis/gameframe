package com.savvasdalkitsis.gameframe.feature.workspace.model

import com.savvasdalkitsis.gameframe.feature.history.model.Moment
import com.savvasdalkitsis.gameframe.feature.history.model.MomentList
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.DEFAULT_BACKGROUND_COLOR
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.LayerSettings
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palettes


class WorkspaceModel constructor(val layers: MomentList<Layer> = newLayers(),
                                 val palettes: MomentList<Palette> = newPalettes()) : Moment<WorkspaceModel> {

    val selectedPalette: Palette
        get() = palettes.first { it.isSelected }

    val selectedLayer: Layer
        get() = layers.first { it.isSelected }

    override fun replicateMoment(): WorkspaceModel {
        return WorkspaceModel(layers.replicateMoment(), palettes.replicateMoment())
    }

    override fun isIdenticalTo(moment: WorkspaceModel): Boolean {
        return layers.isIdenticalTo(moment.layers) && palettes.isIdenticalTo(moment.palettes)
    }

    companion object {
        private fun newLayers() = MomentList(Layer(layerSettings = LayerSettings(
                title = "Background"),
                isBackground = true,
                isSelected = true,
                colorGrid = ColorGrid().fill(DEFAULT_BACKGROUND_COLOR),
                isVisible = true)
        )

        private fun newPalettes() = MomentList(Palettes.preLoaded()
                .filter { it != Palettes.emptyPalette() }
                .apply {
                    val default = indexOfFirst { it == Palettes.defaultPalette() }
                    this[default].isSelected = true
                }
        )
    }
}
