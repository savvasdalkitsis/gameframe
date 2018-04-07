/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
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
