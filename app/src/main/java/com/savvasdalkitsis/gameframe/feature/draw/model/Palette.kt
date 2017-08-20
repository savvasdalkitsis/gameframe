package com.savvasdalkitsis.gameframe.feature.draw.model

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
