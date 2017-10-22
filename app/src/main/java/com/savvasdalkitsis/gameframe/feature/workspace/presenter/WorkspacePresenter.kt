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
package com.savvasdalkitsis.gameframe.feature.workspace.presenter

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.bmp.usecase.BitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.composition.usecase.BlendUseCase
import com.savvasdalkitsis.gameframe.feature.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.history.model.MomentList
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay
import com.savvasdalkitsis.gameframe.feature.navigation.Navigator
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.GridDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.view.GridTouchedListener
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.model.Project
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.UnsupportedProjectVersionException
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.view.WorkspaceView
import com.savvasdalkitsis.gameframe.infra.android.StringUseCase
import com.savvasdalkitsis.gameframe.infra.kotlin.Action
import com.savvasdalkitsis.gameframe.infra.kotlin.or
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File


class WorkspacePresenter<Options, in BitmapSource>(private val gameFrameUseCase: GameFrameUseCase,
                                                   private val blendUseCase: BlendUseCase,
                                                   private val workspaceUseCase: WorkspaceUseCase,
                                                   private val stringUseCase: StringUseCase,
                                                   private val messageDisplay: MessageDisplay,
                                                   private val navigator: Navigator,
                                                   private val bitmapFileUseCase: BitmapFileUseCase<BitmapSource>) : GridTouchedListener {

    private lateinit var view: WorkspaceView<Options>
    private lateinit var gridDisplay: GridDisplay
    private var tempName: String? = null
    private var project = Project(history = HistoryUseCase(WorkspaceModel()))
    private val history: HistoryUseCase<WorkspaceModel>
        get() = project.history
    private val present: WorkspaceModel
        get() = history.present
    private var displayLayoutBorders
        get() = project.displayLayoutBorders
        set(value) {
            project.displayLayoutBorders = value
        }
    private var savedState: WorkspaceModel? = present
    private val modified
        get() = present != savedState

    fun bindView(view: WorkspaceView<Options>, gridDisplay: GridDisplay) {
        this.view = view
        this.gridDisplay = gridDisplay
        view.observe(history)
        history.observe()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    render(it.layers)
                    view.bindPalette(it.selectedPalette)
                    displayProjectName()
                }
    }

    fun saveWorkspace(successAction: Action? = null) {
        view.displayProgress()
        project.name?.let { tempName = it }
        tempName?.let { name ->
            tempName = null
            workspaceUseCase.saveProject(name, present)
                    .compose(RxTransformers.schedulers<File>())
                    .subscribe(
                            {
                                projectChangedSuccessfully(name)
                                successAction?.invoke()
                            },
                            projectSaveFailed()
                    )
        } ?: view.askForFileName(R.string.save) {
            tempName = it
            saveWorkspace(successAction)
        }
    }

    fun loadProject(name: String? = null) {
        view.displayProgress()
        when {
            name != null -> workspaceUseCase.load(name)
                    .compose(RxTransformers.schedulers<WorkspaceModel>())
                    .subscribe(projectLoaded(name), {
                        view.operationFailed(it)
                        if (it is UnsupportedProjectVersionException) {
                            view.displayUnsupportedVersion()
                        }
                    })
            else -> workspaceUseCase.savedProjects()
                    .compose(RxTransformers.schedulers<List<String>>())
                    .subscribe(savedProjectsLoaded {
                        view.stopProgress()
                        view.askForProjectToLoad(it)
                    }, {
                        view.operationFailed(it)
                    })
        }
    }

    fun deleteProjects(names: List<String>? = null) {
        view.displayProgress()
        if (names?.isEmpty() == false) {
            Flowable.fromIterable(names)
                    .flatMapCompletable(workspaceUseCase::deleteProject)
                    .compose(RxTransformers.schedulers())
                    .subscribe(projectsDeleted(names), { view.operationFailed(it) })
        } else {
            workspaceUseCase.savedProjects()
                    .compose(RxTransformers.schedulers<List<String>>())
                    .subscribe(savedProjectsLoaded {
                        view.stopProgress()
                        view.askForProjectsToDelete(it)
                    }, {
                        view.operationFailed(it)
                    })
        }
    }

    fun createNewProject(force: Boolean = false) {
        if (!modified || force) {
            history.restartFrom(WorkspaceModel())
            projectModified(null)
        } else {
            view.askForApprovalToDismissChanges()
        }
    }

    private fun projectsDeleted(names: List<String>) = {
        view.showSuccess()
        if (names.contains(project.name)) {
            project.name = null
            displayProjectName()
        }
    }

    private fun savedProjectsLoaded(onNonEmpty: (List<String>) -> Unit): (List<String>) -> Unit = {
        if (it.isEmpty()) {
            view.displayNoSavedProjectsExist()
        } else {
            onNonEmpty(it)
        }
    }

    private fun projectLoaded(name: String): (WorkspaceModel) -> Unit = {
        history.restartFrom(it)
        projectChangedSuccessfully(name)
    }

    private fun projectSaveFailed(): (Throwable) -> Unit = {
        view.operationFailed(it)
        displayProjectName()
    }

    private fun projectChangedSuccessfully(name: String) {
        view.showSuccess()
        projectModified(name)
    }

    private fun projectModified(name: String?) {
        project.name = name
        savedState = present
        displayProjectName()
    }

    private fun displayProjectName() {
        view.displayProjectName("${project.name ?: stringUseCase.getString(R.string.untitled)}${if (modified) "*" else ""}")
    }

    fun changeColor(currentColor: Int, newColor: Int, paletteIndex: Int) {
        if (currentColor != newColor) {
            with(history) {
                progressTimeWithoutAnnouncing()
                present.selectedPalette.changeColor(paletteIndex, newColor)
                collapsePresentWithPastIfTheSame()
                announcePresent()
            }
        }
    }

    override fun onGridTouchStarted() {
        history.progressTime()
    }

    override fun onGridTouch(startColumn: Int, startRow: Int, column: Int, row: Int) {
        view.drawLayer(present.selectedLayer, startColumn, startRow, column, row)
        render(present.layers)
    }

    override fun onGridTouchFinished() {
        view.finishStroke(present.selectedLayer)
        history.collapsePresentWithPastIfTheSame()
    }

    fun prepareOptions(options: Options) {
        history.hasPast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hasPast ->
                    if (hasPast) {
                        view.enableUndo(options)
                    } else {
                        view.disableUndo(options)
                    }
                }
        history.hasFuture()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hasFuture ->
                    if (hasFuture) {
                        view.enableRedo(options)
                    } else {
                        view.disableRedo(options)
                    }
                }
        if (displayLayoutBorders) {
            view.displayLayoutBordersEnabled(options)
        } else {
            view.displayLayoutBordersDisabled(options)
        }
    }

    fun selectedOptionBorders() {
        displayLayoutBorders = !displayLayoutBorders
        render(present.layers)
        messageDisplay.show(
                if (displayLayoutBorders)
                    R.string.layer_borders_displaying
                else
                    R.string.layer_borders_not_displaying
        )
    }

    fun selectedOptionUndo() {
        history.stepBackInTime()
    }

    fun selectedOptionRedo() {
        history.stepForwardInTime()
    }

    fun replaceDrawing(name: String, colorGrid: Grid) {
        view.displayProgress()
        gameFrameUseCase.removeFolder(name)
                .compose(RxTransformers.schedulers())
                .subscribe({ upload(colorGrid) }, { view.operationFailed(it) })
    }

    fun upload(grid: Grid) {
        if (modified || project.name == null) {
            saveWorkspace { upload(grid) }
        } else {
            val name = project.name as String
            gameFrameUseCase.uploadAndDisplay(name, grid)
                    .compose(RxTransformers.schedulers())
                    .subscribe({ view.showSuccess() }, { e ->
                        if (e is AlreadyExistsOnGameFrameException) {
                            view.drawingAlreadyExists(name, grid, e)
                        } else {
                            view.operationFailed(e)
                        }
                    })
        }
    }

    fun takeUserToPlayStore() {
        navigator.navigateToPlayStore()
    }

    fun exportImage(bitmapSource: BitmapSource) {
        if (modified || project.name == null) {
            saveWorkspace { exportImage(bitmapSource) }
        } else {
            val name = project.name as String
            bitmapFileUseCase.getFileFor(bitmapSource, name)
                    .flatMapCompletable { navigator.navigateToShareImageFile(it, name) }
                    .subscribe({}, { view.operationFailed(it) })
        }
    }

    private fun render(layers: MomentList<Layer>) {
        layers.forEach { blendUseCase.renderOn(it, gridDisplay) }
        val selected = layers.firstOrNull { it.isSelected }

        if (selected != null && displayLayoutBorders) {
            val colorGrid = selected.colorGrid
            view.displayBoundaries(colorGrid.columnTranslation, colorGrid.rowTranslation)
        } else {
            view.clearBoundaries()
        }
        view.displaySelectedLayerName(selected?.layerSettings?.title.or(stringUseCase.getString(R.string.background)))
        view.displaySelectedPalette(stringUseCase.getString(R.string.palette_name, present.selectedPalette.title))
        view.rendered()
    }
}
