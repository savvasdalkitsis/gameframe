package com.savvasdalkitsis.gameframe.infra.preferences

import android.content.SharedPreferences
import com.savvasdalkitsis.gameframe.infra.kotlin.toMaybe
import io.reactivex.Maybe
import io.reactivex.Single

class RxSharedPreferences(private val sharedPreferences: SharedPreferences) {

    fun getString(key: String): Maybe<String> = sharedPreferences.getString(key, null).toMaybe()

    fun setString(key: String, value: String) {
        transaction { putString(key, value) }
    }

    fun getBoolean(key: String): Single<Boolean> = Single.just(sharedPreferences.getBoolean(key, false))

    fun setBoolean(key: String, value: Boolean) {
        transaction { putBoolean(key, value) }
    }

    private fun transaction(transaction: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit().apply(transaction).apply()
    }
}
