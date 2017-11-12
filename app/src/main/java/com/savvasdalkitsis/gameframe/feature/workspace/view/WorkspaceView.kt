/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.workspace.view

import com.savvasdalkitsis.gameframe.base.BaseView
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.infra.kotlin.Action
import com.savvasdalkitsis.gameframe.infra.kotlin.TypeAction

interface WorkspaceView<in O>: BaseView {

    fun askForFileName(positiveText: Int, nameEntered: TypeAction<String>, cancelAction: Action)
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
    fun displaySelectedPalette(paletteName: String)
    fun displayUnsupportedVersion()
    fun wifiNotEnabledError(e: Throwable)
}
