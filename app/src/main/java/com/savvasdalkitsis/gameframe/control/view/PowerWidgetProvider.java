package com.savvasdalkitsis.gameframe.control.view;

import android.util.Log;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector;
import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.usecase.GameFrameUseCase;

public class PowerWidgetProvider extends ClickableWidgetProvider {

    private final GameFrameUseCase gameFrameUseCase = UseCaseInjector.gameFrameUseCase();

    @Override
    protected int layoutResId() {
        return R.layout.widget_power;
    }

    @Override
    protected void onClick() {
        gameFrameUseCase.togglePower()
                .compose(RxTransformers.schedulers())
                .subscribe(n -> {}, e -> {
                    Log.e(PowerTileService.class.getName(), "Error toggling game frame power", e);
                });
    }
}
