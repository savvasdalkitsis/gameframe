package com.savvasdalkitsis.gameframe.feature.draw.view

import com.savvasdalkitsis.gameframe.feature.draw.model.PaletteSettings

internal interface PaletteSettingsSetListener {

    fun onPaletteSettingsSet(paletteSettings: PaletteSettings)
}
