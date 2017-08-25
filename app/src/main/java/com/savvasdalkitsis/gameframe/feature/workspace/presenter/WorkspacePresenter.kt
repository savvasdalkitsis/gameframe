package com.savvasdalkitsis.gameframe.feature.workspace.presenter

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.composition.usecase.BlendUseCase
import com.savvasdalkitsis.gameframe.feature.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.raster.usecase.BmpUseCase
import com.savvasdalkitsis.gameframe.feature.saves.model.FileAlreadyExistsException
import com.savvasdalkitsis.gameframe.feature.saves.usecase.SaveFileUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.GridDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.view.GridTouchedListener
import com.savvasdalkitsis.gameframe.feature.workspace.model.Project
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.feature.workspace.view.WorkspaceView
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File

class WorkspacePresenter<O>(private val gameFrameUseCase: GameFrameUseCase,
                            private val saveFileUseCase: SaveFileUseCase,
                            private val bmpUseCase: BmpUseCase,
                            private val blendUseCase: BlendUseCase): GridTouchedListener {

    private lateinit var view: WorkspaceView<O>
    private lateinit var gridDisplay: GridDisplay
    private var uploading: Boolean = false
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

    fun bindView(view: WorkspaceView<O>, gridDisplay: GridDisplay) {
        this.view = view
        this.gridDisplay = gridDisplay
        view.observe(history)
        history.observe()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    render(it)
                    view.bindPalette(it.selectedPalette)
                }
    }

    fun upload(colorGrid: Grid) {
        if (!uploading) {
            view.askForFileName(R.string.upload) { name -> upload(name, colorGrid) }
        }
    }

    fun replaceDrawing(name: String, colorGrid: Grid) {
        view.displayUploading()
        uploading = true
        saveFileUseCase.deleteDirectory(name)
                .concatWith { gameFrameUseCase.removeFolder(name) }
                .compose(RxTransformers.schedulers())
                .doOnTerminate { uploading = false }
                .subscribe({ upload(name, colorGrid) }, { view.failedToDelete(it) })
    }

    fun saveWorkspace() {

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
        render(present)
    }

    override fun onGridTouchFinished() {
        view.finishStroke(present.selectedLayer)
        history.collapsePresentWithPastIfTheSame()
    }

    fun prepareOptions(options: O) {
        history.hasPast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hasPast -> if (hasPast) {
                    view.enableUndo(options)
                } else {
                    view.disableUndo(options)
                }}
        history.hasFuture()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hasFuture -> if (hasFuture) {
                    view.enableRedo(options)
                } else {
                    view.disableRedo(options)
                }}
        if (displayLayoutBorders) {
            view.displayLayoutBordersEnabled(options)
        } else {
            view.displayLayoutBordersDisabled(options)
        }
    }

    fun selectedOptionBorders() {
        displayLayoutBorders = !displayLayoutBorders
        render(present)
    }

    fun selectedOptionUndo() {
        history.stepBackInTime()
    }

    fun selectedOptionRedo() {
        history.stepForwardInTime()
    }

    private fun upload(name: String, colorGrid: Grid) {
        view.displayUploading()
        uploading = true
        saveFileUseCase.saveFile(name, "0.bmp", { bmpUseCase.rasterizeToBmp(colorGrid) })
                .flatMap<File> { file -> gameFrameUseCase.createFolder(name).toSingleDefault<File>(file) }
                .flatMapCompletable { gameFrameUseCase.uploadFile(it) }
                .concatWith { gameFrameUseCase.play(name) }
                .compose(RxTransformers.schedulers())
                .doOnTerminate { uploading = false }
                .subscribe({ view.fileUploaded() }, { e ->
                    if (e is FileAlreadyExistsException || e is AlreadyExistsOnGameFrameException) {
                        view.drawingAlreadyExists(name, colorGrid, e)
                    } else {
                        view.failedToUpload(e)
                    }
                })
    }

    private fun render(model: WorkspaceModel) {
        model.layers.forEach { blendUseCase.renderOn(it, gridDisplay) }
        val selected = model.layers.firstOrNull { it.isSelected }

        if (selected != null && displayLayoutBorders) {
            val colorGrid = selected.colorGrid
            view.displayBoundaries(colorGrid.columnTranslation, colorGrid.rowTranslation)
        } else {
            view.clearBoundaries()
        }
        view.rendered()
    }
}
