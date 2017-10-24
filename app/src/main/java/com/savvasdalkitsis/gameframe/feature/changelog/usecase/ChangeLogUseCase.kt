package com.savvasdalkitsis.gameframe.feature.changelog.usecase

import com.savvasdalkitsis.gameframe.BuildConfig
import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences
import com.savvasdalkitsis.gameframe.injector.infra.rx.RxSharedPreferencesInjector

private const val KEY = "has_seen_change_log_for_version"

class ChangeLogUseCase(private val preferences: RxSharedPreferences = RxSharedPreferencesInjector.rxSharedPreferences()) {

    fun hasSeenChangeLog(): Boolean = preferences.getString(KEY).blockingGet() == BuildConfig.VERSION_NAME

    fun markChangeLogSeen() {
        preferences.setString(KEY, BuildConfig.VERSION_NAME)
    }
}