package com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model

import android.graphics.Color
import android.support.annotation.ColorInt
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailableBlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailablePorterDuffOperator
import com.savvasdalkitsis.gameframe.infra.kotlin.clip
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector

class ColorGrid : Grid {

    private val colors = Array(SIDE) { IntArray(SIDE) }
    private var translateCol: Int = 0
    private var translateRow: Int = 0

    @Transient
    private val blendUseCase = UseCaseInjector.blendUseCase()
    @Transient
    private var transientTranslateCol: Int = 0
    @Transient
    private var transientTranslateRow: Int = 0
    @Transient
    private var scratch: ColorGrid? = null

    init {
        fill(Color.TRANSPARENT)
    }

    override fun setColor(@ColorInt color: Int, column: Int, row: Int) {
        checkValue(column, "Column")
        checkValue(row, "Row")
        val c = column - columnTranslation
        val r = row - rowTranslation
        if (!isOutOfBounds(c, r)) {
            colors[c - 1][r - 1] = color
        }
    }

    override fun fill(color: Int): ColorGrid {
        for (i in 1..SIDE) {
            for (j in 1..SIDE) {
                setColor(color, i, j)
            }
        }
        return this
    }

    @ColorInt
    override fun getColor(column: Int, row: Int): Int {
        checkValue(column, "Column")
        checkValue(row, "Row")
        val c = column - columnTranslation
        val r = row - rowTranslation
        if (isOutOfBounds(c, r)) {
            return Color.TRANSPARENT
        }
        val color = colors[c - 1][r - 1]
        return if (scratch == null) {
            color
        } else {
            blendUseCase.mix(scratch!!.getColor(column, row), color, AvailableBlendMode.NORMAL, AvailablePorterDuffOperator.SOURCE_OVER, 1f).color()
        }
    }

    override fun replicateMoment(): ColorGrid {
        val colorGrid = ColorGrid()
        colorGrid.copyColorsFrom(this)
        colorGrid.translateCol = translateCol
        colorGrid.translateRow = translateRow
        return colorGrid
    }

    override fun isIdenticalTo(moment: Grid): Boolean {
        if (moment !is ColorGrid) {
            return false
        }
        if (translateCol != moment.translateCol || translateRow != moment.translateRow) {
            return false
        }
        for (i in 0 until SIDE) {
            for (j in 0 until SIDE) {
                if (colors[i][j] != moment.colors[i][j]) {
                    return false
                }
            }
        }
        return true
    }

    private fun checkValue(value: Int, valueName: String) {
        if (value < 1 || value > SIDE) {
            throw IllegalArgumentException("$valueName value should be between 1 and $SIDE but was $value")
        }
    }

    fun translate(translateCol: Int, translateRow: Int) {
        transientTranslateCol = translateCol
        transientTranslateRow = translateRow
    }

    fun freezeTranslation() {
        translateCol = bound(columnTranslation)
        translateRow = bound(rowTranslation)
        transientTranslateCol = 0
        transientTranslateRow = 0
    }

    val rowTranslation: Int
        get() = translateRow + transientTranslateRow

    val columnTranslation: Int
        get() = translateCol + transientTranslateCol

    fun initializeScratch(): Grid {
        scratch = ColorGrid()
        return scratch as ColorGrid
    }

    fun rasterScratch() {
        if (scratch != null) {
            val scratch = this.scratch as ColorGrid
            this.scratch = null
            val grid = blendUseCase.compose(this, scratch, AvailableBlendMode.NORMAL, AvailablePorterDuffOperator.SOURCE_OVER, 1f)
            copyColorsFrom(grid)
        }
    }

    private fun copyColorsFrom(grid: ColorGrid) {
        for (i in 0 until SIDE) {
            System.arraycopy(grid.colors[i], 0, colors[i], 0, SIDE)
        }
    }

    private fun bound(value: Int): Int = value.clip(-SIDE, SIDE)

    companion object {

        const val SIDE = 16

        fun isOutOfBounds(column: Int, row: Int): Boolean {
            return row < 1 || column < 1 || row > SIDE || column > SIDE
        }
    }
}
