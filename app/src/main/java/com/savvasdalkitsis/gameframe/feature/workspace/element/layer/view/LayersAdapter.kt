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
package com.savvasdalkitsis.gameframe.feature.workspace.element.layer.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.LayerSettings
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.infra.kotlin.Action
import com.savvasdalkitsis.gameframe.infra.kotlin.TypeAction
import com.savvasdalkitsis.gameframe.infra.kotlin.findIndexOrThrow
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

internal class LayersAdapter : RecyclerView.Adapter<LayerViewHolder>() {

    private lateinit var modelHistory: HistoryUseCase<WorkspaceModel>
    private var onSelectedAction: Action? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayerViewHolder(parent)

    override fun onBindViewHolder(holder: LayerViewHolder, position: Int) = with(holder) {
        clearListeners()
        bind(layers()[position])
        setOnClickListener { selectWithHistory(holder.adapterPosition) }
        setOnLayerVisibilityChangedListener { visible -> modifyLayer(holder, { it.isVisible = visible }) }
        setOnItemDeletedListener { removeLayer(holder) }
        setOnLayerDuplicatedListener { duplicateLayer(holder) }
        setOnLayerSettingsClickedListener { layerSettings(holder) }
    }

    override fun getItemCount() = layers().size

    private fun modifyLayer(holder: LayerViewHolder, layerModifier: TypeAction<Layer>) {
        val position = holder.adapterPosition
        progressTime()
        val layer = layers()[position]
        layerModifier(layer)
        notifyItemChanged(position)
        notifyObservers()
    }

    private fun selectWithHistory(position: Int) {
        progressTime()
        select(position)
        onSelectedAction?.invoke()
    }

    private fun select(position: Int) {
        val selectedItemPosition = selectedItemPosition()
        layers()[selectedItemPosition].isSelected = false
        layers()[position].isSelected = true
        notifyItemChanged(selectedItemPosition)
        notifyItemChanged(position)
        notifyObservers()
    }

    private fun removeLayer(holder: LayerViewHolder) {
        val position = holder.adapterPosition
        progressTime()
        if (layers()[position].isSelected) {
            layers()[position - 1].isSelected = true
            notifyItemChanged(position - 1)
        }
        layers().removeAt(position)
        notifyItemRemoved(position)
        notifyObservers()
    }

    private fun duplicateLayer(holder: LayerViewHolder) {
        val position = holder.adapterPosition
        val layer = layers()[position]
        val newLayer = layer.replicateMoment()
        newLayer.layerSettings.title = layer.layerSettings.title + " copy"
        addNewLayer(newLayer, position + 1)
    }

    fun swapStarted() = modelHistory.progressTimeWithoutAnnouncing()

    fun swapLayers(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val itemPosition = viewHolder.adapterPosition
        val targetPosition = target.adapterPosition
        Collections.swap(layers(), itemPosition, targetPosition)
        notifyItemMoved(itemPosition, targetPosition)
    }

    fun swapLayersFinished() {
        modelHistory.collapsePresentWithPastIfTheSame()
        notifyObservers()
    }

    private fun layerSettings(holder: LayerViewHolder) {
        val context = holder.itemView.context
        LayerSettingsView.show(context, layers()[holder.adapterPosition], holder.itemView as ViewGroup,
                object: LayerSettingsSetListener {
                    override fun onLayerSettingsSet(layerSettings: LayerSettings) {
                        modifyLayer(holder, { it.layerSettings = layerSettings })
                    }
                })
    }

    fun addNewLayer() = addNewLayer(Layer(layerSettings = LayerSettings(title = "Layer ${layers().size}")), layers().size)

    private fun addNewLayer(layer: Layer, position: Int) {
        progressTime()
        layers().add(position, layer)
        notifyItemInserted(position)
        select(position)
    }

    private fun layers() = modelHistory.present.layers

    private fun notifyObservers() = modelHistory.announcePresent()

    private fun progressTime() = modelHistory.progressTime()

    fun bind(modelHistory: HistoryUseCase<WorkspaceModel>, onSelectedAction: Action) {
        this.modelHistory = modelHistory
        this.onSelectedAction = onSelectedAction
        modelHistory.observe().subscribeOn(AndroidSchedulers.mainThread()).subscribe { notifyDataSetChanged() }
    }

    private fun selectedItemPosition() = layers().findIndexOrThrow(
            { it.isSelected },
            { IllegalStateException("Could not find selected layer") }
    )
}
