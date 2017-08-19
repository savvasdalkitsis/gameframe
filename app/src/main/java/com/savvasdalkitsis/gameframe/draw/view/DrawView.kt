package com.savvasdalkitsis.gameframe.draw.view

import com.savvasdalkitsis.gameframe.grid.model.Grid
import com.savvasdalkitsis.gameframe.infra.kotlin.TypeAction

interface DrawView {

    fun askForFileName(name: TypeAction<String>)

    fun fileUploaded()

    fun failedToUpload(e: Throwable)

    fun displayUploading()

    fun drawingAlreadyExists(name: String, colorGrid: Grid, e: Throwable)

    fun failedToDelete(e: Throwable)
}
