package com.savvasdalkitsis.gameframe.feature.bitmap.model

class RasterBitmap(w: Int, h: Int) : Bitmap {

    private val colors = Array(w) { IntArray(h) }

    override val dimensions = Pair(w, h)

    override fun getPixelAt(col: Int, row: Int) = this[col][row]

    operator fun get(col: Int) = colors[col]
}