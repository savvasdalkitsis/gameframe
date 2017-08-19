package com.savvasdalkitsis.gameframe.infra.kotlin

fun Int.clip(from: Int, to: Int): Int = Math.max(from, Math.min(this, to))
