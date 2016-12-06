package com.savvasdalkitsis.gameframe.control.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.util.Log;

import com.savvasdalkitsis.gameframe.rx.RxTransformers;
import com.savvasdalkitsis.gameframe.usecase.GameFrameUseCase;

import static com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.gameFrameUseCase;

@TargetApi(Build.VERSION_CODES.N)
public class PowerTileService extends TileService {

    private final GameFrameUseCase gameFrameUseCase = gameFrameUseCase();

    @Override
    public void onClick() {
        super.onClick();
        gameFrameUseCase.togglePower()
                .compose(RxTransformers.schedulers())
                .subscribe(n -> {}, e -> {
                    Log.e(PowerTileService.class.getName(), "Error toggling game frame power", e);
                });
    }
}
