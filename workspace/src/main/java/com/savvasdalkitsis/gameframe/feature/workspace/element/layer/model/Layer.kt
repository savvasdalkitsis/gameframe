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
