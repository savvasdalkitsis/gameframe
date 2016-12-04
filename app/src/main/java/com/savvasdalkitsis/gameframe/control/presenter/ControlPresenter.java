package com.savvasdalkitsis.gameframe.control.presenter;

import com.savvasdalkitsis.gameframe.control.view.ControlView;
import com.savvasdalkitsis.gameframe.model.Brightness;
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
        runCommandAndNotifyView(gameFrameUseCase.togglePower());
    }

    public void menu() {
        runCommandAndNotifyView(gameFrameUseCase.menu());
    }

    public void next() {
        runCommandAndNotifyView(gameFrameUseCase.next());
    }

    public void changeBrightness(Brightness brightness) {
        runCommand(gameFrameUseCase.setBrightness(brightness))
                .subscribe(n -> {}, e -> {});
    }

    private Observable<Void> runCommand(Observable<Void> command) {
        return command.compose(RxTransformers.interceptIpMissingException())
                .compose(RxTransformers.schedulers());
    }

    private void runCommandAndNotifyView(Observable<Void> command) {
        runCommand(command).subscribe(n -> controlView.operationSuccess(), e -> controlView.operationFailure(e));
    }
}
