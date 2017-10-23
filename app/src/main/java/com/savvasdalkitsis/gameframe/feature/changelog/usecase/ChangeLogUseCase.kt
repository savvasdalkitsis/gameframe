package com.savvasdalkitsis.gameframe.feature.changelog.usecase

import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences
import com.savvasdalkitsis.gameframe.injector.infra.rx.RxSharedPreferencesInjector

private const val KEY = "has_seen_change_log"

class ChangeLogUseCase(private val preferences: RxSharedPreferences = RxSharedPreferencesInjector.rxSharedPreferences()) {

    fun hasSeenChangeLog(): Boolean = preferences.getBoolean(KEY).blockingGet()

    fun markChangeLogSeen() {
        preferences.setBoolean(KEY, true)
    }
}