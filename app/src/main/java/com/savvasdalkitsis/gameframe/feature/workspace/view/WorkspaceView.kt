package com.savvasdalkitsis.gameframe.feature.workspace.view

import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.infra.kotlin.TypeAction

interface WorkspaceView<in O> {

    fun askForFileName(positiveText: Int, nameEntered: TypeAction<String>)
    fun displayProgress()
    fun stopProgress()
    fun drawingAlreadyExists(name: String, colorGrid: Grid, e: Throwable)
    fun observe(history: HistoryUseCase<WorkspaceModel>)
    fun bindPalette(selectedPalette: Palette)
    fun drawLayer(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int)
    fun finishStroke(layer: Layer)
    fun enableUndo(options: O)
    fun disableUndo(options: O)
    fun enableRedo(options: O)
    fun disableRedo(options: O)
    fun displayLayoutBordersEnabled(options: O)
    fun displayLayoutBordersDisabled(options: O)
    fun displayBoundaries(col: Int, row: Int)
    fun clearBoundaries()
    fun rendered()
    fun displayProjectName(name: String)
    fun askForProjectToLoad(projectNames: List<String>)
    fun askForProjectsToDelete(projectNames: List<String>)
    fun operationFailed(e: Throwable)
    fun showSuccess()
    fun displayNoSavedProjectsExist()
    fun askForApprovalToDismissChanges()
    fun displaySelectedLayerName(layerName: String)
}
