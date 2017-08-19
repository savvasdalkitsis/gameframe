package com.savvasdalkitsis.gameframe.composition.model

interface BlendMode {

    fun blend(source: ARGB, dest: ARGB): ARGB
}
