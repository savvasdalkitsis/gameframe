package com.savvasdalkitsis.gameframe.control.presenter;

import com.savvasdalkitsis.gameframe.control.view.ControlView;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.usecase.GameFrameUseCase;

import rx.Observable;

public class ControlPresenter {

    private ControlView controlView;
    private final GameFrameUseCase gameFrameUseCase;

    public ControlPresenter(GameFrameUseCase gameFrameUseCase) {
        this.gameFrameUseCase = gameFrameUseCase;
    }

    public void bindView(ControlView controlView) {
        this.controlView = controlView;
    }

    public void togglePower() {
        runCommand(gameFrameUseCase.togglePower());
    }

    public void menu() {
        runCommand(gameFrameUseCase.menu());
    }

    public void next() {
        runCommand(gameFrameUseCase.next());
    }

    private void runCommand(Observable<Void> command) {
        command.compose(RxTransformers.interceptIpMissingException())
                .compose(RxTransformers.schedulers())
                .subscribe(n -> controlView.operationSuccess(), e -> controlView.operationFailure(e));
    }
}
