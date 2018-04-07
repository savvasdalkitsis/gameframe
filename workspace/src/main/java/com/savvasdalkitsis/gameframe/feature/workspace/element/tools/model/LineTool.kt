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

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid

class LineTool : ScratchTool() {

    override fun drawOnScratch(scratch: Grid, startColumn: Int, startRow: Int, column: Int, row: Int, color: Int) {
        var x = startColumn
        var y = startRow

        val dx = Math.abs(column - x)
        val dy = Math.abs(row - y)

        val sx = if (x < column) 1 else -1
        val sy = if (y < row) 1 else -1

        var err = dx - dy
        var e2: Int

        while (true) {
            scratch.setColor(color, x, y)

            if (x == column && y == row) {
                break
            }

            e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x += sx
            }

            if (e2 < dx) {
                err += dx
                y += sy
            }
        }
    }
}
