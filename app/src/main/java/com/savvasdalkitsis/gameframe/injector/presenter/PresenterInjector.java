package com.savvasdalkitsis.gameframe.injector.presenter;

import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter;
import com.savvasdalkitsis.gameframe.injector.gameframe.api.GameFrameApiInjector;
import com.savvasdalkitsis.gameframe.ip.presenter.IpSetupPresenter;

public class PresenterInjector {

    public static IpSetupPresenter ipSetupPresenter() {
        return new IpSetupPresenter();
    }

    public static ControlPresenter controlPresenter() {
        return new ControlPresenter(GameFrameApiInjector.gameFrameApi());
    }
}
