package com.savvasdalkitsis.gameframe.injector.rx;

import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences;

import static com.savvasdalkitsis.gameframe.injector.android.SharedPreferencesInjector.sharedPreferences;

public class RxSharedPreferencesInjector {

    public static RxSharedPreferences rxSharedPreferences() {
        return new RxSharedPreferences(sharedPreferences());
    }

}
