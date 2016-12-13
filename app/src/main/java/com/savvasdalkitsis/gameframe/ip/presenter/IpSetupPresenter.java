package com.savvasdalkitsis.gameframe.ip.presenter;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase;
import com.savvasdalkitsis.gameframe.ip.view.IpSetupView;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase;

import rx.subscriptions.CompositeSubscription;

import static com.savvasdalkitsis.gameframe.ip.model.IpAddress.Builder.ipAddress;

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
        loadStoredIp();
    }

    public void unbind() {
        subscriptions.clear();
    }

    public void setup(IpAddress ipAddress) {
        ipRepository.saveIpAddress(ipAddress);
        ipSetupView.addressSaved(ipAddress);
    }

    public void discoverIp() {
        ipSetupView.displayDiscovering();
        subscriptions.add(ipDiscoveryUseCase.monitoredIps()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::tryingAddress, e -> {}));
        subscriptions.add(gameFrameUseCase.discoverGameFrameIp()
                .compose(RxTransformers.schedulers())
                .doOnCompleted(() -> ipSetupView.displayIdleView())
                .subscribe(ipSetupView::ipAddressDiscovered, (throwable) -> {
                    ipSetupView.errorDiscoveringIpAddress(throwable);
                    loadStoredIp();
                }));
    }

    public void cancelDiscover() {
        subscriptions.clear();
        loadStoredIp();
    }

    private void loadStoredIp() {
        ipSetupView.displayIdleView();
        ipRepository.getIpAddress()
                .subscribe(
                        ipSetupView::displayIpAddress,
                        e -> { ipSetupView.displayIpAddress(ipAddress().build());}
                );
    }
}
