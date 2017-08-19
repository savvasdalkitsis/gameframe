package com.savvasdalkitsis.gameframe.grid.view

interface GridTouchedListener {

    fun onGridTouchStarted()

    fun onGridTouch(startColumn: Int, startRow: Int, column: Int, row: Int)

    fun onGridTouchFinished()
}
