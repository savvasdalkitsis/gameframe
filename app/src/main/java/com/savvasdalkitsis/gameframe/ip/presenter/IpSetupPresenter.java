package com.savvasdalkitsis.gameframe.ip.presenter;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.ip.view.IpSetupView;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.usecase.GameFrameUseCase;

public class IpSetupPresenter {

    private final IpRepository ipRepository;
    private IpSetupView ipSetupView;
    private final GameFrameUseCase gameFrameUseCase;

    public IpSetupPresenter(GameFrameUseCase gameFrameUseCase, IpRepository ipRepository) {
        this.ipRepository = ipRepository;
        this.gameFrameUseCase = gameFrameUseCase;
    }

    public void bindView(IpSetupView ipSetupView) {
        this.ipSetupView = ipSetupView;
        ipSetupView.displayLoading();
        ipRepository.getIpAddress()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::displayIpAddress, ipSetupView::errorLoadingIpAddress);
    }

    public void setup(IpAddress ipAddress) {
        ipRepository.saveIpAddress(ipAddress);
        ipSetupView.addressSaved(ipAddress);
    }

    public void discoverIp() {
        ipSetupView.displayLoading();
        gameFrameUseCase.discoverGameFrameIp()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::ipAddressDiscovered, ipSetupView::errorLoadingIpAddress);
    }

}
