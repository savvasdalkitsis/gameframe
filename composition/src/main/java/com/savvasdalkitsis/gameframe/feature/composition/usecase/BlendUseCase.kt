/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.composition.usecase

import com.savvasdalkitsis.gameframe.feature.bitmap.model.Bitmap
import com.savvasdalkitsis.gameframe.feature.bitmap.model.RasterBitmap
import com.savvasdalkitsis.gameframe.feature.composition.model.ARGB
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator

class BlendUseCase {

    fun compose(dest: Bitmap, source: Bitmap, blendMode: BlendMode, porterDuffOperator: PorterDuffOperator, alpha: Float): Bitmap {
        val (sw, sh) = source.dimensions
        val (dw, dh) = dest.dimensions
        if (sw != dw || sh != dh) throw IllegalArgumentException("Can only compose bitmaps of the same dimensions. Was source($sw:$sh) dest($dw:$dh)")
        return RasterBitmap(sw, sh).apply {
            for (col in 0 until sw) {
                for (row in 0 until sh) {
                    val blend = mix(source.getPixelAt(col, row), dest.getPixelAt(col, row),
                            blendMode, porterDuffOperator, alpha)
                    this[col][row] = blend.color()
                }
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
