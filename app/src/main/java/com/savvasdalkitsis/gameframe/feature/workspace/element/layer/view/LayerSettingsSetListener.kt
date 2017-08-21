package com.savvasdalkitsis.gameframe.feature.workspace.element.layer.view

import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.LayerSettings

interface LayerSettingsSetListener {
    fun onLayerSettingsSet(layerSettings: LayerSettings)
}
