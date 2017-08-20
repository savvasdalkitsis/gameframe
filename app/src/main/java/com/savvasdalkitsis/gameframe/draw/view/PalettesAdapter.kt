package com.savvasdalkitsis.gameframe.draw.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.savvasdalkitsis.gameframe.draw.model.Model
import com.savvasdalkitsis.gameframe.draw.model.Palette
import com.savvasdalkitsis.gameframe.draw.model.PaletteSettings
import com.savvasdalkitsis.gameframe.infra.kotlin.findIndexOrThrow
import com.savvasdalkitsis.gameframe.history.model.Historical


internal class PalettesAdapter : RecyclerView.Adapter<PaletteViewHolder>() {

    private lateinit var modelHistory: Historical<Model>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PaletteViewHolder(parent, true)

    override fun onBindViewHolder(holder: PaletteViewHolder, position: Int) = with(holder) {
        clearListeners()
        bind(palettes()[position])
        setOnClickListener { selectWithHistory(holder.adapterPosition) }
        setOnItemDeletedListener { removePalette(holder) }
        setOnPaletteEditClickedListener { paletteEdit(holder) }
        setDeletable(palettes().size > 1)
    }

    override fun getItemCount() = palettes().size

    private fun selectWithHistory(position: Int) {
        progressTime()
        select(position)
        notifyObservers()
    }

    private fun select(position: Int) {
        val selectedItemPosition = selectedItemPosition()
        palettes()[selectedItemPosition].isSelected = false
        palettes()[position].isSelected = true
        notifyItemChanged(selectedItemPosition)
        notifyItemChanged(position)
    }

    private fun modifyPalette(holder: PaletteViewHolder, paletteModifier: (Palette) -> Unit) {
        val position = holder.adapterPosition
        progressTime()
        val palette = palettes()[position]
        paletteModifier(palette)
        notifyItemChanged(position)
        notifyObservers()
    }

    private fun removePalette(holder: PaletteViewHolder) {
        val position = holder.adapterPosition
        progressTime()
        if (palettes()[position].isSelected) {
            palettes()[position - 1].isSelected = true
            notifyItemChanged(position - 1)
        }
        palettes().removeAt(position)
        notifyItemRemoved(position)
        notifyObservers()
        if (palettes().size == 1) {
            notifyDataSetChanged()
        }
    }

    fun addNewPalette(palette: Palette) {
        progressTime()
        palettes().add(palette.replicateMoment().copy(isSelected = true))
        val lastIndex = palettes().size - 1
        notifyItemInserted(lastIndex)
        select(lastIndex)
        notifyObservers()
        if (palettes().size == 2) {
            notifyDataSetChanged()
        }
    }

    private fun paletteEdit(holder: PaletteViewHolder) {
        val context = holder.itemView.context
        PaletteSettingsView.show(context, palettes()[holder.adapterPosition], holder.itemView as ViewGroup,
                object: PaletteSettingsSetListener {
                    override fun onPaletteSettingsSet(paletteSettings: PaletteSettings) {
                        modifyPalette(holder, { it.title = paletteSettings.title ?: "" })
                    }
                })
    }

    private fun palettes() = modelHistory.present.palettes

    private fun notifyObservers() = modelHistory.announcePresent()

    private fun progressTime() = modelHistory.progressTime()

    fun bind(modelHistory: Historical<Model>) {
        this.modelHistory = modelHistory
        modelHistory.observe().subscribe { notifyDataSetChanged() }
    }

    private fun selectedItemPosition() = palettes().findIndexOrThrow({ it.isSelected }, { IllegalStateException("Could not find selected palette") })
}
