package com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model

import android.graphics.Color
import android.support.annotation.ColorInt

import com.savvasdalkitsis.gameframe.feature.history.model.Moment

const val DEFAULT_BACKGROUND_COLOR = Color.GRAY

interface Grid : Moment<Grid> {

    fun setColor(@ColorInt color: Int, column: Int, row: Int)

    fun fill(color: Int): Grid

    @ColorInt
    fun getColor(column: Int, row: Int): Int
}
