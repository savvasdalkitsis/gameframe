package com.savvasdalkitsis.gameframe.draw.view

import com.savvasdalkitsis.gameframe.draw.model.PaletteSettings

internal interface PaletteSettingsSetListener {

    fun onPaletteSettingsSet(paletteSettings: PaletteSettings)
}
