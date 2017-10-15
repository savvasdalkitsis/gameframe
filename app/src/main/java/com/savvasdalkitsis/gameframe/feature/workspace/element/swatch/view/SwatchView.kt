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
package com.savvasdalkitsis.gameframe.feature.workspace.element.swatch.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet

import com.afollestad.materialdialogs.color.CircleView
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view.PaletteView

class SwatchView : CircleView {

    var color: Int = 0
        private set
    var index: Int = 0
        private set
    private lateinit var paletteView: PaletteView
    private lateinit var circlePath: Path
    private lateinit var circleRect: RectF
    private lateinit var tile: Drawable
    private var binding: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener {
            paletteView.deselectAllSwatches()
            isSelected = true
            paletteView.notifyListenerOfSwatchSelected(this)
        }
        setOnLongClickListener {
            paletteView.notifyListenerOfSwatchLongClicked(this)
            true
        }
    }

    @Suppress("DEPRECATION")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        paletteView = parent as PaletteView
        circlePath = Path()
        circleRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredWidth.toFloat())

        tile = resources.getDrawable(R.drawable.transparency_background)
    }

    @Suppress("OverridingDeprecatedMember")
    override fun setBackground(background: Drawable?) {
        // needed to avoid parent illegal argument exception when inflating from xml
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
        circlePath.reset()
        circleRect.set(2f, 2f, (measuredWidth - 2).toFloat(), (measuredWidth - 2).toFloat())
        circlePath.addOval(circleRect, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(circlePath)
        tile.setBounds(0, 0, measuredWidth, measuredWidth)
        tile.draw(canvas)
        canvas.restore()
        super.onDraw(canvas)
    }

    fun bind(color: Int, index: Int) {
        this.color = color
        this.index = index
        binding = true
        setBackgroundColor(color)
        binding = false
    }

    @SuppressLint("MissingSuperCall")
    override fun requestLayout() {
        if (!binding) {
            super.requestLayout()
        }
    }
}
