package com.savvasdalkitsis.gameframe.main.presenter;

import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.main.view.MainView;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;

public class MainPresenter {

    private final IpRepository ipRepository;
    private MainView mainView;

    public MainPresenter(IpRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public void bindView(MainView mainView) {
        this.mainView = mainView;
    }

    public void loadIpAddress() {
        ipRepository.getIpAddress()
                .compose(RxTransformers.schedulers())
                .subscribe(mainView::ipAddressLoaded, mainView::ipCouldNotBeFound);
    }
}
