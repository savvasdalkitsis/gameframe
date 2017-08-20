package com.savvasdalkitsis.gameframe.feature.draw.view

import com.savvasdalkitsis.gameframe.feature.grid.model.Grid
import com.savvasdalkitsis.gameframe.infra.kotlin.TypeAction

interface DrawView {

    fun askForFileName(name: TypeAction<String>)

    fun fileUploaded()

    fun failedToUpload(e: Throwable)

    fun displayUploading()

    fun drawingAlreadyExists(name: String, colorGrid: Grid, e: Throwable)

    fun failedToDelete(e: Throwable)
}
