package com.savvasdalkitsis.gameframe.injector.android

import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.savvasdalkitsis.gameframe.injector.ApplicationInjector.application

object SharedPreferencesInjector {

    fun sharedPreferences(): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application())

}
