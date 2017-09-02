package com.savvasdalkitsis.gameframe.infra.kotlin

import android.view.View
import java.io.InputStream

typealias OnLayerVisibilityChangedListener = (visible:Boolean) -> Unit
typealias InputStreamProvider = () -> InputStream
typealias Action = () -> Unit
typealias ViewAction = (View) -> Unit
typealias TypeAction<T> = (T) -> Unit