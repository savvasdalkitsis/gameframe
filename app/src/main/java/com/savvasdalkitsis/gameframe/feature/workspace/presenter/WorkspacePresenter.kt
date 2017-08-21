package com.savvasdalkitsis.gameframe.feature.workspace.presenter

import com.savvasdalkitsis.gameframe.feature.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.saves.model.SavedDrawingAlreadyExistsException
import com.savvasdalkitsis.gameframe.feature.saves.usecase.SavedDrawingUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.view.WorkspaceView
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import java.io.File

class WorkspacePresenter(private val gameFrameUseCase: GameFrameUseCase,
                         private val savedDrawingUseCase: SavedDrawingUseCase) {

    private lateinit var view: WorkspaceView
    private var uploading: Boolean = false

    fun bindView(view: WorkspaceView) {
        this.view = view
    }

    fun upload(colorGrid: Grid) {
        if (!uploading) {
            view.askForFileName { name -> upload(name, colorGrid) }
        }
    }

    fun replaceDrawing(name: String, colorGrid: Grid) {
        view.displayUploading()
        uploading = true
        savedDrawingUseCase.deleteDrawing(name)
                .concatWith { gameFrameUseCase.removeFolder(name) }
                .compose(RxTransformers.schedulers())
                .doOnTerminate { uploading = false }
                .subscribe({ upload(name, colorGrid) }, { view.failedToDelete(it) })
    }

    private fun upload(name: String, colorGrid: Grid) {
        view.displayUploading()
        uploading = true
        savedDrawingUseCase.saveDrawing(name, colorGrid)
                .flatMap<File> { file -> gameFrameUseCase.createFolder(name).toSingleDefault<File>(file) }
                .flatMapCompletable { gameFrameUseCase.uploadFile(it) }
                .concatWith { gameFrameUseCase.play(name) }
                .compose(RxTransformers.schedulers())
                .doOnTerminate { uploading = false }
                .subscribe({ view.fileUploaded() }, { e ->
                    if (e is SavedDrawingAlreadyExistsException || e is AlreadyExistsOnGameFrameException) {
                        view.drawingAlreadyExists(name, colorGrid, e)
                    } else {
                        view.failedToUpload(e)
                    }
                })
    }
}
