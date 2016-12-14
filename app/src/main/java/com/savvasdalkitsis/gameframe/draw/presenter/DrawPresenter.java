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

    public DrawPresenter(GameFrameUseCase gameFrameUseCase, SavedDrawingUseCase savedDrawingUseCase) {
        this.gameFrameUseCase = gameFrameUseCase;
        this.savedDrawingUseCase = savedDrawingUseCase;
    }

    public void bindView(DrawView view) {
        this.view = view;
    }

    public void upload(ColorGrid colorGrid) {
        view.askForFileName(name -> {
            view.displayUploading();
            savedDrawingUseCase.saveDrawing(name, colorGrid)
                    .flatMap(file -> gameFrameUseCase.createFolder(name).map(n -> file))
                    .flatMap(gameFrameUseCase::uploadFile)
                    .flatMap(n -> gameFrameUseCase.play(name))
                    .compose(RxTransformers.schedulers())
                    .subscribe(n -> view.fileUploaded(), (e) -> {
                        if (e instanceof SavedDrawingAlreadyExistsException) {
                            view.savedDrawingAlreadyExists(e);
                        } else if (e instanceof AlreadyExistsOnGameFrameException) {
                            view.alreadyExistsOnGameFrame(e);
                        } else {
                            view.failedToUpload(e);
                        }
                    });
        });
    }
}
