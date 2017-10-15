package com.savvasdalkitsis.gameframe.feature.workspace.element.layer.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.view.LedGridView
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.infra.kotlin.*

internal class LayerViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_layer_view, parent, false)) {

    private val ledGridView: LedGridView = itemView.findViewById(R.id.view_layer_thumbnail)
    private val visibilityVisible: View = itemView.findViewById(R.id.view_layer_visibility_visible)
    private val visibilityInvisible: View = itemView.findViewById(R.id.view_layer_visibility_invisible)
    private val delete: View = itemView.findViewById(R.id.view_layer_delete)
    private val duplicate: View = itemView.findViewById(R.id.view_layer_duplicate)
    private val settings: View = itemView.findViewById(R.id.view_layer_settings)
    private val title: TextView = itemView.findViewById(R.id.view_layer_title)

    init {
        ledGridView.setThumbnailMode()
    }

    fun bind(layer: Layer) {
        itemView.isSelected = layer.isSelected
        title.text = layer.layerSettings.title
        ledGridView.display(layer.colorGrid)
        delete.visible()
        duplicate.visible()
        settings.visible()
        visibilityVisible.visibleOrGone(layer.isVisible)
        visibilityInvisible.visibleOrGone(!layer.isVisible)
        if (layer.isBackground) {
            listOf(delete, duplicate, settings, visibilityInvisible, visibilityVisible)
                    .forEach { it.gone() }
        }
    }

    fun clearListeners() {
        setOnClickListener(null)
        setOnItemDeletedListener(null)
        setOnLayerDuplicatedListener(null)
        setOnLayerSettingsClickedListener(null)
        setOnLayerVisibilityChangedListener(null)
    }

    fun setOnClickListener(onClickListener: ViewAction?) =
            itemView.setOnClickListener(onClickListener)

    fun setOnItemDeletedListener(onItemDeletedListener: Action?) =
            delete.setOnClickListener { onItemDeletedListener?.invoke() }

    fun setOnLayerDuplicatedListener(onLayerDuplicatedListener: Action?) =
            duplicate.setOnClickListener { onLayerDuplicatedListener?.invoke() }

    fun setOnLayerSettingsClickedListener(onLayerSettingsClickedListener: Action?) =
            settings.setOnClickListener { onLayerSettingsClickedListener?.invoke() }

    fun setOnLayerVisibilityChangedListener(onLayerVisibilityChangedListener: OnLayerVisibilityChangedListener?) {
        visibilityVisible.setOnClickListener { onLayerVisibilityChangedListener?.invoke(false) }
        visibilityInvisible.setOnClickListener { onLayerVisibilityChangedListener?.invoke(true) }
    }
}