package com.savvasdalkitsis.gameframe.feature.composition

import com.savvasdalkitsis.gameframe.feature.composition.usecase.BlendUseCase

object CompositionInjector {
    fun blendUseCase() = BlendUseCase()
}