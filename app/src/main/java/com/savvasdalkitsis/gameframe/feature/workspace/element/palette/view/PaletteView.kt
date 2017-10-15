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
package com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view

import android.content.Context
import android.support.v7.widget.GridLayout
import android.util.AttributeSet

import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.element.swatch.view.SwatchSelectedListener
import com.savvasdalkitsis.gameframe.feature.workspace.element.swatch.view.SwatchView

class PaletteView : GridLayout {

    private lateinit var swatches: Array<SwatchView?>
    private var swatchSelectedListener: SwatchSelectedListener? = null
    private var thumbnailMode: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        val childCount = childCount
        if (childCount != SWATCH_COUNT) {
            throw IllegalStateException("Wrong number of swatches in PaletteView. Expecting ${SWATCH_COUNT} found $childCount")
        }
        swatches = arrayOfNulls(SWATCH_COUNT)
        for (i in 0 until SWATCH_COUNT) {
            swatches[i] = getChildAt(i) as SwatchView
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!thumbnailMode) {
            post { this.selectFirstSwatch() }
        }
    }

    fun setThumbnailMode() {
        thumbnailMode = true
        for (swatch in swatches) {
            swatch?.let { with(it) {
                setOnClickListener(null)
                setOnLongClickListener(null)
                isClickable = false
                isLongClickable = false
            } }
        }
    }

    fun bind(palette: Palette) {
        val colors = palette.colors
        for (i in 0 until SWATCH_COUNT) {
            swatches[i]?.bind(colors[i], i)
        }
    }

    fun deselectAllSwatches() {
        for (swatch in swatches) {
            swatch?.isSelected = false
        }
    }

    fun setOnSwatchSelectedListener(swatchSelectedListener: SwatchSelectedListener) {
        this.swatchSelectedListener = swatchSelectedListener
    }

    fun notifyListenerOfSwatchSelected(swatchView: SwatchView) {
        swatchSelectedListener?.onSwatchSelected(swatchView)
    }

    fun notifyListenerOfSwatchLongClicked(swatchView: SwatchView) {
        swatchSelectedListener?.onSwatchLongPressed(swatchView)
    }

    private fun selectFirstSwatch() {
        swatches[0]?.performClick()
    }

    companion object {

        private val SWATCH_COUNT = 16
    }
}
