package com.savvasdalkitsis.gameframe.composition.usecase

import com.savvasdalkitsis.gameframe.composition.model.ARGB
import com.savvasdalkitsis.gameframe.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator
import com.savvasdalkitsis.gameframe.draw.model.Layer
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.grid.model.Grid
import com.savvasdalkitsis.gameframe.grid.view.LedGridView

class BlendUseCase {

    fun renderOn(layer: Layer, ledGridView: LedGridView) {
        if (!layer.isVisible) {
            return
        }
        if (layer.isBackground) {
            ledGridView.display(layer.colorGrid)
        } else {
            val composite = compose(ledGridView.colorGrid, layer.colorGrid, layer.layerSettings.blendMode, layer.layerSettings.porterDuffOperator, layer.layerSettings.alpha)
            ledGridView.display(composite)
        }
    }

    fun compose(dest: Grid, source: Grid, blendMode: BlendMode, porterDuffOperator: PorterDuffOperator, alpha: Float): ColorGrid {
        val colorGrid = ColorGrid()
        for (col in 1..ColorGrid.SIDE) {
            for (row in 1..ColorGrid.SIDE) {
                val blend = mix(source.getColor(col, row), dest.getColor(col, row),
                        blendMode, porterDuffOperator, alpha)
                colorGrid.setColor(blend.color(), col, row)
            }
        }
        return colorGrid
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
