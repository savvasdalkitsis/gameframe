package com.savvasdalkitsis.gameframe.feature.bitmap.model

interface Bitmap {

    val dimensions: Pair<Int, Int>
    fun getPixelAt(col: Int, row: Int): Int
}