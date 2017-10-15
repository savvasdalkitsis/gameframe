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
package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

class FillTool : AbstractDrawingTool() {

    override fun drawOn(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        fill(layer.colorGrid, column, row, color, layer.colorGrid.getColor(column, row))
    }

    fun fill(grid: Grid, column: Int, row: Int, newColor: Int, target: Int) {
        if (ColorGrid.isOutOfBounds(column, row)
                || target == newColor
                || grid.getColor(column, row) != target) {
            return
        }
        grid.setColor(newColor, column, row)
        fill(grid, column, row + 1, newColor, target)
        fill(grid, column, row - 1, newColor, target)
        fill(grid, column + 1, row, newColor, target)
        fill(grid, column - 1, row, newColor, target)
    }
}
