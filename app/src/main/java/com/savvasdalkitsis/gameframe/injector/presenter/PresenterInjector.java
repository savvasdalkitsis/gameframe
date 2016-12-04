package com.savvasdalkitsis.gameframe.injector.presenter;

import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter;
import com.savvasdalkitsis.gameframe.injector.gameframe.api.GameFrameApiInjector;
import com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector;
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector;
import com.savvasdalkitsis.gameframe.ip.presenter.IpSetupPresenter;

import static com.savvasdalkitsis.gameframe.injector.gameframe.api.GameFrameApiInjector.gameFrameApi;
import static com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector.ipRepository;
import static com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.gameFrameUseCase;

public class PresenterInjector {

    public static IpSetupPresenter ipSetupPresenter() {
        return new IpSetupPresenter(gameFrameUseCase(), ipRepository());
    }

    public static ControlPresenter controlPresenter() {
        return new ControlPresenter(gameFrameApi());
    }
}
