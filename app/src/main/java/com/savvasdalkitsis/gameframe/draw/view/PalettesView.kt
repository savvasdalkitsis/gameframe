package com.savvasdalkitsis.gameframe.draw.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

import com.savvasdalkitsis.gameframe.draw.model.Palette
import com.savvasdalkitsis.gameframe.history.model.Historical
import com.savvasdalkitsis.gameframe.draw.model.Model

class PalettesView : RecyclerView {

    private val palettes = PalettesAdapter()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setHasFixedSize(true)
        adapter = palettes
    }

    fun addNewPalette(palette: Palette) {
        palettes.addNewPalette(palette)
    }

    fun bind(modelHistory: Historical<Model>) {
        palettes.bind(modelHistory)
    }

}