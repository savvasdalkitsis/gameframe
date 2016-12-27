package com.savvasdalkitsis.gameframe.saves.usecase;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.savvasdalkitsis.gameframe.GameFrameApplication;
import com.savvasdalkitsis.gameframe.bmp.usecase.BmpUseCase;
import com.savvasdalkitsis.gameframe.grid.model.Grid;
import com.savvasdalkitsis.gameframe.saves.model.SavedDrawingAlreadyExistsException;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;

public class SavedDrawingUseCase {

    private final BmpUseCase bmpUseCase;
    private final GameFrameApplication application;

    public SavedDrawingUseCase(BmpUseCase bmpUseCase, GameFrameApplication application) {
        this.bmpUseCase = bmpUseCase;
        this.application = application;
    }

    public Observable<File> saveDrawing(String name, Grid colorGrid) {
        return file(name)
                .flatMap(dir -> {
                    if (dir.exists()) {
                        return Observable.error(new SavedDrawingAlreadyExistsException("The directory '" + name + "' already exists"));
                    }
                    if (!dir.mkdirs()) {
                        return Observable.error(new IOException("Could not create directory " + dir.getAbsolutePath()));
                    }
                    return Observable.just(new File(dir, "0.bmp"));
                })
                .zipWith(bmpUseCase.rasterizeToBmp(colorGrid), Pair::create)
                .flatMap(save -> {
                    try {
                        FileOutputStream stream = new FileOutputStream(save.first);
                        stream.write(save.second);
                        stream.close();
                        return Observable.just(save.first);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                });
    }

    public Observable<Void> deleteDrawing(String name) {
        return file(name)
                .flatMap(dir -> {
                    if (!dir.exists()) {
                        return Observable.just(null);
                    }
                    try {
                        FileUtils.deleteDirectory(dir);
                        return Observable.just(null);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                });
    }

    @NonNull
    private Observable<File> file(String name) {
        return Observable.just(new File(application.getExternalFilesDir(null), name));
    }
}
