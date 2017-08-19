package com.savvasdalkitsis.gameframe.injector.rx

import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences

import com.savvasdalkitsis.gameframe.injector.android.SharedPreferencesInjector.sharedPreferences

object RxSharedPreferencesInjector {

    fun rxSharedPreferences() = RxSharedPreferences(sharedPreferences())

}
