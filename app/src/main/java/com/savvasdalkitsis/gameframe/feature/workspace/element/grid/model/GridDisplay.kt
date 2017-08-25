package com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model

interface GridDisplay {

    fun display(grid: Grid)
    fun current(): Grid
}