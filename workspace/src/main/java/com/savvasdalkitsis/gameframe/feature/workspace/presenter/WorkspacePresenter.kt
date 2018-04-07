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

import com.savvasdalkitsis.gameframe.feature.bitmap.usecase.BitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.composition.usecase.BlendUseCase
import com.savvasdalkitsis.gameframe.feature.device.model.AlreadyExistsOnDeviceException
import com.savvasdalkitsis.gameframe.feature.device.usecase.DeviceUseCase
import com.savvasdalkitsis.gameframe.feature.history.model.MomentList
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.ip.model.IpBaseHostMissingException
import com.savvasdalkitsis.gameframe.feature.ip.navigation.IpNavigator
import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay
import com.savvasdalkitsis.gameframe.feature.networking.model.WifiNotEnabledException
import com.savvasdalkitsis.gameframe.feature.networking.usecase.WifiUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.GridDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.asBitmap
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.view.GridTouchedListener
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.model.Project
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.feature.workspace.navigation.WorkspaceNavigator
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.UnsupportedProjectVersionException
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.view.WorkspaceView
import com.savvasdalkitsis.gameframe.infra.android.StringUseCase
import com.savvasdalkitsis.gameframe.infra.base.BasePresenter
import com.savvasdalkitsis.gameframe.infra.base.plusAssign
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import com.savvasdalkitsis.gameframe.kotlin.Action
import com.savvasdalkitsis.gameframe.kotlin.or
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class WorkspacePresenter<Options, in BitmapSource>(private val deviceUseCase: DeviceUseCase,
                                                   private val blendUseCase: BlendUseCase,
                                                   private val workspaceUseCase: WorkspaceUseCase,
                                                   private val stringUseCase: StringUseCase,
                                                   private val messageDisplay: MessageDisplay,
                                                   private val navigator: WorkspaceNavigator,
                                                   private val ipNavigator: IpNavigator,
                                                   private val bitmapFileUseCase: BitmapFileUseCase<BitmapSource>,
                                                   private val wifiUseCase: WifiUseCase) : GridTouchedListener, BasePresenter<WorkspaceView<Options>>() {

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
    private val historyStream = CompositeDisposable()

    fun bindGrid(gridDisplay: GridDisplay) {
        this.gridDisplay = gridDisplay
        view?.observe(history)
        historyStream += history.observe()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    render(it.layers)
                    view?.bindPalette(it.selectedPalette)
                    displayProjectName()
                }
    }

    fun saveWorkspace(successAction: Action? = null) {
        view?.displayProgress()
        project.name?.let { tempName = it }
        tempName?.let { name ->
            tempName = null
            managedStreams += workspaceUseCase.saveProject(name, present)
                    .compose(RxTransformers.schedulers())
                    .subscribe(
                            {
                                projectChangedSuccessfully(name)
                                successAction?.invoke()
                            },
                            projectSaveFailed()
                    )
        } ?: view?.askForFileName(R.string.save, {
            tempName = it
            saveWorkspace(successAction)
        }, {
            view?.stopProgress()
        })
    }

    fun loadProject(name: String? = null) {
        view?.displayProgress()
        managedStreams += when (name) {
            null -> workspaceUseCase.savedProjectNames()
                    .compose(RxTransformers.schedulers<List<String>>())
                    .subscribe(savedProjectsLoaded {
                        view?.stopProgress()
                        view?.askForProjectToLoad(it)
                    }, {
                        view?.operationFailed(it)
                    })
            else -> workspaceUseCase.load(name)
                    .compose(RxTransformers.schedulers<WorkspaceModel>())
                    .subscribe(projectLoaded(name), {
                        view?.operationFailed(it)
                        if (it is UnsupportedProjectVersionException) {
                            view?.displayUnsupportedVersion()
                        }
                    })
        }
    }

    fun deleteProjects(names: List<String>? = null) {
        view?.displayProgress()
        managedStreams += if (names?.isEmpty() == false) {
            Flowable.fromIterable(names)
                    .flatMapCompletable(workspaceUseCase::deleteProject)
                    .compose(RxTransformers.schedulers())
                    .subscribe(projectsDeleted(names), { view?.operationFailed(it) })
        } else {
            workspaceUseCase.savedProjectNames()
                    .compose(RxTransformers.schedulers<List<String>>())
                    .subscribe(savedProjectsLoaded {
                        view?.stopProgress()
                        view?.askForProjectsToDelete(it)
                    }, {
                        view?.operationFailed(it)
                    })
        }
    }

    fun createNewProject(force: Boolean = false) {
        if (!modified || force) {
            history.restartFrom(WorkspaceModel())
            projectModified(null)
        } else {
            view?.askForApprovalToDismissChanges()
        }
    }

    private fun projectsDeleted(names: List<String>) = {
        view?.showSuccess()
        if (names.contains(project.name)) {
            project.name = null
            displayProjectName()
        }
    }

    private fun savedProjectsLoaded(onNonEmpty: (List<String>) -> Unit): (List<String>) -> Unit = {
        if (it.isEmpty()) {
            view?.displayNoSavedProjectsExist()
        } else {
            onNonEmpty(it)
        }
    }

    private fun projectLoaded(name: String): (WorkspaceModel) -> Unit = {
        history.restartFrom(it)
        projectChangedSuccessfully(name)
    }

    private fun projectSaveFailed(): (Throwable) -> Unit = {
        view?.operationFailed(it)
        displayProjectName()
    }

    private fun projectChangedSuccessfully(name: String) {
        view?.showSuccess()
        projectModified(name)
    }

    private fun projectModified(name: String?) {
        project.name = name
        savedState = present
        displayProjectName()
    }

    private fun displayProjectName() {
        view?.displayProjectName("${project.name ?: stringUseCase.getString(R.string.untitled)}${if (modified) "*" else ""}")
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
        view?.drawLayer(present.selectedLayer, startColumn, startRow, column, row)
        render(present.layers)
    }

    override fun onGridTouchFinished() {
        view?.finishStroke(present.selectedLayer)
        history.collapsePresentWithPastIfTheSame()
    }

    fun prepareOptions(options: Options) {
        managedStreams += history.hasPast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hasPast ->
                    if (hasPast) {
                        view?.enableUndo(options)
                    } else {
                        view?.disableUndo(options)
                    }
                }
        managedStreams += history.hasFuture()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hasFuture ->
                    if (hasFuture) {
                        view?.enableRedo(options)
                    } else {
                        view?.disableRedo(options)
                    }
                }
        if (displayLayoutBorders) {
            view?.displayLayoutBordersEnabled(options)
        } else {
            view?.displayLayoutBordersDisabled(options)
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
        view?.displayProgress()
        managedStreams += deviceUseCase.removeFolder(name)
                .compose(RxTransformers.schedulers())
                .subscribe({ upload(colorGrid) }, { view?.operationFailed(it) })
    }

    fun upload(grid: Grid) {
        if (needsSave()) {
            saveWorkspace { upload(grid) }
        } else {
            val name = project.name as String
            managedStreams +=
                deviceUseCase.uploadAndDisplay(name, grid.asBitmap())
                        .compose(RxTransformers.schedulers())
                        .subscribe({ view?.showSuccess() }, { e -> when (e) {
                            is AlreadyExistsOnDeviceException -> view?.drawingAlreadyExists(name, grid, e)
                            is IpBaseHostMissingException -> {
                                view?.operationFailed(e)
                                ipNavigator.navigateToIpSetup()
                            }
                            is WifiNotEnabledException -> view?.wifiNotEnabledError(e)else -> view?.operationFailed(e)
                        } })

        }
    }

    fun takeUserToPlayStore() {
        navigator.navigateToPlayStore()
    }

    fun exportImage(bitmapSource: BitmapSource) {
        if (needsSave()) {
            saveWorkspace { exportImage(bitmapSource) }
        } else {
            val name = project.name as String
            managedStreams += bitmapFileUseCase.getFileFor(bitmapSource, name)
                    .flatMapCompletable { navigator.navigateToShareImageFile(it, name) }
                    .subscribe({}, { view?.operationFailed(it) })
        }
    }

    private fun needsSave() = modified || project.name == null

    private fun render(layers: MomentList<Layer>) {
        layers.forEach { renderOn(it, gridDisplay) }
        val selected = layers.firstOrNull { it.isSelected }

        if (selected != null && displayLayoutBorders) {
            val colorGrid = selected.colorGrid
            view?.displayBoundaries(colorGrid.columnTranslation, colorGrid.rowTranslation)
        } else {
            view?.clearBoundaries()
        }
        view?.displaySelectedLayerName(selected?.layerSettings?.title.or(stringUseCase.getString(R.string.background)))
        view?.displaySelectedPalette(stringUseCase.getString(R.string.palette_name, present.selectedPalette.title))
        view?.rendered()
    }

    fun enableWifi() {
        wifiUseCase.enableWifi()
    }

    fun paused() {
        historyStream.clear()
    }

    private fun renderOn(layer: Layer, gridDisplay: GridDisplay) { with(layer) {
        when { isVisible ->
            gridDisplay.display(if (isBackground) {
                colorGrid
            } else {
                ColorGrid.from(blendUseCase.compose(gridDisplay.current().asBitmap(), colorGrid.asBitmap(), layerSettings.blendMode, layerSettings.porterDuffOperator, layerSettings.alpha))
            })
        }
    } }
}
