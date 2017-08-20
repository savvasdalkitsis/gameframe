package com.savvasdalkitsis.gameframe.feature.draw.view

import android.content.Context
import android.support.v7.widget.GridLayout
import android.util.AttributeSet

import com.savvasdalkitsis.gameframe.feature.draw.model.Palette

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
            throw IllegalStateException("Wrong number of swatches in PaletteView. Expecting $SWATCH_COUNT found $childCount")
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
