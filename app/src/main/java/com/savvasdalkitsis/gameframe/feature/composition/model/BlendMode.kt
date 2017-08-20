package com.savvasdalkitsis.gameframe.feature.composition.model

interface BlendMode {

    fun blend(source: ARGB, dest: ARGB): ARGB
}
