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
package com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model

import com.savvasdalkitsis.gameframe.feature.history.model.Moment
import java.util.*

data class Palette(
        var title: String,
        var isSelected: Boolean = false,
        val colors: IntArray) : Moment<Palette> {

    fun changeColor(index: Int, color: Int) {
        colors[index] = color
    }

    override fun replicateMoment() = this.copy(colors = colors.copyOf())

    override fun isIdenticalTo(moment: Palette) = this == moment

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Palette

        if (title != other.title) return false
        if (isSelected != other.isSelected) return false
        if (!Arrays.equals(colors, other.colors)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + isSelected.hashCode()
        result = 31 * result + Arrays.hashCode(colors)
        return result
    }
}
