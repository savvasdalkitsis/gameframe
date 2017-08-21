package com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view

import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.PaletteSettings

internal interface PaletteSettingsSetListener {

    fun onPaletteSettingsSet(paletteSettings: PaletteSettings)
}
