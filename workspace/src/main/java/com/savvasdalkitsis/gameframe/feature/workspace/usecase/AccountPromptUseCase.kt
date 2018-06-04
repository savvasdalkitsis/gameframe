package com.savvasdalkitsis.gameframe.feature.workspace.usecase

import com.savvasdalkitsis.gameframe.infra.injector.RxSharedPreferencesInjector
import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences

private const val KEY = "has_seen_account_prompt"

class AccountPromptUseCase(private val preferences: RxSharedPreferences = RxSharedPreferencesInjector.rxSharedPreferences()) {

    fun hasSeenAccountPrompt(): Boolean = preferences.getBoolean(KEY).blockingGet()

    fun markAccountUpsellSeen() {
        preferences.setBoolean(KEY, true)
    }
}