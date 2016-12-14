package com.savvasdalkitsis.gameframe.draw.presenter;

import com.savvasdalkitsis.gameframe.draw.view.DrawView;
import com.savvasdalkitsis.gameframe.gameframe.model.AlreadyExistsOnGameFrameException;
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.saves.model.SavedDrawingAlreadyExistsException;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.saves.usecase.SavedDrawingUseCase;

public class DrawPresenter {

    private final GameFrameUseCase gameFrameUseCase;
    private final SavedDrawingUseCase savedDrawingUseCase;
    private DrawView view;
    private boolean uploading;

    public DrawPresenter(GameFrameUseCase gameFrameUseCase, SavedDrawingUseCase savedDrawingUseCase) {
        this.gameFrameUseCase = gameFrameUseCase;
        this.savedDrawingUseCase = savedDrawingUseCase;
    }

    public void bindView(DrawView view) {
        this.view = view;
    }

    public void upload(ColorGrid colorGrid) {
        if (!uploading) {
            view.askForFileName(name -> upload(name, colorGrid));
        }
    }

    public void replaceDrawing(String name, ColorGrid colorGrid) {
        view.displayUploading();
        uploading = true;
        savedDrawingUseCase.deleteDrawing(name)
                .flatMap(n -> gameFrameUseCase.removeFolder(name))
                .compose(RxTransformers.schedulers())
                .doOnTerminate(() -> uploading = false)
                .subscribe(n -> upload(name, colorGrid), view::failedToDelete);
    }

    private void upload(String name, ColorGrid colorGrid) {
        view.displayUploading();
        uploading = true;
        savedDrawingUseCase.saveDrawing(name, colorGrid)
                .flatMap(file -> gameFrameUseCase.createFolder(name).map(n -> file))
                .flatMap(gameFrameUseCase::uploadFile)
                .flatMap(n -> gameFrameUseCase.play(name))
                .compose(RxTransformers.schedulers())
                .doOnTerminate(() -> uploading = false)
                .subscribe(n -> view.fileUploaded(), (e) -> {
                    if (e instanceof SavedDrawingAlreadyExistsException
                            || e instanceof AlreadyExistsOnGameFrameException) {
                        view.drawingAlreadyExists(name, colorGrid, e);
                    } else {
                        view.failedToUpload(e);
                    }
                });
    }
}
