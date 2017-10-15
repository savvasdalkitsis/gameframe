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
package com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model

import android.graphics.Color
import android.support.annotation.ColorInt

import com.savvasdalkitsis.gameframe.feature.history.model.Moment

const val DEFAULT_BACKGROUND_COLOR = Color.GRAY

interface Grid : Moment<Grid> {

    fun setColor(@ColorInt color: Int, column: Int, row: Int)

    fun fill(color: Int): Grid

    @ColorInt
    fun getColor(column: Int, row: Int): Int
}
