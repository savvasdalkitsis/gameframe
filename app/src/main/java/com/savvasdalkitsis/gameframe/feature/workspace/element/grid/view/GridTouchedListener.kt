package com.savvasdalkitsis.gameframe.feature.workspace.element.grid.view

interface GridTouchedListener {

    fun onGridTouchStarted()

    fun onGridTouch(startColumn: Int, startRow: Int, column: Int, row: Int)

    fun onGridTouchFinished()
}
