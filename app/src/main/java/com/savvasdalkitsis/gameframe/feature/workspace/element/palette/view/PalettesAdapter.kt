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
import android.view.ViewGroup
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.PaletteSettings
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.infra.kotlin.Action
import com.savvasdalkitsis.gameframe.infra.kotlin.findIndexOrThrow
import io.reactivex.android.schedulers.AndroidSchedulers


internal class PalettesAdapter : RecyclerView.Adapter<PaletteViewHolder>() {

    private lateinit var modelHistory: HistoryUseCase<WorkspaceModel>
    private var onSelectedAction: Action? = null

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
        onSelectedAction?.invoke()
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
            val newSelectedPosition = if (position > 0) position - 1 else position + 1
            palettes()[newSelectedPosition].isSelected = true
            notifyItemChanged(newSelectedPosition)
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
                object : PaletteSettingsSetListener {
                    override fun onPaletteSettingsSet(paletteSettings: PaletteSettings) {
                        modifyPalette(holder, { it.title = paletteSettings.title ?: "" })
                    }
                })
    }

    private fun palettes() = modelHistory.present.palettes

    private fun notifyObservers() = modelHistory.announcePresent()

    private fun progressTime() = modelHistory.progressTime()

    fun bind(modelHistory: HistoryUseCase<WorkspaceModel>, onSelectedAction: Action) {
        this.modelHistory = modelHistory
        this.onSelectedAction = onSelectedAction
        modelHistory.observe().subscribeOn(AndroidSchedulers.mainThread()).subscribe { notifyDataSetChanged() }
    }

    private fun selectedItemPosition() = palettes().findIndexOrThrow({ it.isSelected }, { IllegalStateException("Could not find selected palette") })
}
