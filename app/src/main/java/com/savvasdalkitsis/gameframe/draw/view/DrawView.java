package com.savvasdalkitsis.gameframe.draw.view;

import rx.functions.Action1;

public interface DrawView {

    void askForFileName(Action1<String> name);

    void fileUploaded();

    void failedToUpload(Throwable e);

    void displayUploading();

    void savedDrawingAlreadyExists(Throwable e);

    void alreadyExistsOnGameFrame(Throwable e);
}
