package com.savvasdalkitsis.gameframe.control.presenter;

import com.savvasdalkitsis.gameframe.control.view.ControlView;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.model.Brightness;
import com.savvasdalkitsis.gameframe.model.ClockFace;
import com.savvasdalkitsis.gameframe.model.CycleInterval;
import com.savvasdalkitsis.gameframe.model.DisplayMode;
import com.savvasdalkitsis.gameframe.model.PlaybackMode;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase;

import rx.Observable;

public class ControlPresenter {

    private final IpRepository ipRepository;
    private ControlView controlView;
    private final GameFrameUseCase gameFrameUseCase;

    public ControlPresenter(GameFrameUseCase gameFrameUseCase, IpRepository ipRepository) {
        this.gameFrameUseCase = gameFrameUseCase;
        this.ipRepository = ipRepository;
    }

    public void bindView(ControlView controlView) {
        this.controlView = controlView;
    }

    public void loadIpAddress() {
        ipRepository.getIpAddress()
                .compose(RxTransformers.schedulers())
                .subscribe(controlView::ipAddressLoaded, controlView::ipCouldNotBeFound);
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
        runCommandAndIgnoreResult(gameFrameUseCase.setBrightness(brightness));
    }

    public void changePlaybackMode(PlaybackMode playbackMode) {
        runCommandAndIgnoreResult(gameFrameUseCase.setPlaybackMode(playbackMode));
    }

    public void changeCycleInterval(CycleInterval cycleInterval) {
        runCommandAndIgnoreResult(gameFrameUseCase.setCycleInterval(cycleInterval));
    }

    public void changeDisplayMode(DisplayMode displayMode) {
        runCommandAndIgnoreResult(gameFrameUseCase.setDisplayMode(displayMode));
    }

    public void changeClockFace(ClockFace clockFace) {
        runCommandAndIgnoreResult(gameFrameUseCase.setClockFace(clockFace));
    }

    private Observable<Void> runCommand(Observable<Void> command) {
        return command.compose(RxTransformers.interceptIpMissingException())
                .compose(RxTransformers.schedulers());
    }

    private void runCommandAndNotifyView(Observable<Void> command) {
        runCommand(command).subscribe(n -> controlView.operationSuccess(), e -> controlView.operationFailure(e));
    }

    private void runCommandAndIgnoreResult(Observable<Void> command) {
        runCommand(command).subscribe(n -> {}, e -> {});
    }
}
