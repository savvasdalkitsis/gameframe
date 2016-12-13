package com.savvasdalkitsis.gameframe.widget.presenter;

import android.util.Log;

import com.savvasdalkitsis.gameframe.control.view.PowerTileService;
import com.savvasdalkitsis.gameframe.infra.navigation.Navigator;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase;
import com.savvasdalkitsis.gameframe.widget.view.WidgetView;

import rx.Observable;

public class WidgetPresenter {

    private final GameFrameUseCase gameFrameUseCase;
    private final IpRepository ipRepository;
    private final Navigator navigator;
    private WidgetView widgetView;

    public WidgetPresenter(GameFrameUseCase gameFrameUseCase, IpRepository ipRepository, Navigator navigator) {
        this.gameFrameUseCase = gameFrameUseCase;
        this.ipRepository = ipRepository;
        this.navigator = navigator;
    }

    public void bindView(WidgetView widgetView) {
        this.widgetView = widgetView;
    }

    public void menu() {
        perform(gameFrameUseCase.menu());
    }

    public void next() {
        perform(gameFrameUseCase.next());
    }

    public void power() {
        perform(gameFrameUseCase.togglePower());
    }

    private void perform(Observable<Void> operation) {
        ipRepository.getIpAddress()
                .doOnError(e -> {
                    Log.e(PowerTileService.class.getName(), "IP address not found", e);
                    navigator.navigateToIpSetup();
                })
                .onErrorResumeNext(Observable.empty())
                .flatMap(ip -> operation)
                .compose(RxTransformers.schedulers())
                .subscribe(n -> {}, e -> {
                    Log.e(PowerTileService.class.getName(), "Error communicating with the GameFrame", e);
                    widgetView.operationError();
                });
    }
}
