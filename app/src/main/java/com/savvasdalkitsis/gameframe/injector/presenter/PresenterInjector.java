package com.savvasdalkitsis.gameframe.injector.presenter;

import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter;
import com.savvasdalkitsis.gameframe.ip.presenter.IpSetupPresenter;
import com.savvasdalkitsis.gameframe.main.presenter.MainPresenter;

import static com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector.ipRepository;
import static com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.gameFrameUseCase;
import static com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.ipDiscoveryUseCase;

public class PresenterInjector {

    public static IpSetupPresenter ipSetupPresenter() {
        return new IpSetupPresenter(gameFrameUseCase(), ipRepository(), ipDiscoveryUseCase());
    }

    public static ControlPresenter controlPresenter() {
        return new ControlPresenter(gameFrameUseCase(), ipRepository());
    }

    public static MainPresenter mainPresenter() {
        return new MainPresenter(ipRepository());
    }
}
