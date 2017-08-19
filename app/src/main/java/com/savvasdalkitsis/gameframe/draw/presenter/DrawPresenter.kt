package com.savvasdalkitsis.gameframe.draw.presenter

import com.savvasdalkitsis.gameframe.draw.view.DrawView
import com.savvasdalkitsis.gameframe.gameframe.model.AlreadyExistsOnGameFrameException
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.grid.model.Grid
import com.savvasdalkitsis.gameframe.saves.model.SavedDrawingAlreadyExistsException
import com.savvasdalkitsis.gameframe.rx.RxTransformers
import com.savvasdalkitsis.gameframe.saves.usecase.SavedDrawingUseCase
import java.io.File

class DrawPresenter(private val gameFrameUseCase: GameFrameUseCase,
                    private val savedDrawingUseCase: SavedDrawingUseCase) {

    private lateinit var view: DrawView
    private var uploading: Boolean = false

    fun bindView(view: DrawView) {
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
