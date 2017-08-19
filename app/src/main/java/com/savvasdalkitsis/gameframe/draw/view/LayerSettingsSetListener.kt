package com.savvasdalkitsis.gameframe.draw.view

import com.savvasdalkitsis.gameframe.draw.model.LayerSettings

interface LayerSettingsSetListener {
    fun onLayerSettingsSet(layerSettings: LayerSettings)
}
