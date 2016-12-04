package com.savvasdalkitsis.gameframe.ip.presenter;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase;
import com.savvasdalkitsis.gameframe.ip.view.IpSetupView;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.usecase.GameFrameUseCase;

import rx.subscriptions.CompositeSubscription;

public class IpSetupPresenter {

    private final IpRepository ipRepository;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private IpSetupView ipSetupView;
    private final GameFrameUseCase gameFrameUseCase;
    private final IpDiscoveryUseCase ipDiscoveryUseCase;

    public IpSetupPresenter(GameFrameUseCase gameFrameUseCase, IpRepository ipRepository, IpDiscoveryUseCase ipDiscoveryUseCase) {
        this.ipRepository = ipRepository;
        this.gameFrameUseCase = gameFrameUseCase;
        this.ipDiscoveryUseCase = ipDiscoveryUseCase;
    }

    public void bindView(IpSetupView ipSetupView) {
        this.ipSetupView = ipSetupView;
        ipSetupView.displayLoading();
        subscriptions.add(ipRepository.getIpAddress()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::displayIpAddress, ipSetupView::errorLoadingIpAddress));
    }

    public void unbind() {
        subscriptions.clear();
    }

    public void setup(IpAddress ipAddress) {
        ipRepository.saveIpAddress(ipAddress);
        ipSetupView.addressSaved(ipAddress);
    }

    public void discoverIp() {
        ipSetupView.displayLoading();
        subscriptions.add(ipDiscoveryUseCase.monitoredIps()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::tryingAddress, e -> {}));
        subscriptions.add(gameFrameUseCase.discoverGameFrameIp()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::ipAddressDiscovered, ipSetupView::errorLoadingIpAddress));
    }

}
