package com.savvasdalkitsis.gameframe.feature.draw.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import com.savvasdalkitsis.gameframe.feature.draw.model.Palette


internal class SimplePalettesAdapter(private val palettes: Array<Palette>) : RecyclerView.Adapter<PaletteViewHolder>() {

    private var onAddNewPaletteSelectedListener: AddNewPaletteSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PaletteViewHolder(parent, false)

    override fun onBindViewHolder(holder: PaletteViewHolder, position: Int) = with(holder) {
        clearListeners()
        bind(palettes[position])
        setOnClickListener { onAddNewPaletteSelectedListener?.onAddNewPalletSelected(palettes[position]) }
    }

    override fun getItemCount() = palettes.size

    fun setOnAddNewPaletteSelectedListener(onAddNewPaletteSelectedListener: AddNewPaletteSelectedListener) {
        this.onAddNewPaletteSelectedListener = onAddNewPaletteSelectedListener
    }

}
