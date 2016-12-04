package com.savvasdalkitsis.gameframe.control.presenter;

import com.savvasdalkitsis.gameframe.control.view.ControlView;
import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;

import rx.Observable;

public class ControlPresenter {

    private ControlView controlView;
    private GameFrameApi gameFrameApi;

    public ControlPresenter(GameFrameApi gameFrameApi) {
        this.gameFrameApi = gameFrameApi;
    }

    public void bindView(ControlView controlView) {
        this.controlView = controlView;
    }

    public void togglePower() {
        runCommand(gameFrameApi.togglePower(""));
    }

    public void menu() {
        runCommand(gameFrameApi.menu(""));
    }

    private void runCommand(Observable<Void> command) {
        command.compose(RxTransformers.globalTransformation())
                .subscribe(n -> controlView.operationSuccess(), e -> controlView.operationFailure(e));
    }
}
