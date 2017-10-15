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
package com.savvasdalkitsis.gameframe.feature.workspace.element.grid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.GridDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.infra.kotlin.clip

class LedGridView : View, GridDisplay {

    var colorGrid: Grid = ColorGrid()
        private set
    private var tileSide: Float = 0f
    private lateinit var paint: Paint
    private var thumbBackground: Drawable? = null
    private var gridTouchedListener: GridTouchedListener? = null
    private var startX: Float = 0f
    private var startY: Float = 0f
    private var columnTranslation: Int = 0
    private var rowTranslation: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        paint = Paint()
        paint.strokeWidth = resources.getDimensionPixelSize(R.dimen.grid_line_width).toFloat()
        setOnTouchListener(touched())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        tileSide = measuredWidth / ColorGrid.SIDE.toFloat()
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

    @Suppress("DEPRECATION")
    fun setThumbnailMode() {
        paint.strokeWidth = resources.getDimensionPixelSize(R.dimen.grid_line_width_thumbnail).toFloat()
        isEnabled = false

        thumbBackground = resources.getDrawable(R.drawable.transparency_backround_tiled)
    }

    override fun display(grid: Grid) {
        this.colorGrid = grid
        invalidate()
    }

    override fun current() = colorGrid

    fun displayBoundaries(columnTranslation: Int, rowTranslation: Int) {
        this.columnTranslation = columnTranslation
        this.rowTranslation = rowTranslation
    }

    fun clearBoundaries() {
        displayBoundaries(0, 0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val gridSide = measuredWidth
        thumbBackground?.run {
            setBounds(0, 0, gridSide, gridSide)
            draw(canvas)
        }
        for (column in 1..ColorGrid.SIDE) {
            for (row in 1..ColorGrid.SIDE) {
                val left = toPx(column - 1)
                val top = toPx(row - 1)
                paint.color = colorGrid.getColor(column, row)
                canvas.drawRect(left, top, left + tileSide, top + tileSide, paint)
            }
        }
        paint.color = Color.BLACK
        for (i in 0..ColorGrid.SIDE) {
            drawRow(i, 0, canvas)
            drawColumn(i, 0, canvas)
        }
        if (columnTranslation != 0 || rowTranslation != 0) {
            paint.color = Color.LTGRAY

            drawRow(rowTranslation, columnTranslation, canvas)
            drawRow(ColorGrid.SIDE + rowTranslation, columnTranslation, canvas)
            drawColumn(columnTranslation, rowTranslation, canvas)
            drawColumn(ColorGrid.SIDE + columnTranslation, rowTranslation, canvas)
        }
    }

    private fun drawColumn(column: Int, verticalOffset: Int, canvas: Canvas) {
        if (column >= 0 && column <= ColorGrid.SIDE) {
            canvas.drawLine(toPx(column), toPx(verticalOffset), toPx(column), measuredWidth + toPx(verticalOffset), paint)
        }
    }

    private fun drawRow(row: Int, horizontalOffset: Int, canvas: Canvas) {
        if (row >= 0 && row <= ColorGrid.SIDE) {
            canvas.drawLine(toPx(horizontalOffset), toPx(row), measuredWidth + toPx(horizontalOffset), toPx(row), paint)
        }
    }

    private fun toPx(tile: Int) = tile * tileSide

    fun setOnGridTouchedListener(gridTouchedListener: GridTouchedListener) {
        this.gridTouchedListener = gridTouchedListener
    }

    private fun touched(): View.OnTouchListener = OnTouchListener { _, event ->
        if (!isEnabled) {
            return@OnTouchListener false
        }
        val block = width / ColorGrid.SIDE
        val x: Float
        val y: Float
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                x = event.x
                y = event.y
                gridTouchedListener?.onGridTouchStarted()
                gridTouchedListener?.onGridTouch(
                        blockCoordinate(block, startX),
                        blockCoordinate(block, startY),
                        blockCoordinate(block, x),
                        blockCoordinate(block, y)
                )
                return@OnTouchListener true
            }
            MotionEvent.ACTION_MOVE -> {
                x = event.x
                y = event.y
                gridTouchedListener?.onGridTouch(
                        blockCoordinate(block, startX),
                        blockCoordinate(block, startY),
                        blockCoordinate(block, x),
                        blockCoordinate(block, y)
                )
                return@OnTouchListener true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> gridTouchedListener?.onGridTouchFinished()
        }
        false
    }

    private fun blockCoordinate(block: Int, coordinate: Float): Int {
        val c = (coordinate / block).toInt() + 1
        return c.clip(1, ColorGrid.SIDE)
    }
}
