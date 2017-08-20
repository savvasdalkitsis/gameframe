package com.savvasdalkitsis.gameframe.feature.draw.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.draw.model.Palette
import com.savvasdalkitsis.gameframe.infra.kotlin.Action

internal class PaletteViewHolder(parent: ViewGroup, editable: Boolean) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_palette_view, parent, false)) {

    private val paletteView: PaletteView = itemView.findViewById(R.id.view_palette_thumbnail)
    private val title: TextView = itemView.findViewById(R.id.view_palette_title)
    private val edit: View = itemView.findViewById(R.id.view_palette_edit)
    private val delete: View = itemView.findViewById(R.id.view_palette_delete)
    private val controls: View = itemView.findViewById(R.id.view_palette_controls)

    init {
        paletteView.setThumbnailMode()
        setEditable(editable)
    }

    private fun setEditable(editable: Boolean) {
        controls.visibility = if (editable) View.VISIBLE else View.GONE
    }

    fun setDeletable(deletable: Boolean) {
        delete.visibility = if (deletable) View.VISIBLE else View.GONE
    }

    fun bind(palette: Palette) {
        title.text = palette.title
        paletteView.bind(palette)
    }

    fun clearListeners() {
        setOnClickListener(null)
        setOnItemDeletedListener(null)
    }

    fun setOnClickListener(onClickListener: Action?) {
        itemView.setOnClickListener { onClickListener?.invoke() }
    }

    fun setOnItemDeletedListener(onItemDeletedListener: Action?) {
        delete.setOnClickListener { onItemDeletedListener?.invoke() }
    }

    fun setOnPaletteEditClickedListener(onPaletteEditClickedListener: Action) {
        edit.setOnClickListener { onPaletteEditClickedListener() }
    }
}