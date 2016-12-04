package com.savvasdalkitsis.gameframe.injector.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.savvasdalkitsis.gameframe.injector.ApplicationInjector.application;

public class SharedPreferencesInjector {

    public static SharedPreferences sharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application());
    }

}
