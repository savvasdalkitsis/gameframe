package com.savvasdalkitsis.gameframe.draw.view;

import com.savvasdalkitsis.gameframe.grid.model.Grid;

import rx.functions.Action1;

public interface DrawView {

    void askForFileName(Action1<String> name);

    void fileUploaded();

    void failedToUpload(Throwable e);

    void displayUploading();

    void drawingAlreadyExists(String name, Grid colorGrid, Throwable e);

    void failedToDelete(Throwable e);
}
