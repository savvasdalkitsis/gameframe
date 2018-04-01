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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.kotlin.Action
import com.savvasdalkitsis.gameframe.kotlin.visibleOrGone

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
        controls.visibleOrGone(editable)
    }

    fun setDeletable(deletable: Boolean) {
        delete.visibleOrGone(deletable)
    }

    fun bind(palette: Palette) {
        itemView.isSelected = palette.isSelected
        title.text = palette.title
        paletteView.bind(palette)
        paletteView.isSelected = false
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