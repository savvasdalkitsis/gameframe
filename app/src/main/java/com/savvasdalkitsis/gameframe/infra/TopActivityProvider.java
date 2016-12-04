package com.savvasdalkitsis.gameframe.infra;

import android.app.Activity;
import android.support.annotation.Nullable;

public interface TopActivityProvider {

    @Nullable
    Activity getTopActivity();

}
