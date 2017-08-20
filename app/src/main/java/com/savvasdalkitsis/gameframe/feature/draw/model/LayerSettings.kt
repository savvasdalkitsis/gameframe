package com.savvasdalkitsis.gameframe.feature.draw.model

import com.savvasdalkitsis.gameframe.feature.composition.model.AvailableBlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailablePorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.history.model.Moment

data class LayerSettings(
        val alpha: Float = 1f,
        val blendMode: BlendMode = AvailableBlendMode.defaultMode(),
        val porterDuffOperator: PorterDuffOperator = AvailablePorterDuffOperator.defaultOperator(),
        var title: String = ""): Moment<LayerSettings> {

    override fun replicateMoment() = this.copy()

    override fun isIdenticalTo(moment: LayerSettings) = this == moment
}
