package com.savvasdalkitsis.gameframe.ip.presenter;

import com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.ip.view.IpSetupView;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;

public class IpSetupPresenter {

    private final IpRepository ipRepository;
    private IpSetupView ipSetupView;

    public IpSetupPresenter() {
        ipRepository = IpRepositoryInjector.ipRepository();
    }

    public void bindView(IpSetupView ipSetupView) {
        this.ipSetupView = ipSetupView;
        ipRepository.getIpAddress()
                .compose(RxTransformers.schedulers())
                .subscribe(ipSetupView::displayIpAddress, ipSetupView::errorLoadingIpAddress);
    }

    public void setup(IpAddress ipAddress) {
        ipRepository.saveIpAddress(ipAddress);
        ipSetupView.addressSaved(ipAddress);
    }
}
