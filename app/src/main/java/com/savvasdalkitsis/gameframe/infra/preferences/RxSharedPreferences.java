package com.savvasdalkitsis.gameframe.infra.preferences;

import android.content.SharedPreferences;

import rx.Observable;

public class RxSharedPreferences {

    private final SharedPreferences sharedPreferences;

    public RxSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Observable<String> getString(String key) {
        return Observable.just(sharedPreferences.getString(key, null));
    }

    public void setString(String key, String value) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }
}
