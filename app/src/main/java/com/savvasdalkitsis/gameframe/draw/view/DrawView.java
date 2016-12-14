package com.savvasdalkitsis.gameframe.draw.view;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

import rx.functions.Action1;

public interface DrawView {

    void askForFileName(Action1<String> name);

    void fileUploaded();

    void failedToUpload(Throwable e);

    void displayUploading();

    void drawingAlreadyExists(String name, ColorGrid colorGrid, Throwable e);

    void failedToDelete(Throwable e);
}
