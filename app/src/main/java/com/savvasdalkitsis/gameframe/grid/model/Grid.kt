package com.savvasdalkitsis.gameframe.grid.model

import android.support.annotation.ColorInt

import com.savvasdalkitsis.gameframe.model.Moment

interface Grid : Moment<Grid> {

    fun setColor(@ColorInt color: Int, column: Int, row: Int)

    fun fill(color: Int): Grid

    @ColorInt
    fun getColor(column: Int, row: Int): Int
}
