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
package com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model

import android.support.annotation.ArrayRes
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector

object Palettes {

    private val resources = ApplicationInjector.application().resources
    private val EMPTY_INDEX = 0
    private val DEFAULT_INDEX = 1

    fun preLoaded() = arrayOf(
            palette("Empty", R.array.palette_empty),
            palette("MS Paint", R.array.palette_ms_paint),
            palette("Apple II", R.array.palette_apple_II),
            palette("Commodore 64", R.array.palette_c64),
            palette("CGA", R.array.palette_cga),
            palette("Gameboy", R.array.palette_gameboy),
            palette("MSX", R.array.palette_msx),
            palette("NES 1", R.array.palette_nes1),
            palette("NES 2", R.array.palette_nes2),
            palette("NES 3", R.array.palette_nes3),
            palette("NES 4", R.array.palette_nes4),
            palette("ZX Spectrum", R.array.palette_zx_spectrum)
    )

    internal fun defaultPalette() = preLoaded()[DEFAULT_INDEX].replicateMoment()

    internal fun emptyPalette() = preLoaded()[EMPTY_INDEX].replicateMoment()

    private fun palette(title: String, @ArrayRes resource: Int) =
            Palette(title = title, colors = resources.getIntArray(resource).toMutableList())
}
