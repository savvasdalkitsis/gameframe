package com.savvasdalkitsis.gameframe.infra.preferences

import android.content.SharedPreferences

import rx.Observable

class RxSharedPreferences(private val sharedPreferences: SharedPreferences) {

    fun getString(key: String): Observable<String> =
            Observable.just(sharedPreferences.getString(key, null))

    fun setString(key: String, value: String) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply()
    }
}
