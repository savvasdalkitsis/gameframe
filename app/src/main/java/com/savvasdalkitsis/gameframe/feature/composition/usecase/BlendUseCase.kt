package com.savvasdalkitsis.gameframe.feature.composition.usecase

import com.savvasdalkitsis.gameframe.feature.composition.model.ARGB
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.GridDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer

class BlendUseCase {

    fun renderOn(layer: Layer, gridDisplay: GridDisplay) { with(layer) {
        when { isVisible ->
            gridDisplay.display(if (isBackground) {
                colorGrid
            } else {
                compose(gridDisplay.current(), colorGrid, layerSettings.blendMode, layerSettings.porterDuffOperator, layerSettings.alpha)
            })
        }
    } }

    fun compose(dest: Grid, source: Grid, blendMode: BlendMode, porterDuffOperator: PorterDuffOperator, alpha: Float) =
            ColorGrid().apply {
                for (col in 1..ColorGrid.SIDE) {
                    for (row in 1..ColorGrid.SIDE) {
                        val blend = mix(source.getColor(col, row), dest.getColor(col, row),
                                blendMode, porterDuffOperator, alpha)
                        setColor(blend.color(), col, row)
                    }
                }
            }

    fun mix(source: Int, dest: Int, blendMode: BlendMode, porterDuffOperator: PorterDuffOperator, alpha: Float): ARGB {
        val argb = ARGB(source)
        val destination = ARGB(dest)
        val blend = blend(argb, destination, blendMode).withAlphaValue(argb.a).multiplyAlpha(alpha)
        return compose(blend, destination, porterDuffOperator)
    }

    private fun blend(source: ARGB, dest: ARGB, blendMode: BlendMode) =
            source * (1 - alphaFloat(dest)) + blendMode.blend(source, dest) * alphaFloat(dest)

    private fun compose(source: ARGB, dest: ARGB, porterDuffOperator: PorterDuffOperator) =
            source * (alphaFloat(source) * porterDuffOperator.fa(dest)) + dest * (alphaFloat(dest) * porterDuffOperator.fb(source))

    private fun alphaFloat(argb: ARGB) = argb.a / 255f
}
