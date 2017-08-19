package com.savvasdalkitsis.gameframe.infra.kotlin

import android.view.View

typealias OnLayerVisibilityChangedListener = (visible:Boolean) -> Unit
typealias Action = () -> Unit
typealias ViewAction = (View) -> Unit
typealias TypeAction<T> = (T) -> Unit