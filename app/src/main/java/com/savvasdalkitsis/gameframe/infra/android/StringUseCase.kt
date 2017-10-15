package com.savvasdalkitsis.gameframe.infra.android

import android.content.Context
import android.support.annotation.StringRes

class StringUseCase(private val context:Context) {

    fun getString(@StringRes resId: Int): String = context.getString(resId)
    fun getString(@StringRes resId: Int, arg: String): String = context.getString(resId, arg)
}