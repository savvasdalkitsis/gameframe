package com.savvasdalkitsis.gameframe.feature.workspace.view

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.infra.kotlin.TypeAction

interface WorkspaceView {

    fun askForFileName(name: TypeAction<String>)

    fun fileUploaded()

    fun failedToUpload(e: Throwable)

    fun displayUploading()

    fun drawingAlreadyExists(name: String, colorGrid: Grid, e: Throwable)

    fun failedToDelete(e: Throwable)
}
