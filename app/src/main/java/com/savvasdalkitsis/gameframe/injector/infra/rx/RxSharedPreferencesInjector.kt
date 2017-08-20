package com.savvasdalkitsis.gameframe.injector.infra.rx

import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences

import com.savvasdalkitsis.gameframe.injector.infra.android.SharedPreferencesInjector.sharedPreferences

object RxSharedPreferencesInjector {

    fun rxSharedPreferences() = RxSharedPreferences(sharedPreferences())

}
