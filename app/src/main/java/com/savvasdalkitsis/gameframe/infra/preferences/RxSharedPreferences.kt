package com.savvasdalkitsis.gameframe.infra.preferences

import android.content.SharedPreferences
import com.savvasdalkitsis.gameframe.infra.kotlin.toMaybe
import io.reactivex.Maybe

class RxSharedPreferences(private val sharedPreferences: SharedPreferences) {

    fun getString(key: String): Maybe<String> = sharedPreferences.getString(key, null).toMaybe()

    fun setString(key: String, value: String) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply()
    }
}
