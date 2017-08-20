package com.savvasdalkitsis.gameframe.feature.draw.view

import com.savvasdalkitsis.gameframe.feature.draw.model.LayerSettings

interface LayerSettingsSetListener {
    fun onLayerSettingsSet(layerSettings: LayerSettings)
}
